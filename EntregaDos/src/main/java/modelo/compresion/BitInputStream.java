package modelo.compresion;

import java.io.FileInputStream;
import java.io.IOException;

public class BitInputStream extends BitStream {
    /**
     * Indice del int actualmente examinado.
     */
    protected int ptrToByteArray;

    /**
     * Escribe los bits en archivo
     *
     * @param filename nombre del archivo
     * @throws IOException en caso de haber algun error en la escritura
     */
    public void read(String filename) throws IOException {
//        DataInputStream in = new DataInputStream(new FileInputStream(filename));
//        boolean feof = false;
        FileInputStream in = new FileInputStream(filename);
        int c;

        this.lastNumberOfBits = in.read();
        while ((c = in.read()) != -1) {
            this.byteArray.add((byte) c);
        }
        in.close();
    }

    public int nextBit() {
        if (this.ptrToByteArray == this.byteArray.size() - 1 && Byte.SIZE - this.lastNumberOfBits > this.byteBitIndex) {
            return -1;
        } else {
            if (this.byteBitIndex < 0) {
                this.ptrToByteArray += 1;
                this.byteBitIndex = Byte.SIZE - 1;
            }
            int currentAnalizadByte = this.byteArray.get(this.ptrToByteArray);
            int bit = (currentAnalizadByte >> this.byteBitIndex) & 0x1;
            this.byteBitIndex -= 1;
            return bit;
        }
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
