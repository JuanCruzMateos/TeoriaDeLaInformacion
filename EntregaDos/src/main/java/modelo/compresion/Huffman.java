package modelo.compresion;

import modelo.fuente.Fuente;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */
public class Huffman extends Fuente implements Compressor {
    protected static final String EXTENSION = ".huff";
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

    public void printCodes() {
        System.out.printf("%-15s %-15s\n", "Palabra", "Huffman");
        for (String s : this.huffcodes.keySet()) {
            System.out.printf("%-15s %-15s%n", s, this.huffcodes.get(s));
        }
    }

    @Override
    public void writeToTxt(String filename) throws IOException {
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(RESULTSPATH + filename));
        this.printCodes();
        System.out.println("\nEntropia:");
        System.out.println("H(S) = " + this.entropia() + " bits\n");
        System.out.println("Kraft:");
        System.out.println("k = " + this.kraft() + "\n");
        System.out.println("Longitud media:");
        System.out.println("L = " + this.longitudMedia() + "\n");
        System.out.println("Rendimiento:");
        System.out.println("n = " + this.rendimiento() + "\n");
        System.out.println("Rendundancia:");
        System.out.println("n = " + this.redundancia() + "\n");
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

        while ((caracter = reader.read()) != -1) {
            word = Character.toString((char) caracter);
            String huffcode = this.huffcodes.get(word);
            bitOutputStream.addBits(huffcode);
        }
        bitOutputStream.writeTo(new FileOutputStream(newfile));
        reader.close();
    }

    @Override
    public long getTasaDeCompresion() throws IOException {
        String originalFile = RESOURCESPATH + this.inputfile;
        String compressdFile = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + EXTENSION;

        long sizeOriginal = Files.size(Paths.get(originalFile));
        long sizeComprimido = Files.size(Paths.get(compressdFile));
        return sizeOriginal / sizeComprimido;
    }

    @Override
    public void decompress() throws IOException {
        String filename = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + EXTENSION;
        Writer writer = new FileWriter(Fuente.RESOURCESPATH + "recoveryHuffman" + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".txt");
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