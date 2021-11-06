package modelo.fuente;

import java.io.*;
import java.util.TreeMap;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */

public class Fuente {
    protected static final String RESOURCESPATH = "src" + File.separator + "resources" + File.separator;
    protected static final String RESULTSPATH = "src" + File.separator + "results" + File.separator;
    protected TreeMap<String, Integer> frec = new TreeMap<>();
    protected TreeMap<String, Double> prob = new TreeMap<>();
    protected TreeMap<String, Double> info = new TreeMap<>();
    protected String inputfile;
    protected int digitosPalabra;

    public void parseFile(String inputfile, int digitosPalabra) throws IOException {
        Reader reader = new FileReader(RESOURCESPATH + inputfile);
        char[] buffer = new char[digitosPalabra];
        String word;

        this.inputfile = inputfile;
        this.digitosPalabra = digitosPalabra;
        while (reader.read(buffer) != -1) {
            word = new String(buffer);
            this.frec.put(word, this.frec.getOrDefault(word, 0) + 1);
        }
        reader.close();
        this.calculcarProbCantInfo();
    }

    public void calculcarProbCantInfo() {
        int total = this.frec.values().stream().mapToInt(Integer::intValue).sum();
        double prob;

        for (String str : this.frec.keySet()) {
            prob = (double) this.frec.get(str) / total;
            this.prob.put(str, prob);
            this.info.put(str, cantidadDeInformacion(prob));
        }
    }

    public void printFrecuencies() {
        System.out.println("Frecuencias: ");
        for (String s : this.frec.keySet()) {
            System.out.println("[" + s + "] = " + this.frec.get(s));
        }
        System.out.println("total = " + this.frec.values().stream().mapToInt(Integer::intValue).sum());
        System.out.println();
    }

    public void printCantidadDeInfo() {
        System.out.println("Cantidad de info: ");
        for (String s : this.frec.keySet()) {
            System.out.println("I(" + s + ") = " + this.info.get(s));
        }
        System.out.println();
    }

    public void printProbabilidad() {
        System.out.println("Probabilidades: ");
        for (String s : this.frec.keySet()) {
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
        for (String s : this.frec.keySet()) {
            h += this.prob.get(s) * this.info.get(s);
        }
        return h;
    }

    public double kraft() {
        double suma = 0;
        for (String key : this.frec.keySet()) {
            suma += Math.pow(2.0, -1.0 * key.length());
        }
        return suma;
    }

    public double longitudMedia() {
        double longitud = 0;
        for (String key : this.frec.keySet()) {
            longitud += this.prob.get(key) * key.length();
        }
        return longitud;
    }

    public double rendimiento() {
        return this.entropia() / this.longitudMedia() * 100.0;
    }

    public double redundancia() {
        return 100.0 - this.rendimiento();
    }

    public void writeToTxt(String outputfile) throws IOException {
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(RESULTSPATH + outputfile));

        System.out.println(this);
        System.out.println("Entropia:");
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

    public void writeToCsv(String outputfile) throws IOException {
        Writer file = new FileWriter(RESULTSPATH + outputfile);
        int total = this.frec.values().stream().mapToInt(Integer::intValue).sum();
        double suma = this.prob.values().stream().mapToDouble(Double::doubleValue).sum();

        file.write(String.join(",", "Palabras", "Frecuencia", "Probabilidad", "Cant. Informacion", "\n"));
        for (String key : this.frec.keySet()) {
            file.write(String.join(",", key, Integer.toString(this.frec.get(key)), Double.toString(this.prob.get(key)),
                    Double.toString(this.info.get(key)), "\n"));
        }
        file.write(String.join(",", " ", Integer.toString(total), Double.toString(suma), " \n"));
        file.write("\n");
        file.write(String.join(",", "Entropia H(S)", Double.toString(this.entropia()), "\n"));
        file.write(String.join(",", "Kraft", Double.toString(this.kraft()), "\n"));
        file.write(String.join(",", "Rendimiento", Double.toString(this.rendimiento()), "\n"));
        file.write(String.join(",", "Redundancia", Double.toString(this.redundancia()), "\n"));
        file.close();
    }

    protected void clearAll() {
        this.prob.clear();
        this.frec.clear();
        this.info.clear();
        this.inputfile = null;
        this.digitosPalabra = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Escenario: palabras ").append(this.digitosPalabra).append(" digitos.\n").append("\n");
        sb.append(String.format("%-15s%-15s%-20s%-20s\n", "Palabra", "Frecuencia", "Probabilidad", "Cant. informacion"));
        sb.append(new String(new char[67]).replace('\0', '-')).append("\n");
        for (String palabra : this.prob.keySet()) {
            sb.append(String.format("%-15s%-15d%-20.15f%-20.15f\n", palabra, this.frec.get(palabra), this.prob.get(palabra), this.info.get(palabra)));
        }
        sb.append(new String(new char[67]).replace('\0', '-')).append("\n");
        sb.append(String.format("%-15s%-15s%-20s%-20s\n", this.prob.keySet().size(), this.frec.values().stream().mapToInt(Integer::intValue).sum(), this.prob.values().stream().mapToDouble(Double::doubleValue).sum(), ""));
        return sb.toString();
    }
}