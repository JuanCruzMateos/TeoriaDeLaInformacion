package modelo.compresion;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BitInputStream extends BitStream {

    public BitInputStream() {
        super(new ArrayList<>());
    }

    /**
     * Escribe los bits en archivo
     *
     * @param filename nombre del archivo
     * @throws IOException en caso de haber algun error en la escritura
     */
    public void read(String filename) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(filename));
        boolean feof = false;

        this.intArray.clear();
        this.lastNumberOfBits = in.readInt();
        while (!feof) {
            try {
                this.intArray.add(in.readInt());
            } catch (IOException e) {
                feof = true;
            }
        }
//        System.out.println(this);
    }

    public int nextBit() {
        if (this.ptrToIntArray == this.intArray.size() - 1 && Integer.SIZE - this.lastNumberOfBits > this.intBitIndex) {
            return -1;
        } else {
            if (this.intBitIndex < 0) {
                this.ptrToIntArray += 1;
                this.intBitIndex = Integer.SIZE - 1;
            }
            int currentAnalizadByte = this.intArray.get(this.ptrToIntArray);
            int bit = (currentAnalizadByte >> this.intBitIndex) & 0x1;
            this.intBitIndex -= 1;
            return bit;
        }
    }

    @Override
    public String toString() {
        return "BitInputStream{\n" +
                "\nintArray=" + intArray +
                ", \ncurrentBits=" + currentBits +
                ", \ncurrentBitsSize=" + currentBitsSize +
                ", \nptrToIntArray=" + ptrToIntArray +
                ", \nintBitIndex=" + intBitIndex +
                ", \nlastNumberOfBits=" + lastNumberOfBits +
                "\n}";
    }
}
