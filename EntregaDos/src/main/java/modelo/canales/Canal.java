package modelo.canales;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Dados los tres canales de comunicación que se detallan a continuación, calcular la Equivocación,
 * Información Mutua y las propiedades de cada canal. Mostrar, comparar los resultados obtenidos.
 * Obtener conclusiones.
 */
public class Canal {
    public static final String RESOURCESPATH = "src" + File.separator + "resources" + File.separator;
    public static final String RESULTSPATH = "src" + File.separator + "results" + File.separator + "canales" + File.separator;
    // P(ai)
    protected double[] probA;
    // P(bi)
    protected double[] probB;
    // Pij = P(bj/ai)
    protected double[][] matriz;

    public static void main(String[] args) throws IOException {
        Canal canal = new Canal();
        String[] canales = {"canal1.txt", "canal2.txt", "canal3.txt"};

        for (String c : canales) {
            canal.readFile(c);
            canal.printProbabilidadesDeA();
            canal.printMat();
        }
    }

    public void readFile(String filename) throws IOException {
        Scanner scanner = new Scanner(new FileInputStream(RESOURCESPATH + filename));
        int r, c;

        r = scanner.nextInt();
        c = scanner.nextInt();
        this.probA = new double[r];
        this.matriz = new double[r][c];
        for (int i = 0; i < r; i++) {
            this.probA[i] = scanner.nextDouble();
        }
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                this.matriz[i][j] = scanner.nextDouble();
            }
        }
        scanner.close();
    }

    public void calcularProbB() {
        this.probB = new double[this.matriz[0].length];

        for (int i = 0; i < this.matriz.length; i++) {
//        this.probB[i] += this.probA[i]
        }
    }

    public void printProbabilidadesDeA() {
        System.out.println("Probabilidades alfabeto de entrada A");
        System.out.println(Arrays.toString(this.probA));
    }

    public void printMat() {
        System.out.println("Matriz del canal");
        for (double[] doubles : matriz) {
            System.out.println(Arrays.toString(doubles));
        }
    }
}
