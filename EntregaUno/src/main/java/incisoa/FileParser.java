package incisoa;

import java.io.*;
import java.util.HashMap;
import java.util.TreeSet;

public class FileParser {
    private final HashMap<String, Integer> frec = new HashMap<>();
    private final HashMap<String, Double> prob = new HashMap<>();
    private final HashMap<String, Double> cantInfo = new HashMap<>();
    private TreeSet<String> palabras;

    public void parseFile(String inputfile, int digitosPalabra) throws IOException {
        String path = inputfile;
        Reader reader = new FileReader(path);
        char[] buffer = new char[digitosPalabra];
        String word;
        int total;

        while (reader.read(buffer) != -1) {
            word = new String(buffer);
            this.frec.put(word, this.frec.getOrDefault(word, 0) + 1);
        }
        reader.close();

        this.palabras = new TreeSet<>(this.frec.keySet()); // para imprimir ordenado

        total = this.frec.values().stream().mapToInt(Integer::intValue).sum();

        for (String str : this.palabras) {
            this.prob.put(str, (double) this.frec.get(str) / total);
            this.cantInfo.put(str, cantidadDeInformacion(this.prob.get(str)));
        }
    }

    public void printFrecuencies() {
        System.out.println("Frecuencias: ");
        for (String s : this.palabras) {
            System.out.println("[" + s + "] = " + this.frec.get(s));
        }
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
        double total = 0;

        System.out.println("Probabilidades: ");
        for (String s : this.palabras) {
            System.out.println("P(" + s + ") = " + this.prob.get(s));
            total += this.prob.get(s);
        }
        System.out.println("sum = " + total);
        System.out.println();
        // System.out.println("sum = " +
//         this.prob.values().stream().mapToDouble(Double::doubleValue).sum();
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
        String path = outputfile;
        System.setOut(new PrintStream(path));

        this.printFrecuencies();
        this.printProbabilidad();
        this.printCantidadDeInfo();
        System.out.println("H(S) = " + this.entropia());
        System.setOut(stdout);
    }

    public void writeToCsv(String outputfile) throws IOException {
        String path = outputfile;
        Writer file = new FileWriter(path);

        file.write(String.join(",", "Palabras", "Frecuencia", "Probabilidad", "Cant. Informacion", "\n"));
        for (String key : this.palabras) {
            file.write(String.join(",", key, Integer.toString(this.frec.get(key)), Double.toString(this.prob.get(key)),
                    Double.toString(this.cantInfo.get(key)), "\n"));
        }
        file.write(String.join(",", "Entropia", Double.toString(this.entropia())));
        file.close();
    }
}
