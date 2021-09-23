package incisoa;

import java.io.*;
import java.util.Arrays;

public class Markov {
    private static final int INF = 999;
    private static final int DIGITOS = 2;
    private static final int ESTADOS = 4;
    private final double[][] probCond = new double[ESTADOS][ESTADOS];
    private final double[] vecEstacionario = new double[ESTADOS];

    public Markov() {
        this.vecEstacionario[0] = 1;
        for (int i = 1; i < ESTADOS; i++) {
            this.vecEstacionario[i] = 0;
        }
    }

    public static void main(String[] args) {
        Markov markov = new Markov();

        try {
            markov.readFile("src/resources/anexo1-grupo5.txt");
            markov.calcProbCondicionales();
            markov.calcVectorEstacionario();
            markov.writeTxt("src/results/markov.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void readFile(String inputfile) throws IOException {
        Reader reader = new FileReader(inputfile);
        char[] buffer = new char[DIGITOS];
        int ant, act;

        if (reader.read(buffer) == -1) {
            throw new IOException("Eof");
        }
        ant = this.toDec(Integer.parseInt(new String(buffer)));
        while (reader.read(buffer) != -1) {
            act = this.toDec(Integer.parseInt(new String(buffer)));
            this.probCond[act][ant] += 1;
            ant = act;
        }
        reader.close();
    }

    public void calcProbCondicionales() {
        for (int j = 0; j < ESTADOS; j++) {
            int finalJ = j;
            double sum = Arrays.stream(this.probCond).mapToDouble(row -> row[finalJ]).sum();
            for (int i = 0; i < 4; i++) {
                this.probCond[i][j] /= sum;
            }
        }
    }

    public void calcVectorEstacionario() {
        double[] iter = new double[ESTADOS];
        double[] aux = new double[ESTADOS];
        double tol = 10E-7;
        double err = 1;
        double acum;

        while (err > tol) {
            for (int i = 0; i < ESTADOS; i++) {
                acum = 0;
                for (int j = 0; j < ESTADOS; j++)
                    acum += this.probCond[i][j] * this.vecEstacionario[j];
                iter[i] = acum;
            }
            for (int i = 0; i < ESTADOS; i++) {
                aux[i] = iter[i] - this.vecEstacionario[i];
                this.vecEstacionario[i] = iter[i];
            }
            err = this.normaM(aux);
        }
    }

    private double normaM(double[] vec) {
        double[] p = Arrays.stream(vec).map(i -> i > 0 ? i : i * -1).toArray();
        return Arrays.stream(p).max().getAsDouble();
    }

    private int toDec(int bin) {
        int dec = 0, i = 1;

        do {
            dec += (bin % 10) * i;
            i *= 2;
            bin = bin / 10;
        } while (bin != 0);
        return dec;
    }

    public void printProbCond() {
        System.out.println("Probabilidades condicionales: ");
        for (int i = 0; i < ESTADOS; i++) {
            System.out.print('|');
            for (int j = 0; j < ESTADOS; j++) {
                System.out.printf("%10.6f", this.probCond[i][j]);
            }
            System.out.println("|");
        }
    }

    public void printVectorEstacionario() {
        System.out.println("Vector estacionario: ");
        System.out.println(Arrays.toString(this.vecEstacionario));
    }

    public void writeTxt(String filename) throws FileNotFoundException {
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(filename));
        this.printProbCond();
        System.out.println();
        this.printVectorEstacionario();
        System.setOut(stdout);
    }

    // TODO:
    private boolean warshall(double[][] probs) {
        int[][] a = new int[4][4];
        int i, j, k;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                if (i == j)
                    a[i][j] = 0;
                else if (probs[i][j] == 0)
                    a[i][j] = INF;
                else
                    a[i][j] = 1;
            }
        }

        for (k = 0; k < 4; k++) {
            for (i = 0; i < 4; i++) {
                for (j = 0; j < 4; j++) {
                    if (a[i][k] + a[k][j] != 0 && a[i][k] + a[k][j] < INF) {
                        a[i][j] = 1;
                    }
                }
            }
        }
        return false;
    }

    public double entropia() {
        return 0;
    }

}
