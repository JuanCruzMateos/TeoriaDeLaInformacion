package prueba;

import modelo.Fuente;
import modelo.Huffman;
import modelo.Markov;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String filename = "src/resources/anexo1-grupo5.txt";

        // 1a
        Fuente fuente = new Fuente();
        try {
            fuente.parseFile(filename, 2);
            fuente.writeToTxt("src/results/resultados2digitos.txt");
            fuente.writeToCsv("src/results/resultados2digitos.csv");
            fuente.clearAll();
            for (int i = 5; i < 10; i += 2) {
                fuente.parseFile(filename, i);
                fuente.writeToTxt("src/results/resultados" + i + "digitos.txt");
                fuente.writeToCsv("src/results/resultados" + i + "digitos.csv");
                fuente.clearAll();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //
        Markov markov = new Markov();
        try {
            markov.readFile(filename);
            markov.calcProbCondicionales();
            markov.calcVectorEstacionario();
            markov.writeToTxt("src/results/markov.txt");
            markov.writeToCsv("src/results/markov.csv");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        //
        Huffman huffman = new Huffman();
        try {
            huffman.parseFile(filename, 2);
            huffman.crearArbol();
            huffman.generarCodigos();
            huffman.writeHuffmanToTxt("src/results/huffman2digitos.txt");
            huffman.writeHuffmanToCsv("src/results/huffman2digitos.csv");
            huffman.newHuffmanFile("src/results/anexo1-grupo5-huffman2.txt");
            huffman.clearAll();
            for (int i = 5; i < 10; i += 2) {
                huffman.parseFile(filename, i);
                huffman.crearArbol();
                huffman.generarCodigos();
//                huffman.printCodes();
//                System.out.println(huffman.getHuffcodes().keySet().size());
                huffman.writeHuffmanToTxt("src/results/huffman" + i + "digitos.txt");
                huffman.writeHuffmanToCsv("src/results/huffman" + i + "digitos.csv");
                huffman.newHuffmanFile("src/results/anexo1-grupo5-huffman" + i + ".txt");
                huffman.clearAll();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
