package modelo.compresion;

import java.io.IOException;
import java.io.InputStream;

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
     * Posicion del bit del byte actualmente examinado.
     */
    protected int byteBitIndex;

    public BitInputStream() {
        this.byteBitIndex = Byte.SIZE - 1;
    }

    /**
     * Crea un stream de bits leyendolos del archivo pasado por parametro.
     *
     * @param input nombre del archivo
     * @throws IOException en caso de haber algun error en la escritura
     */
    public void readFrom(InputStream input) throws IOException {
        int c;
        this.lastValidBit = input.read();
        while ((c = input.read()) != -1) {
            this.byteArray.add((byte) c);
        }
        input.close();
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
        return this.ptrToByteArray == this.byteArray.size() - 1 && this.lastValidBit > this.byteBitIndex;
    }

    @Override
    public String toString() {
        return "BitInputStream{" +
                "\nptrToByteArray=" + ptrToByteArray +
                ", \nbyteArray=" + byteArray +
                ", \nlastNumberOfBits=" + lastValidBit +
                ", \nbyteBitIndex=" + byteBitIndex +
                "\n}";
    }
}