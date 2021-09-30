package modelo;

import java.io.*;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */

public class Huffman extends Fuente {
    private final TreeMap<String, String> huffcodes = new TreeMap<>();
    private Nodo root;

    public void crearArbol() {
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
        System.out.println("Codigo ---> Huffman");
        for (String s : this.huffcodes.keySet()) {
            System.out.println(s + " ---> " + this.huffcodes.get(s));
        }
    }

    public void writeHuffmanToTxt(String filename) throws FileNotFoundException {
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(filename));
        this.printCodes();
        System.setOut(stdout);
    }

    public void writeHuffmanToCsv(String filename) throws IOException {
        Writer file = new FileWriter(filename);

        file.write(String.join(",", "Probabilidad", "Codigo", "Huffman\n"));
        for (String s : this.huffcodes.keySet()) {
            file.write(String.join(",", Double.toString(this.prob.get(s)), s, this.huffcodes.get(s), "\n"));
        }
        file.close();
    }

    public void newHuffmanFile(String newfile) throws IOException {
        Reader reader = new FileReader(this.inputfile);
        char[] buffer = new char[this.digitosPalabra];
        Writer writer = new FileWriter(newfile);
        String word;

        while (reader.read(buffer) != -1) {
            word = new String(buffer);
            writer.write(this.huffcodes.get(word));
        }
        reader.close();
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
