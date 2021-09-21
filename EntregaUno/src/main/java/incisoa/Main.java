package incisoa;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            FileParser fileParser = new FileParser();
            for (int i = 5; i < 10; i += 2) {
                fileParser.parseFile("src/resources/anexo1-grupo5.txt", i);
//            fileParser.printFrecuencies();
//            fileParser.printProbabilidad();
//            fileParser.printCantidadDeInfo();
//            System.out.println("H(S) = " + fileParser.entropia());
                fileParser.writeToTxt("src/results/resultados" + i + "digitos.txt");
                fileParser.writeToCsv("src/results/resultados" + i + "digitos.csv");
                fileParser.clearAll();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
