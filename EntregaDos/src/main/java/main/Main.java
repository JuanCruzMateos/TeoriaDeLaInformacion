package main;

import modelo.compresion.RLC;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        String[] files = {"Argentina.txt", "Hawai.txt", "imagen.raw"};
//        Huffman huffman = new Huffman();
//        ShannonFano shannonFano = new ShannonFano();
//        RLC rlc = new RLC();
//
//        for (String file : files) {
//            try {
//                huffman.parseFile(file);
//                huffman.generarArbolCodificacion();
//                huffman.generarCodigos();
//                huffman.compress();
//                huffman.decompress();
//                huffman.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "HuffCodes.txt");
//                huffman.clearAll();
//
//                shannonFano.parseFile(file);
//                shannonFano.generarArbolCodificacion();
//                shannonFano.generarCodigos();
//                shannonFano.compress();
//                shannonFano.decompress();
//                shannonFano.writeToTxt(file.substring(0, file.lastIndexOf('.')) + "ShannonCodes.txt");
//                shannonFano.clearAll();
//
//                rlc.encodeTxt(file, true);
//                rlc.decodeTxt(file.substring(0, file.lastIndexOf('.')) + ".rlc");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        String[] files = {"Argentina.txt", "Hawai.txt"};
        RLC rlc = new RLC();
        try {
            for (String file : files) {
                String compressed = file.substring(0, file.lastIndexOf('.')) + ".rlc";
                rlc.compressTxt(file, true);
                rlc.decompressTxt(compressed);
                System.out.println(rlc.getTasaDeCompresion(file, compressed));
            }

            rlc.compressRaw("imagen.raw");
            rlc.decompressRaw("imagen.rlc");
            System.out.println(rlc.getTasaDeCompresion("imagen.raw", "imagen.rlc"));
        } catch (IOException e) {
            e.printStackTrace();
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