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
                huffman.parseFile(file, 1);
                huffman.crearArbolHuffman();
                huffman.generarCodigos();
                huffman.writeHuffmanToTxt(file.substring(0, file.lastIndexOf('.')) + "HuffCodes.txt");
                huffman.compress();
                huffman.decompress();
                huffman.clearAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
