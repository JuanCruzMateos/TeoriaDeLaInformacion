package modelo.compresion;

import java.util.ArrayList;

public class NodoShannonFano {
    private final ArrayList<Nodo> nodos;
    public NodoShannonFano izq;
    public NodoShannonFano der;

    public NodoShannonFano(ArrayList<Nodo> nodos, NodoShannonFano izq, NodoShannonFano der) {
        this.nodos = nodos;
        this.izq = izq;
        this.der = der;
    }

    public boolean isHoja() {
        return this.izq == null && this.der == null;
    }

    private int getSplitIndex() {
        double sumaTotalProb = this.nodos.stream().mapToDouble(Nodo::getProb).sum();
        double acum = 0;
        int i = 0;
        Nodo nodo;

        nodo = this.nodos.get(i);
        while (acum + nodo.prob <= sumaTotalProb / 2.0) {
            acum += nodo.prob;
            nodo = this.nodos.get(++i);
        }
        return i;
    }

    public ArrayList<Nodo> getMitadDer() {
        ArrayList<Nodo> der = new ArrayList<>();

        for (int j = this.getSplitIndex(); j < this.nodos.size(); j++) {
            der.add(new Nodo(this.nodos.get(j).simb, this.nodos.get(j).prob, null, null));
        }
        return der;
    }

    public ArrayList<Nodo> getMitadIzq() {
        ArrayList<Nodo> izq = new ArrayList<>();

        for (int j = 0; j < this.getSplitIndex(); j++) {
            izq.add(new Nodo(this.nodos.get(j).simb, this.nodos.get(j).prob, null, null));
        }
        return izq;
    }

    public int getCantNodosInternos() {
        return this.nodos.size();
    }

    public String getSimbolo() {
        return this.nodos.get(0).simb;
    }

    @Override
    public String toString() {
        return "NodoShannonFano{" + nodos + '}';
    }
}