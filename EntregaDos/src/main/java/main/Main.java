package main;

import modelo.compresion.Huffman;
import modelo.compresion.RLC;
import modelo.compresion.ShannonFano;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String[] files = {"Argentina.txt", "Hawai.txt", "imagen.raw"};
        Huffman huffman = new Huffman();
        ShannonFano shannonFano = new ShannonFano();
        RLC rlc = new RLC();

        for (String file : files) {
            try {
                huffman.parseFile(file);
                huffman.generarArbolCodificacion();
                huffman.generarCodigos();
                huffman.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "HuffCodes.txt");
                huffman.compress();
                huffman.decompress();
                huffman.clearAll();

                shannonFano.parseFile(file);
                shannonFano.generarArbolCodificacion();
                shannonFano.generarCodigos();
                shannonFano.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "ShannonCodes.txt");
                shannonFano.compress();
                shannonFano.decompress();
                shannonFano.clearAll();

                rlc.encode(file, true, file.equals("imagen.raw"));
                rlc.decode(file.substring(0, file.lastIndexOf('.')) + ".rlc");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        try {
//            String file = "test.txt";
//
//            huffman.parseFile(file);
//            huffman.generarArbolCodificacion();
//            huffman.generarCodigos();
//            huffman.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "HuffCodes.txt");
//            huffman.compress();
//            huffman.decompress();
//            huffman.clearAll();
//
//            shannonFano.parseFile(file);
//            shannonFano.generarArbolCodificacion();
//            shannonFano.generarCodigos();
//            shannonFano.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "ShannCodes.txt");
//            shannonFano.compress();
//            shannonFano.decompress();
//            shannonFano.clearAll();
//
//            rlc.encode(file, true, false);
//            rlc.decode(file.substring(0, file.lastIndexOf('.')) + ".rlc");
//        } catch (IOException e) {
//            System.out.println("Error");
//        }
    }
}