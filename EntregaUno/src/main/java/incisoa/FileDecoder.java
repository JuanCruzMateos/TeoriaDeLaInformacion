package incisoa;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

public class FileDecoder {
    private final HashMap<String, Integer> frec = new HashMap<>();
    private final HashMap<String, Double> prob = new HashMap<>();
    private final HashMap<String, Double> cantInfo = new HashMap<>();
    private TreeSet<String> palabras;

    public static void main(String[] args) throws IOException {
        FileDecoder fd = new FileDecoder();

        fd.probCondicionales("src/resources/anexo1-grupo5.txt");
    }
    public void parseFile(String inputfile, int digitosPalabra) throws IOException {
        Reader reader = new FileReader(inputfile);
        char[] buffer = new char[digitosPalabra];
        String word;
        int total;
        double prob;

        while (reader.read(buffer) != -1) {
            word = new String(buffer);
            this.frec.put(word, this.frec.getOrDefault(word, 0) + 1);
        }
        reader.close();

        this.palabras = new TreeSet<>(this.frec.keySet()); // para imprimir ordenado
        total = this.frec.values().stream().mapToInt(Integer::intValue).sum();

        for (String str : this.palabras) {
            prob = (double) this.frec.get(str) / total;
            this.prob.put(str, prob);
            this.cantInfo.put(str, cantidadDeInformacion(prob));
        }
    }

    public void printFrecuencies() {
        System.out.println("Frecuencias: ");
        for (String s : this.palabras) {
            System.out.println("[" + s + "] = " + this.frec.get(s));
        }
        System.out.println("total = " + this.frec.values().stream().mapToInt(Integer::intValue).sum());
        System.out.println();
    }

    public void printCantidadDeInfo() {
        System.out.println("Cantidad de info: ");
        for (String s : this.palabras) {
            System.out.println("I(" + s + ") = " + this.cantInfo.get(s));
        }
        System.out.println();
    }

    public void printProbabilidad() {
        System.out.println("Probabilidades: ");
        for (String s : this.palabras) {
            System.out.println("P(" + s + ") = " + this.prob.get(s));
        }
        System.out.println("suma = " + this.prob.values().stream().mapToDouble(Double::doubleValue).sum());
        System.out.println();
    }

    private double cantidadDeInformacion(double prob) {
        return -1 * Math.log(prob) / Math.log(2);
    }

    public double entropia() {
        double h = 0;
        for (String s : this.palabras) {
            h += this.prob.get(s) * this.cantInfo.get(s);
        }
        return h;
    }

    public void writeToTxt(String outputfile) throws IOException {
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(outputfile));

        this.printFrecuencies();
        this.printProbabilidad();
        this.printCantidadDeInfo();
        System.out.println("Entropia:");
        System.out.println("H(S) = " + this.entropia());
        System.setOut(stdout);
    }

    public void writeToCsv(String outputfile) throws IOException {
        Writer file = new FileWriter(outputfile);
        int total = this.frec.values().stream().mapToInt(Integer::intValue).sum();
        double suma = this.prob.values().stream().mapToDouble(Double::doubleValue).sum();

        file.write(String.join(",", "Palabras", "Frecuencia", "Probabilidad", "Cant. Informacion", "\n"));
        for (String key : this.palabras) {
            file.write(String.join(",", key, Integer.toString(this.frec.get(key)), Double.toString(this.prob.get(key)),
                    Double.toString(this.cantInfo.get(key)), "\n"));
        }
        file.write(String.join(",", " ", Integer.toString(total), Double.toString(suma), " \n"));
        file.write(String.join(",", "Entropia H(S)", Double.toString(this.entropia()), "\n"));
        file.close();
    }

    public void clearAll() {
        this.palabras.clear();
        this.prob.clear();
        this.frec.clear();
        this.cantInfo.clear();
    }

    private int toDec(int bin) {
        int dec = 0, i = 1;
        int nro;

        do {
            nro = bin % 10;
            dec += nro * i;
            i *= 2;
            bin = bin / 10;
        } while (bin != 0);
        return dec;
    }

    public void probCondicionales(String inputfile) throws IOException {
        double[][] probs = new double[4][4];
        Reader reader = new FileReader(inputfile);
        char[] buffer = new char[2];
        int ant, act;

        if (reader.read(buffer) == -1) {
            throw new IOException("Eof");
        }
        ant = this.toDec(Integer.parseInt(new String(buffer)));
        while (reader.read(buffer) != -1) {
            act = this.toDec(Integer.parseInt(new String(buffer)));
            probs[act][ant] += 1;
            ant = act;
        }
        reader.close();

        System.out.println(Arrays.deepToString(probs));

        
    }
}
