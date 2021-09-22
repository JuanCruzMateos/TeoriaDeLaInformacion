package incisoa;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class Markov {

    public static void main(String[] args) throws IOException {
        Markov markov = new Markov();

        markov.probCondicionales("src/resources/anexo1-grupo5.txt");
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

    public void probCondicionales(String inputfile) throws IOException {
        double[][] probs = new double[4][4];
        Reader reader = new FileReader(inputfile);
        char[] buffer = new char[2];
        int ant, act;

        if (reader.read(buffer) == -1) {
            throw new IOException("Eof");
        }
        ant = this.toDec(Integer.parseInt(new String(buffer)));
        while (reader.read(buffer) != -1) {
            act = this.toDec(Integer.parseInt(new String(buffer)));
            probs[act][ant] += 1;
            ant = act;
        }
        reader.close();

        this.printMat(probs);
        for (int j = 0; j < 4; j++) {
            int finalJ = j;
            double sum = Arrays.stream(probs).mapToDouble(row -> row[finalJ]).sum();
//            System.out.println(finalJ + " " + sum);
            for (int i = 0; i < 4; i++) {
                probs[i][j] /= sum;
            }
        }
        this.printMat(probs);
    }

    private void printMat(double[][] mat) {
        for (int i = 0; i < 4; i++) {
            System.out.print('|');
            for (int j = 0; j < 4; j++) {
                System.out.printf("%10.2f", mat[i][j]);
            }
            System.out.println("|");
        }
    }

    private boolean warshall(double[][] probs) {
        int[][] a = new int[4][4];
        int i, j, k;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                if (i == j)
                    a[i][j] = 0;
                else if (probs[i][j] == 0)
                    a[i][j] = Integer.MAX_VALUE;
                else
                    a[i][j] = 1;
            }
        }

        for (k = 0; k < 4; k++) {
            for (i = 0; i < 4; i++) {
                for (j = 0; j < 4; j++) {
                    if (a[i][k] + a[k][j] != 0 && a[i][k] + a[k][j] != Integer.MAX_VALUE) {
                        a[i][j] = 1;
                    }
                }
            }
        }
        return true;
    }

}
