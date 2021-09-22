package incisoa;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        FileDecoder fileDecoder = new FileDecoder();
        try {
            for (int i = 5; i < 10; i += 2) {
                fileDecoder.parseFile("src/resources/anexo1-grupo5.txt", i);
//            fileDecoder.printFrecuencies();
//            fileDecoder.printProbabilidad();
//            fileDecoder.printCantidadDeInfo();
//            System.out.println("H(S) = " + fileDecoder.entropia());
                fileDecoder.writeToTxt("src/results/resultados" + i + "digitos.txt");
                fileDecoder.writeToCsv("src/results/resultados" + i + "digitos.csv");
                fileDecoder.clearAll();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
