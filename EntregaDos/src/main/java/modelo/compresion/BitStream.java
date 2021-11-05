package modelo.compresion;

import java.util.ArrayList;

public abstract class BitStream {
    protected final ArrayList<Integer> intArray;
    protected int currentBits;
    protected int currentBitsSize;
    protected int ptrToIntArray;
    protected int intBitIndex;
    protected int lastNumberOfBits;

    public BitStream() {
        this.intArray = new ArrayList<>();
        this.intBitIndex = Integer.SIZE - 1;
    }
}
