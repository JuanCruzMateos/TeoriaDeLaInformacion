package incisoa;

import java.io.*;
import java.util.HashMap;
import java.util.TreeSet;

public class FileDecoder {
    private final HashMap<String, Integer> frec = new HashMap<>();
    private final HashMap<String, Double> prob = new HashMap<>();
    private final HashMap<String, Double> info = new HashMap<>();
    private TreeSet<String> palabras;

    public void parseFile(String inputfile, int digitosPalabra) throws IOException {
        Reader reader = new FileReader(inputfile);
        char[] buffer = new char[digitosPalabra];
        String word;
        double prob;
        int total;

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
            this.info.put(str, cantidadDeInformacion(prob));
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
            System.out.println("I(" + s + ") = " + this.info.get(s));
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
            h += this.prob.get(s) * this.info.get(s);
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
                    Double.toString(this.info.get(key)), "\n"));
        }
        file.write(String.join(",", " ", Integer.toString(total), Double.toString(suma), " \n"));
        file.write(String.join(",", "Entropia H(S)", Double.toString(this.entropia()), "\n"));
        file.close();
    }

    public void clearAll() {
        this.palabras.clear();
        this.prob.clear();
        this.frec.clear();
        this.info.clear();
    }
}
