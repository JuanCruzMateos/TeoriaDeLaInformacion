package modelo.compresion;

import java.util.ArrayList;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */
public abstract class BitStream {
    /**
     * Lista donde acumulo los bits que recibo.
     * Una vez que junto 8 bits, lo agrego como un byte a la coleccion.
     */
    protected final ArrayList<Byte> byteArray;
    /**
     * Indice del ultimo bit valido del ultimo byte.
     */
    protected int lastValidBit;

    public BitStream() {
        this.byteArray = new ArrayList<>();
    }
}