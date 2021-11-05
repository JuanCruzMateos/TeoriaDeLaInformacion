package modelo.compresion;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream extends BitStream {
    /**
     * Agrega los bits represantados por la cadena bits.
     *
     * @param bits cadena de bits.
     */
    public void addBits(String bits) {
        int i = 0;
        while (i < bits.length()) {
            while (i < bits.length() && this.currentBitsSize < Integer.SIZE) {
                this.currentBits = (this.currentBits << 1) | bits.charAt(i) & 0x1;
                this.currentBitsSize += 1;
                i += 1;
            }
            if (this.currentBitsSize == Integer.SIZE) {
                this.intArray.add(this.currentBits);
                this.currentBits = 0;
                this.currentBitsSize = 0;
            }
        }
    }

    /**
     * Finaliza el stream completando con 1 los bits del ultimo byte.
     */
    private void endStream() {
        this.lastNumberOfBits = this.currentBitsSize;
        int faltantes = Integer.SIZE - this.currentBitsSize;
        String f = new String(new char[faltantes]).replace('\0', '1');
        this.addBits(f);
    }

    /**
     * Escribe los bits en archivo
     *
     * @param filename nombre del archivo
     * @throws IOException en caso de haber algun error en la escritura
     */
    public void write(String filename) throws IOException {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));

        this.endStream();
        out.writeInt(this.lastNumberOfBits);
        for (Integer num : this.intArray) {
            out.writeInt(num);
        }
//        System.out.println(this);
        out.close();
    }

    @Override
    public String toString() {
        return "BitOutputStream{\n" +
                "\nintArray=" + intArray +
                ", \ncurrentBits=" + currentBits +
                ", \ncurrentBitsSize=" + currentBitsSize +
                ", \nptrToIntArray=" + ptrToIntArray +
                ", \nintBitIndex=" + intBitIndex +
                ", \nlastNumberOfBits=" + lastNumberOfBits +
                "\n}";
    }
}
