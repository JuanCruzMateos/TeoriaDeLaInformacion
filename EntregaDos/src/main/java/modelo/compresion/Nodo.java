package modelo.compresion;

public class Nodo implements Comparable<Nodo> {
    public final String simb;
    public final double prob;
    public final Nodo izq;
    public final Nodo der;

    public Nodo(String simb, double prob, Nodo izq, Nodo der) {
        this.simb = simb;
        this.prob = prob;
        this.izq = izq;
        this.der = der;
    }

    public boolean isHoja() {
        return this.izq == null && this.der == null;
    }

    @Override
    public int compareTo(Nodo o) {
        return Double.compare(this.prob, o.prob);
    }

    @Override
    public String toString() {
        return "{ simb=" + this.simb + ", prob=" + this.prob + " }";
    }

    public double getProb() {
        return prob;
    }
}