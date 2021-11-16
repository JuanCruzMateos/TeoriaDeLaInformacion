package modelo.canales;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Canal canal = new Canal();
//        String[] canales = {"canal1.txt", "canal2.txt", "canal3.txt"};

//        canal.readFile("canal1.txt");
//        canal.printProbabilidades("Entropias a priori :: H(A)", canal.getProbA());
//        canal.printMatriz("Matriz del canal :: P(bj/ai)", canal.getMatriz());
//        canal.calcularProbabilidadesDeB();
//        canal.calcularProbabilidadesPosteriori();
//        canal.printMatriz("Posteriori :: P(ai/bj) ", canal.getProbPosteriori());
//        canal.calcularProbabilidadesSimultaneas();
//        canal.printMatriz("Simultaneas :: P(ai,bj)", canal.getProbSucesosSimultaneos());
//        canal.printEntropiasPosteriori();
//        System.out.println("entropia a priori =" + Canal.FMT.format(canal.entropiaPriori()));

        canal.setMatriz(new double[][]{{1. / 2, 0, 1. / 2}, {0, 1., 0}});
        canal.setProbA(new double[]{0.5, 0.5});
        canal.printProbabilidades("Probabiidades de entrada :: P(A)", canal.getProbA());
        canal.printMatriz("Matriz del canal :: P(bj/ai)", canal.getMatriz());
        canal.calcularProbabilidadesDeSalida();
        canal.printProbabilidades("Probabilidades de salida :: P(B)", canal.getProbB());
        canal.calcularProbabilidadesPosteriori();
        canal.printMatriz("Posteriori :: P(ai/bj) ", canal.getProbPosteriori());
        canal.calcularProbabilidadesSimultaneas();
        canal.printMatriz("Simultaneas :: P(ai,bj)", canal.getProbSucesosSimultaneos());
        System.out.println("entropia a priori H(A)=" + Canal.FMT.format(canal.entropiaPriori()));
        canal.calularEntropiasPosteriori();
        canal.printEntropiasPosteriori();
        System.out.println("equivocacion H(A/B)=" + Canal.FMT.format(canal.equivocacion()));
    }
}
