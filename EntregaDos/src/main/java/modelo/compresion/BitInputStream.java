package modelo.compresion;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */
public class BitInputStream extends BitStream {
    /**
     * Indice del byte actualmente examinado por nextBit().
     */
    protected int ptrToByteArray;

    /**
     * Crea un stream de bits leyendolos del archivo pasado por parametro.
     *
     * @param filename nombre del archivo
     * @throws IOException en caso de haber algun error en la escritura
     */
    public void read(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        int c;

        this.lastNumberOfBits = in.read();
        while ((c = in.read()) != -1) {
            this.byteArray.add((byte) c);
        }
        in.close();
    }

    /**
     * Devuelve el proximo bit presente en el stream o -1 en caso de haber finalizado.
     *
     * @return proximo bit 0 || 1, o -1 en caso de haber finalizado el stream
     */
    public int nextBit() {
        int currentAnalizadByte, bit;

        if (this.endOfStream()) {
            return -1;
        } else {
            if (this.byteBitIndex < 0) {
                this.ptrToByteArray += 1;
                this.byteBitIndex = Byte.SIZE - 1;
            }
            currentAnalizadByte = this.byteArray.get(this.ptrToByteArray);
            bit = (currentAnalizadByte >> this.byteBitIndex) & 0x1;
            this.byteBitIndex -= 1;
            return bit;
        }
    }

    /**
     * @return true si estoy en la ultima entrada de la lista y ya lei el ultimo bit valido.
     */
    private boolean endOfStream() {
        return this.ptrToByteArray == this.byteArray.size() - 1 && Byte.SIZE - this.lastNumberOfBits > this.byteBitIndex;
    }

    @Override
    public String toString() {
        return "BitInputStream{" +
                "\nptrToByteArray=" + ptrToByteArray +
                ", \nbyteArray=" + byteArray +
                ", \nlastNumberOfBits=" + lastNumberOfBits +
                ", \nbyteBitIndex=" + byteBitIndex +
                "\n}";
    }
}