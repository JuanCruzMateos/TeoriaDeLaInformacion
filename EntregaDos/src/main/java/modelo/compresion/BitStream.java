package modelo.compresion;

import java.util.ArrayList;

public abstract class BitStream {
    /**
     * Lista donde acumulo los bits que recibo.
     * Una vez que junto 8 bits, lo agrego como un byte a la coleccion.
     */
    protected final ArrayList<Byte> byteArray;
    /**
     * Cantidad de bits validas del ultimo byte de la lista.
     */
    protected int lastNumberOfBits;
    /**
     * Posicion del bit del byte actualmente examinado.
     */
    protected int byteBitIndex;

    public BitStream() {
        this.byteArray = new ArrayList<>();
        this.byteBitIndex = Byte.SIZE - 1;
    }
}
