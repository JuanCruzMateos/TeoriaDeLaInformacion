package incisoa;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            FileParser fp = new FileParser();
            fp.parseFile("src/resources/anexo1-grupo5.txt", 2);
            fp.printFrecuencies();
            fp.printProbabilidad();
            fp.printCantidadDeInfo();
            System.out.println("H(S) = " + fp.entropia());
            fp.writeToTxt("src/results/resultos2.txt");
            fp.writeToCsv("src/results/resultos2.csv");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
