package modelo.compresion;

import modelo.fuente.Fuente;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class ShannonFano extends Fuente implements Compressor {
    protected final TreeMap<String, String> shannonCodes = new TreeMap<>();
    private NodoShannonFano root;

    public void generarArbolCodificacion() {
        ArrayList<Nodo> nodos = new ArrayList<>();
        for (String s : this.prob.keySet()) {
            nodos.add(new Nodo(s, this.prob.get(s), null, null));
        }
        Collections.sort(nodos);
        this.root = new NodoShannonFano(nodos, null, null);
        this.generarArbolCodificacionRec(this.root);
    }

    private void generarArbolCodificacionRec(NodoShannonFano nodo) {
        if (nodo.getCantNodosInternos() > 1) {
            nodo.izq = new NodoShannonFano(nodo.getMitadIzq(), null, null);
            nodo.der = new NodoShannonFano(nodo.getMitadDer(), null, null);
            generarArbolCodificacionRec(nodo.izq);
            generarArbolCodificacionRec(nodo.der);
        }
    }

    public void generarCodigos() {
        this.encode(this.root, "");
    }

    private void encode(NodoShannonFano root, String s) {
        if (root.isHoja()) {
            this.shannonCodes.put(root.getSimbolo(), s);
        } else {
            encode(root.izq, s + "0");
            encode(root.der, s + "1");
        }
    }

    public void printCodes() {
        System.out.printf("%-15s %-15s\n", "Palabra", "ShannonFano");
        for (String s : this.shannonCodes.keySet()) {
            System.out.printf("%-15s %-15s%n", s, this.shannonCodes.get(s));
        }
    }

    public void writeToTxt(String filename) throws FileNotFoundException {
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
            suma += Math.pow(2.0, -1.0 * this.shannonCodes.get(key).length());
        }
        return suma;
    }

    @Override
    public double longitudMedia() {
        double longitud = 0;
        for (String key : this.frec.keySet()) {
            longitud += this.prob.get(key) * this.shannonCodes.get(key).length();
        }
        return longitud;
    }

    public void writeToCsv(String filename) throws IOException {
        Writer file = new FileWriter(RESULTSPATH + filename);

        file.write(String.join(",", "Probabilidad", "Codigo", "ShannonFano\n"));
        for (String s : this.shannonCodes.keySet()) {
            file.write(String.join(",", Double.toString(this.prob.get(s)), s, this.shannonCodes.get(s), "\n"));
        }
        file.close();
    }

    public void compress() throws IOException {
        String newfile = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".fan";
        Reader reader = new FileReader(RESOURCESPATH + this.inputfile);
        BitOutputStream bitOutputStream = new BitOutputStream();
        String word;
        int caracter;

        while ((caracter = reader.read()) != -1) {
            word = Character.toString((char) caracter);
            String shanncode = this.shannonCodes.get(word);
            bitOutputStream.addBits(shanncode);
        }
        bitOutputStream.writeTo(new FileOutputStream(newfile));
        reader.close();
    }

    public void decompress() throws IOException {
        String filename = RESULTSPATH + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".fan";
        Writer writer = new FileWriter(Fuente.RESOURCESPATH + "recoveryShannon" + this.inputfile.substring(0, this.inputfile.lastIndexOf('.')) + ".txt");
        BitInputStream bitInputStream = new BitInputStream();
        NodoShannonFano nodo;
        int c;

        bitInputStream.readFrom(new FileInputStream(filename));
        while ((c = bitInputStream.nextBit()) != -1) {
            nodo = this.root;
            nodo = c == 0 ? nodo.izq : nodo.der;
            while (!nodo.isHoja()) {
                nodo = bitInputStream.nextBit() == 0 ? nodo.izq : nodo.der;
            }
            writer.write(nodo.getSimbolo());
        }
        writer.close();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        this.root = null;
        this.shannonCodes.clear();
    }

    @Override
    public String toString() {
        StringBuilder shan = new StringBuilder();
        for (String s : this.shannonCodes.keySet()) {
            shan.append(String.format("word=%s, code=%s\n", s, this.shannonCodes.get(s)));
        }
        return shan.toString();
    }
}
