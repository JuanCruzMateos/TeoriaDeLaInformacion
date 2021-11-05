package main;

import modelo.compresion.Huffman;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String file1 = "Argentina.txt";
        String file2 = "Hawai.txt";
//        String file3 = "imagen.raw";

        Huffman huffman = new Huffman();
        try {
            huffman.parseFile(file1, 1);
            huffman.crearArbolHuffman();
            huffman.generarCodigos();
            huffman.writeHuffmanToTxt("ArgentinaHuf.txt");
            huffman.compress();
            huffman.decompress();
            huffman.clearAll();

//            huffman.parseFile(file2, 1);
//            huffman.crearArbolHuffman();
//            huffman.generarCodigos();
//            huffman.writeHuffmanToTxt("HawaiHuf.txt");
//            huffman.compress();
//            huffman.decompress();
//            huffman.clearAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
