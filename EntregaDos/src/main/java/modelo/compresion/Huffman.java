package modelo.compresion;

import modelo.fuente.Fuente;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */
public class Huffman extends Fuente implements Compressor {
    public static final String RESULTSPATH = Fuente.RESULTSPATH + "huffman" + File.separator;
    protected static final String EXTENSION = ".huf";
    protected final TreeMap<String, String> huffcodes = new TreeMap<>();
    protected Nodo root;

    @Override
    public void generarArbolCodificacion() {
        PriorityQueue<Nodo> pq = new PriorityQueue<>();
        Nodo hijoIzq, hijoDer, padre;

        for (String s : this.prob.keySet()) {
            pq.add(new Nodo(s, this.prob.get(s), null, null));
        }
        while (pq.size() > 1) {
            hijoIzq = pq.poll();
            hijoDer = pq.poll();
            padre = new Nodo(null, hijoIzq.prob + hijoDer.prob, hijoIzq, hijoDer);
            pq.add(padre);
        }
        this.root = pq.poll();
    }

    @Override
    public void generarCodigos() {
        this.encode(this.root, "");
    }

    private void encode(Nodo root, String s) {
        if (root.isHoja()) {
            this.huffcodes.put(root.simb, s);
        } else {
            encode(root.izq, s + "0");
            encode(root.der, s + "1");
        }
    }

    public String getDetalleCodificacion() {
        List<Map.Entry<String, Integer>> paraOrdenar = new ArrayList<>(this.frec.entrySet());
        paraOrdenar.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        List<String> clavesOrdenadasPorFrecuencia = new ArrayList<>();
        paraOrdenar.forEach(entry -> clavesOrdenadasPorFrecuencia.add(entry.getKey()));

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s%-15s%-20s%-20s%-20s\n", "Simbolo", "Frecuencia", "Probabilidad", "Cant. informacion", "Codigo Huffman"));
        sb.append(new String(new char[85]).replace('\0', '-')).append("\n");
//        for (String palabra : this.prob.keySet()) {
        for (String palabra : clavesOrdenadasPorFrecuencia) {
            sb.append(String.format("%-15s%-15d%-20.15f%-20.15f%-20s\n", Fuente.printable(palabra), this.frec.get(palabra), this.prob.get(palabra), this.info.get(palabra), this.huffcodes.get(palabra)));
        }
        sb.append(new String(new char[85]).replace('\0', '-')).append("\n");
        sb.append(String.format("%-15s%-15s%-20s%-20s\n", this.prob.keySet().size(), this.frec.values().stream().mapToInt(Integer::intValue).sum(), this.prob.values().stream().mapToDouble(Double::doubleValue).sum(), ""));
        return sb.toString();
    }

    @Override
    public void writeToTxt(String filename) throws IOException {
        String originalFile = RESOURCESPATH + this.inputfile;
        String compressdFile = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + EXTENSION;

        PrintStream stdout = System.out;
        System.setOut(new PrintStream(RESULTSPATH + filename));
        System.out.println(this.getDetalleCodificacion());
        System.out.println("\nEntropia:");
        System.out.println("H(S) = " + this.entropia() + " bits\n");
        System.out.println("Kraft:");
        System.out.println("k = " + this.kraft() + "\n");
        System.out.println("Longitud media:");
        System.out.println("L = " + this.longitudMedia() + "\n");
        System.out.println("Rendimiento:");
        System.out.println("n = " + this.rendimiento() + "\n");
        System.out.println("Rendundancia:");
        System.out.println("1 - n = " + this.redundancia() + "\n");
        System.out.println("Tamaño archivo original = " + Files.size(Paths.get(originalFile)) + " bytes");
        System.out.println("Tamaño archivo comprimido = " + Files.size(Paths.get(compressdFile)) + " bytes");
        System.out.println("Tasa de compresion = " + this.getTasaDeCompresion());
        System.setOut(stdout);
    }

    @Override
    public double kraft() {
        double suma = 0;
        for (String key : this.frec.keySet()) {
            suma += Math.pow(2.0, -1.0 * this.huffcodes.get(key).length());
        }
        return suma;
    }

    @Override
    public double longitudMedia() {
        double longitud = 0;
        for (String key : this.frec.keySet()) {
            longitud += this.prob.get(key) * this.huffcodes.get(key).length();
        }
        return longitud;
    }

    @Override
    public void writeToCsv(String filename) throws IOException {
        Writer file = new FileWriter(RESULTSPATH + filename);

        file.write(String.join(",", "Probabilidad", "Codigo", "Huffman\n"));
        for (String s : this.huffcodes.keySet()) {
            file.write(String.join(",", Double.toString(this.prob.get(s)), s, this.huffcodes.get(s), "\n"));
        }
        file.close();
    }

    @Override
    public void compress() throws IOException {
        String newfile = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + EXTENSION;
        Reader reader = new FileReader(RESOURCESPATH + this.inputfile);
        BitOutputStream bitOutputStream = new BitOutputStream();
        String word;
        int caracter;

        while ((caracter = reader.read()) != -1) {      // lee
            word = Character.toString((char) caracter);  // caracter: h
            String huffcode = this.huffcodes.get(word);  // h -> '10011'
            bitOutputStream.addBits(huffcode);
        }
        bitOutputStream.writeTo(new FileOutputStream(newfile));
        reader.close();
    }

    @Override
    public double getTasaDeCompresion() throws IOException {
        String originalFile = RESOURCESPATH + this.inputfile;
        String compressdFile = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + EXTENSION;

        long sizeOriginal = Files.size(Paths.get(originalFile));
        long sizeComprimido = Files.size(Paths.get(compressdFile));
        return (double) sizeOriginal / sizeComprimido;
    }

    @Override
    public void decompress() throws IOException {
        String filename = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + EXTENSION;
        Writer writer = new FileWriter(Fuente.RESOURCESPATH + "recovery" + File.separator + "recoveryHuffman" + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".txt");
        BitInputStream bitInputStream = new BitInputStream();
        Nodo nodo;
        int c;

        bitInputStream.readFrom(new FileInputStream(filename));
        while ((c = bitInputStream.nextBit()) != -1) {
            nodo = this.root;
            nodo = c == 0 ? nodo.izq : nodo.der;
            while (!nodo.isHoja()) {
                nodo = bitInputStream.nextBit() == 0 ? nodo.izq : nodo.der;
            }
            writer.write(nodo.simb);
        }
        writer.close();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        this.root = null;
        this.huffcodes.clear();
    }

    @Override
    public String toString() {
        StringBuilder huff = new StringBuilder();
        for (String s : this.huffcodes.keySet()) {
            huff.append(String.format("word=%s, code=%s\n", s, this.huffcodes.get(s)));
        }
        return huff.toString();
    }
}