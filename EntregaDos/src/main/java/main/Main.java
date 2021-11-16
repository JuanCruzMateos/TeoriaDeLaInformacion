package main;

import modelo.compresion.Huffman;
import modelo.compresion.RLC;
import modelo.compresion.ShannonFano;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String[] files = {"Argentina.txt", "Hawai.txt"};
        Huffman huffman = new Huffman();
        ShannonFano shannonFano = new ShannonFano();
        RLC rlc = new RLC();

        for (String file : files) {
            try {
                huffman.parseFile(file);
                huffman.generarArbolCodificacion();
                huffman.generarCodigos();
                huffman.compress();
                huffman.decompress();
                huffman.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "HuffCodes.txt");
                huffman.clearAll();

                shannonFano.parseFile(file);
                shannonFano.generarArbolCodificacion();
                shannonFano.generarCodigos();
                shannonFano.compress();
                shannonFano.decompress();
                shannonFano.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "ShannonCodes.txt");
                shannonFano.clearAll();

                rlc.compressTxt(file, true);
                rlc.decompressTxt(file.substring(0, file.lastIndexOf('.')) + ".rlc");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            rlc.compressRaw("imagen.raw");
            rlc.decompressRaw("imagen.rlc");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}