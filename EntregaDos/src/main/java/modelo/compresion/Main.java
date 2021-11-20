package modelo.compresion;

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            for (int i = 0; i < files.length - 1; i++) {
                rlc.compressTxt(files[i], true);
                rlc.decompressTxt(files[i].substring(0, files[i].lastIndexOf('.')) + ".rlc");
                rlc.compressInfoTxt();
            }
            rlc.compressRaw("imagen.raw");
            rlc.decompressRaw("imagen.rlc");
            rlc.compressInfoRaw();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}