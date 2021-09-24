package incisob;

import incisoa.Fuente;

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

    public static void main(String[] args) {
        Huffman huffman = new Huffman();

        try {
            huffman.parseFile("src/resources/anexo1-grupo5.txt", 2);
            huffman.crearTree();
            huffman.generarCodigos();
            huffman.writeHuffmanToTxt("src/results/huffman2digitos.txt");
            huffman.writeHuffmanToCsv("src/results/huffman2digitos.csv");
            huffman.clearAll();
            for (int i = 5; i < 10; i += 2) {
                huffman.parseFile("src/resources/anexo1-grupo5.txt", i);
                huffman.crearTree();
                huffman.generarCodigos();
                huffman.writeHuffmanToCsv("src/results/huffman" + i + "digitos.csv");
                huffman.clearAll();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void crearTree() {
        PriorityQueue<Nodo> pq = new PriorityQueue<>();
        Nodo hijoIzq, hijoDer, padre;

        for (String s : this.prob.keySet()) {
            pq.add(new Nodo(s, this.prob.get(s), null, null));
        }
        while (pq.size() > 1) {
            hijoIzq = pq.poll();
            hijoDer = pq.poll();
            padre = new Nodo(null, hijoIzq.prob + hijoDer.prob, hijoIzq, hijoDer);
            this.root = padre;
            pq.add(padre);
        }
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

    public void decode() {

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
        public int compareTo(Nodo otro) {
            return Double.compare(this.prob, otro.prob);
        }
    }
}
