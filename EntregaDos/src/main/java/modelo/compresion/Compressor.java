package modelo.compresion;

import java.io.IOException;

public interface Compressor {
    void generarArbolCodificacion();

    void generarCodigos();

    void writeToTxt(String filename) throws IOException;

    void writeToCsv(String filename) throws IOException;

    void compress() throws IOException;

    void decompress() throws IOException;
}
