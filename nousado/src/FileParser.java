import java.util.Scanner;
import java.io.*;
import java.util.*;

public class FileParser {
    private String inputfile;
    private String outputfile;
    private int digitosPalabra;
    private ArrayList<String> events;
    private TreeSet<String> palabras;
    private HashMap<String, Integer> frec;
    private HashMap<String, Double> prob;
    private HashMap<String, Double> cantInfo;

    public FileParser(String inputfile, int digitosPalabra, String outputfile) {
        this.inputfile = inputfile;
        this.digitosPalabra = digitosPalabra;
        this.outputfile = outputfile;
    }

    public void parseFile() throws FileNotFoundException, IOException {
        File file = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Scanner scan = null;
        String regex;

        file = new File(this.inputfile);
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        scan = new Scanner(bufferedReader);

        regex = "(?<=\\G.{" + String.valueOf(this.digitosPalabra) + "})";
        String[] arr = scan.nextLine().split(regex);

        scan.close();
        bufferedReader.close();
        fileReader.close();

        this.events = new ArrayList<String>(Arrays.asList(arr));
        this.frec = new HashMap<>();
        for (String str : this.events) {
            this.frec.put(str, this.frec.getOrDefault(str, 0) + 1);
        }
        this.palabras = new TreeSet<String>(this.frec.keySet());
        this.prob = new HashMap<>();
        for (String str : this.palabras) {
            this.prob.put(str, (double) this.frec.get(str) / this.events.size());
        }
        this.cantInfo = new HashMap<>();
        for (String str : this.palabras) {
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
        // this.prob.values().stream().mapToDouble(Double::doubleValue).sum());
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

    public void writeToCsv() throws IOException {
        Writer file = new FileWriter(this.outputfile);

        file.write(String.join(",", "Palabras", "Frecuencia", "Probabilidad", "Cant. Informacion", "\n"));
        for (String key : this.palabras) {
            file.write(String.join(",", key, Integer.toString(this.frec.get(key)), Double.toString(this.prob.get(key)),
                    Double.toString(this.cantInfo.get(key)), "\n"));
        }
        file.write(String.join(",", "Entropia", Double.toString(this.entropia())));
        file.close();
    }

    public static void main(String[] args) {
        try {
            FileParser fp = new FileParser(args[0], Integer.valueOf(args[1]), args[2]);
            fp.parseFile();
            fp.printFrecuencies();
            fp.printProbabilidad();
            fp.printCantidadDeInfo();
            System.out.println("H(S) = " + fp.entropia());
            fp.writeToCsv();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
