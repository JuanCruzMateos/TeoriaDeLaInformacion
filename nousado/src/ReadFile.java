
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
// import java.util.Arrays;
import java.util.HashMap;
// import java.util.Scanner;
import java.io.Reader;

public class ReadFile {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filename = "../datos/anexo1-grupo5.txt";
        HashMap<String, Integer> frec = new HashMap<>();
        int digitos = 2;

        // // java.util.InputMismatchException :(
        // Scanner scan = new Scanner(new BufferedReader(new FileReader(filename)));
        // String regex;
        // regex = "(\\d{" + String.valueOf(digitos) + "})";
        // while (scan.hasNext()) {
        // String word = scan.next(regex);
        // System.out.println(word);
        // frec.put(word, frec.getOrDefault(word, 0) + 1);
        // }
        // scan.close();

        // :)
        // FileInputStream file = new FileInputStream(new File(filename));
        // byte[] buffer = new byte[digitos];
        // while (file.read(buffer) != -1) {
        // String word = new String(buffer);
        // frec.put(word, frec.getOrDefault(word, 0) + 1);
        // }
        // file.close();

        Reader reader = new FileReader(filename);
        char[] buffer = new char[digitos];
        while (reader.read(buffer) != -1) {
            String word = new String(buffer);
            frec.put(word, frec.getOrDefault(word, 0) + 1);
        }
        reader.close();

        System.out.println(frec.size());
        for (String key : frec.keySet()) {
            System.out.println(key + ": " + frec.get(key));
        }

    }

}