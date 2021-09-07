import java.util.Scanner;
import java.io.*;
import java.util.*;


public class FileParser {
    private String inputfile;
    private String outputfile;
    private int digitosPalabra;
    private ArrayList<String> events;
    private HashMap<String, Integer> frec;
    private HashMap<String, Double> prob;
    private HashMap<String, Double> cantInfo;
    

    public FileParser(String inputfile, int digitosPalabra) {
        this.inputfile = inputfile;
        this.digitosPalabra = digitosPalabra;
    }


    public void parseFile() throws FileNotFoundException, IOException {
        File file = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Scanner scan = null;
        StringBuilder sb = new StringBuilder();
        String regex;

        file = new File(this.inputfile);
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        scan = new Scanner(bufferedReader);
        
        sb.append("(?<=\\G");
        for (int i = 0; i < this.digitosPalabra; i++) {
            sb.append(".");
        }
        sb.append(")");
        regex = sb.toString();
 
        String[] arr = scan.nextLine().split(regex);

        scan.close();
        bufferedReader.close();
        fileReader.close();
        
        this.events = new ArrayList<String>(Arrays.asList(arr));
        this.frec = new HashMap<>();
        for (String str : this.events) {
            this.frec.put(str, this.frec.getOrDefault(str, 0) + 1);
        }
        this.prob = new HashMap<>();
        for (String str : this.frec.keySet()) {
            this.prob.put(str, (double )this.frec.get(str) / this.events.size());
        }
        this.cantInfo = new HashMap<>();
        for (String str : this.frec.keySet()) {
            this.cantInfo.put(str, cantidadDeInformacion(this.prob.get(str)));
        }
    }


    public void printFrecuencies() {
        System.out.println("Frecuencias: ");

        for (String s : this.frec.keySet()) {
            System.out.println("[" + s + "] = " + this.frec.get(s));
        }
        System.out.println();
    }


    public void printCantidadDeInfo() {
        System.out.println("Cantidad de info: ");

        for (String s : this.cantInfo.keySet()) {
            System.out.println("I(" + s + ") = " + this.cantInfo.get(s));
        }
        System.out.println();
    }


    public void printProbabilidad() {
        double total = 0;
        System.out.println("Probabilidades: ");

        for (String s : this.cantInfo.keySet()) {
            System.out.println("P(" + s + ") = " + this.prob.get(s));
            total += this.prob.get(s);
        }
        System.out.println("sum = " + total);
        System.out.println();
    }


    private double cantidadDeInformacion(double prob) {
        return -1 * Math.log(prob) / Math.log(2);
    }
    

    public double entropia() {
        double h = 0;
        for (String s : this.prob.keySet()) {
            h += this.prob.get(s) * this.cantInfo.get(s);
        }
        return h;
    }

    public static void main(String[] args) {
        try {
            FileParser fp = new FileParser(args[0], Integer.valueOf(args[1]));
            fp.parseFile();
            fp.printFrecuencies();
            fp.printProbabilidad();
            fp.printCantidadDeInfo();
            System.out.println("H(S) = " + fp.entropia());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

