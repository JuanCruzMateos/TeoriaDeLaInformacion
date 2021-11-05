package modelo.compresion;

import java.util.ArrayList;

public abstract class BitStream {
    /**
     * Lista donde acumulo los bits que recibo.
     * Una vez que junto 32 bits, lo agrego como un int a la coleccion.
     */
    protected final ArrayList<Byte> byteArray;
    /**
     * Cantidad de bits validas del ultimo int de la lista.
     */
    protected int lastNumberOfBits;
    /**
     * Posicion del bit del int actualmente examinado.
     */
    protected int byteBitIndex;

    public BitStream() {
        this.byteArray = new ArrayList<>();
        this.byteBitIndex = Byte.SIZE - 1;
    }
}
