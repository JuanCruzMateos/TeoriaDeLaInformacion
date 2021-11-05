package modelo.compresion;

import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream extends BitStream {
    /**
     * Variable donde acumulo los bits que recibo.
     */
    protected byte currentBits;
    /**
     * Cantidad de bits acumulados en currentBits.
     */
    protected byte currentBitsSize;

    /**
     * Agrega los bits represantados por la cadena bits.
     *
     * @param bits cadena de bits.
     */
    public void addBits(String bits) {
        int i = 0;
        while (i < bits.length()) {
            while (i < bits.length() && this.currentBitsSize < Byte.SIZE) {
                this.currentBits = (byte) ((this.currentBits << 1) | bits.charAt(i) & 0x1);
                this.currentBitsSize += 1;
                i += 1;
            }
            if (this.currentBitsSize == Byte.SIZE) {
                this.byteArray.add(this.currentBits);
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
        int faltantes = Byte.SIZE - this.currentBitsSize;
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
//        DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));  out.writeInt();
        FileOutputStream out = new FileOutputStream(filename);

        this.endStream();
        out.write(this.lastNumberOfBits);
        for (Byte num : this.byteArray) {
            out.write(num);
        }
        out.close();
    }

    @Override
    public String toString() {
        return "BitOutputStream{" +
                "\ncurrentBits=" + currentBits +
                ", \ncurrentBitsSize=" + currentBitsSize +
                ", \nbyteArray=" + byteArray +
                ", \nlastNumberOfBits=" + lastNumberOfBits +
                ", \nbyteBitIndex=" + byteBitIndex +
                "\n}";
    }
}
