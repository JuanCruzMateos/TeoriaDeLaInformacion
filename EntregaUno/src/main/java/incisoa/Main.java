package incisoa;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        FileDecoder fileDecoder = new FileDecoder();
        try {
            fileDecoder.parseFile("src/resources/anexo1-grupo5.txt", 2);
            fileDecoder.writeToTxt("src/results/resultados2digitos.txt");
            fileDecoder.writeToCsv("src/results/resultados2digitos.csv");
            fileDecoder.clearAll();
            for (int i = 5; i < 10; i += 2) {
                fileDecoder.parseFile("src/resources/anexo1-grupo5.txt", i);
                fileDecoder.writeToTxt("src/results/resultados" + i + "digitos.txt");
                fileDecoder.writeToCsv("src/results/resultados" + i + "digitos.csv");
                fileDecoder.clearAll();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
