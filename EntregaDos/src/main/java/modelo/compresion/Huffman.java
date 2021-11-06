package modelo.compresion;

import modelo.fuente.Fuente;

import java.io.*;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */

public class Huffman extends Fuente {
    protected final TreeMap<String, String> huffcodes = new TreeMap<>();
    protected Nodo root;

    public void crearArbolHuffman() {
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

    public void writeHuffmanToTxt(String filename) throws FileNotFoundException {
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
        System.out.println("n = " + this.redundancia());
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

    public void writeHuffmanToCsv(String filename) throws IOException {
        Writer file = new FileWriter(RESULTSPATH + filename);

        file.write(String.join(",", "Probabilidad", "Codigo", "Huffman\n"));
        for (String s : this.huffcodes.keySet()) {
            file.write(String.join(",", Double.toString(this.prob.get(s)), s, this.huffcodes.get(s), "\n"));
        }
        file.close();
    }

    public void compress() throws IOException {
        String newfile = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".huff";
        Reader reader = new FileReader(RESOURCESPATH + this.inputfile);
        char[] buffer = new char[this.digitosPalabra];
        BitOutputStream bitOutputStream = new BitOutputStream();
        String word;

        while (reader.read(buffer) != -1) {
            word = new String(buffer);
            String huffcode = this.huffcodes.get(word);
            bitOutputStream.addBits(huffcode);
        }
        bitOutputStream.write(newfile);
        reader.close();
    }

    public String getHuffmanCode(String word) {
        return this.huffcodes.get(word);
    }

    public void decompress() throws IOException {
        String filename = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".huff";
        Writer writer = new FileWriter(Fuente.RESOURCESPATH + "recovery" + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".txt");
        BitInputStream bitInputStream = new BitInputStream();
        Nodo nodo;
        int c;

        bitInputStream.read(filename);
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

    public TreeMap<String, String> getHuffcodes() {
        return huffcodes;
    }

    @Override
    public void clearAll() {
        super.clearAll();
        this.root = null;
        this.huffcodes.clear();
    }

    private static class Nodo implements Comparable<Nodo> {
        private final String simb;
        private final double prob;
        private final Nodo izq;
        private final Nodo der;

        public Nodo(String simb, double prob, Nodo izq, Nodo der) {
            this.simb = simb;
            this.prob = prob;
            this.izq = izq;
            this.der = der;
        }

        public boolean isHoja() {
            return this.izq == null && this.der == null;
        }

        @Override
        public int compareTo(Nodo o) {
            return Double.compare(this.prob, o.prob);
        }

        @Override
        public String toString() {
            return "{ " + this.prob + " }";
        }
    }
}