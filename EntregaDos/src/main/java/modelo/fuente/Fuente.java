package modelo.fuente;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.TreeMap;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */

public abstract class Fuente {
    public static final String RESOURCESPATH = "src" + File.separator + "resources" + File.separator;
    public static final String RESULTSPATH = "src" + File.separator + "results" + File.separator;
    protected TreeMap<String, Integer> frec = new TreeMap<>();
    protected TreeMap<String, Double> prob = new TreeMap<>();
    protected TreeMap<String, Double> info = new TreeMap<>();
    protected String inputfile;

    public void parseFile(String inputfile) throws IOException {
        Reader reader = new FileReader(RESOURCESPATH + inputfile);
        int caracter;
        String word;

        this.inputfile = inputfile;
        while ((caracter = reader.read()) != -1) {
            word = Character.toString((char) caracter);
            this.frec.put(word, this.frec.getOrDefault(word, 0) + 1);
        }
        reader.close();
        this.calculcarProbabilidades();
        this.calculcarCantidadDeInfo();
    }

    public void calculcarProbabilidades() {
        int total = this.frec.values().stream().mapToInt(Integer::intValue).sum();
        double prob;

        for (String str : this.frec.keySet()) {
            prob = (double) this.frec.get(str) / total;
            this.prob.put(str, prob);
        }
    }

    public void calculcarCantidadDeInfo() {
        int total = this.frec.values().stream().mapToInt(Integer::intValue).sum();
        double prob;

        for (String str : this.frec.keySet()) {
            prob = (double) this.frec.get(str) / total;
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

    public abstract double kraft();

    public abstract double longitudMedia();

//    public double kraft() {
//        double suma = 0;
//        for (String key : this.frec.keySet()) {
//            suma += Math.pow(2.0, -1.0 * key.length());
//        }
//        return suma;
//    }

//    public double longitudMedia() {
//        double longitud = 0;
//        for (String key : this.frec.keySet()) {
//            longitud += this.prob.get(key) * key.length();
//        }
//        return longitud;
//    }

    public double rendimiento() {
        return this.entropia() / this.longitudMedia() * 100.0;
    }

    public double redundancia() {
        return 100.0 - this.rendimiento();
    }

    protected void clearAll() {
        this.prob.clear();
        this.frec.clear();
        this.info.clear();
        this.inputfile = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

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
