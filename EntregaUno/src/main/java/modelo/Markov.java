package modelo;

import java.io.*;
import java.util.Arrays;

/**
 * @author Noelia Echeverria
 * @author Camila Ezama
 * @author Juan Cruz Mateos
 */

public class Markov {
    private static final double TOL = 10E-7;
    private static final int INF = 999;
    private static final int DIGITOS = 2;
    private static final int ESTADOS = 4;
    private final double[][] probCond = new double[Markov.ESTADOS][Markov.ESTADOS];
    private final double[] vecEstacionario = new double[Markov.ESTADOS];

    public void readFile(String inputfile) throws IOException {
        Reader reader = new FileReader(inputfile);
        char[] buffer = new char[Markov.DIGITOS];
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
        for (int j = 0; j < Markov.ESTADOS; j++) {
            int finalJ = j;
            double sum = Arrays.stream(this.probCond).mapToDouble(row -> row[finalJ]).sum();
            for (int i = 0; i < 4; i++) {
                this.probCond[i][j] /= sum;
            }
        }
    }

    public void calcVectorEstacionario() {
        double[] iter = new double[Markov.ESTADOS];
        double err = Markov.TOL;

        // inicializacion
        this.vecEstacionario[0] = 1;
        for (int i = 1; i < ESTADOS; i++) {
            this.vecEstacionario[i] = 0;
        }
        // calculo iterativo
        while (err >= Markov.TOL) {
            for (int i = 0; i < Markov.ESTADOS; i++) {
                iter[i] = 0;
                for (int j = 0; j < Markov.ESTADOS; j++)
                    iter[i] += this.probCond[i][j] * this.vecEstacionario[j];
            }
            err = normaM(iter, this.vecEstacionario);
            System.arraycopy(iter, 0, this.vecEstacionario, 0, Markov.ESTADOS);
        }
    }

    public double normaM(double[] act, double[] ant) {
        double max, diff;

        max = Math.abs(act[0] - ant[0]);
        for (int i = 1; i < act.length; i++) {
            diff = Math.abs(act[i] - ant[i]);
            if (diff > max)
                max = diff;
        }
        return max;
    }

    public double[] vectorErroresEquip() {
        double[] err = new double[Markov.ESTADOS];

        for (int i = 0; i < Markov.ESTADOS; i++) {
            err[i] = Math.abs(0.25 - this.vecEstacionario[i]);
        }
        return err;
    }

    public double entropia(double[][] prob, double[] vec) {
        double h = 0, aux;

        for (int i = 0; i < vec.length; i++) {
            aux = 0;
            for (int j = 0; j < vec.length; j++) {
                if (prob[j][i] != 0)
                    aux += prob[j][i] * -1. * Math.log(prob[j][i]) / Math.log(2);
            }
            h += aux * vec[i];
        }
        return h;
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
        for (int i = 0; i < Markov.ESTADOS; i++) {
            System.out.print('|');
            for (int j = 0; j < Markov.ESTADOS; j++) {
                System.out.printf("%10.6f", this.probCond[i][j]);
            }
            System.out.println("|");
        }
    }

    public void printVectorEstacionario() {
        System.out.println("Vector estacionario: ");
        System.out.println(Arrays.toString(this.vecEstacionario));
    }

    public void writeToTxt(String filename) throws FileNotFoundException {
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(filename));

        this.printProbCond();
        System.out.println();
        this.printVectorEstacionario();
        System.out.println();
        System.out.println("Errores al suponiendo fuente de memoria nula:");
        System.out.println(Arrays.toString(this.vectorErroresEquip()));
        System.out.println("\nEntropia:\nH(S) = " + this.entropia(this.probCond, this.vecEstacionario) + " bits");
        System.setOut(stdout);
    }


    public void writeToCsv(String outputfile) throws IOException {
        Writer file = new FileWriter(outputfile);
        StringBuilder sb;

        file.write("Matriz de transicion:\n");
        for (int i = 0; i < Markov.ESTADOS; i++) {
            sb = new StringBuilder();
            sb.append(',');
            for (int j = 0; j < Markov.ESTADOS; j++) {
                sb.append(this.probCond[i][j]).append(',');
            }
            file.write(sb.append("\n").toString());
        }
        file.write("\n");
        file.write("Vector estacionario:\n");
        sb = new StringBuilder();
        sb.append(",");
        for (int i = 0; i < Markov.ESTADOS; i++) {
            sb.append(this.vecEstacionario[i]).append(',');
        }
        file.write(sb.append("\n").toString());
        file.write("\n");
        file.write(String.join(", ", "Entropia:", Double.toString(this.entropia(this.probCond, this.vecEstacionario))));
        file.close();
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
        return true;
    }
}
