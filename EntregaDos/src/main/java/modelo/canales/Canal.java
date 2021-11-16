package modelo.canales;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Equivocación,
 * Información Mutua y
 * las propiedades de cada canal.
 * Mostrar, comparar los resultados obtenidos.
 * Obtener conclusiones.
 */
public class Canal {
    public static final String RESOURCESPATH = "src" + File.separator + "resources" + File.separator;
    public static final String RESULTSPATH = "src" + File.separator + "results" + File.separator + "canales" + File.separator;
    public static final NumberFormat FMT = new DecimalFormat("0.0000");
    private double[] probA; // priori
    private double[] probB;
    private double[][] matriz; // Pij = P(bj/ai)
    private double[][] probPosteriori; // Pij = P(ai/bj) -> posteriori
    private double[][] probSucesosSimultaneos; // Pij = P(ai,bj)
    private double[] entropiasPosteriori; // H(A/bj)

    public static double log2(double number) {
        return number == 0 ? 0 : Math.log(number) / Math.log(2);
    }

    public static double entropia(double[] prob) {
        return Arrays.stream(prob).reduce(0, (partialH, p) -> partialH + (p * log2(1.0 / p)));
    }

    public void readFile(String filename) throws IOException {
        Scanner scanner = new Scanner(new FileInputStream(RESOURCESPATH + filename));
        int r, s;

        r = scanner.nextInt();
        s = scanner.nextInt();
        this.probA = new double[r];
        this.matriz = new double[r][s];
        for (int i = 0; i < r; i++) {
            this.probA[i] = scanner.nextDouble();
        }
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < s; j++) {
                this.matriz[i][j] = scanner.nextDouble();
            }
        }
        scanner.close();
    }

    public void calcularProbabilidadesDeSalida() {
        this.probB = new double[this.matriz[0].length]; // en 0 por defecto

        for (int j = 0; j < this.probB.length; j++) {
            for (int i = 0; i < this.matriz.length; i++) {
                this.probB[j] += this.probA[i] * this.matriz[i][j];
            }
        }
    }

    public void calcularProbabilidadesPosteriori() {
        this.probPosteriori = new double[this.matriz[0].length][this.matriz.length]; // s x r

        for (int i = 0; i < this.probPosteriori.length; i++) {
            for (int j = 0; j < this.probPosteriori[0].length; j++) {
                this.probPosteriori[i][j] = this.matriz[j][i] * this.probA[j] / this.probB[i];
            }
        }
    }

    public void calcularProbabilidadesSimultaneas() {
        this.probSucesosSimultaneos = new double[this.matriz.length][this.matriz[0].length];
        for (int i = 0; i < this.probSucesosSimultaneos.length; i++) {
            for (int j = 0; j < this.probSucesosSimultaneos[0].length; j++) {
                this.probSucesosSimultaneos[i][j] = this.matriz[i][j] * this.probA[i];
            }
        }
    }

    public double entropiaPriori() {
        return entropia(this.probA);
    }

    public void calularEntropiasPosteriori() {
        this.entropiasPosteriori = new double[this.matriz[0].length];
        for (int i = 0; i < this.matriz[0].length; i++) {
            this.entropiasPosteriori[i] = entropia(this.probPosteriori[i]);
        }
    }

    /**
     * H(A/B)
     * >>> Entropia media a posteriori
     * >>> Equivocación de A conrespecto a B a través del canal
     * >>> Ruido
     * – Mide la información que queda en A después de observar B.
     * – Pérdida de información sobre A causada por el canal.
     * – Cantidad de información sobre A que no deja pasar el canal.
     *
     * @return equivocacion
     */
    public double equivocacion() {
        double h = 0;
        for (int i = 0; i < this.probB.length; i++) {
            h += this.probB[i] * this.entropiasPosteriori[i];
        }
        return h;
    }

    public double perdida() {
        return -1.0 * (this.informacionMutua());
    }

    /**
     * I(A,B)= H(A)-H(A/B)
     * -  Es la cantidad de información sobre A, menos la cantidad de
     * información que todavía hay en A después de observar la salida.
     * – Es la cantidad de información que se obtiene de A gracias al conocimiento de B.
     * – Es la incertidumbre sobre la entrada del canal que se resuelve observando la salida del canal.
     * – Es la cantidad de información sobre A que atraviesa el canal
     *
     * @return informacion mutua
     */
    public double informacionMutua() {
        return 0;
    }

    /**
     * H(A,B)=H(B)+H(A/B) <<
     * H(A,B)=H(A)+H(B/A)
     *
     * @return entropia afin
     */
    public double entropiaAfin() {
        return entropia(this.probB) + this.equivocacion();
    }

    public void printEntropiasPosteriori() {
        System.out.println("Entropias a posteriori");
        for (int i = 0; i < this.entropiasPosteriori.length; i++) {
            System.out.println("H(A/b" + (i + 1) + ") = " + FMT.format(this.entropiasPosteriori[i]));
        }
        System.out.println();
    }

    public void printProbabilidades(String title, double[] probs) {
        System.out.println(title);
        System.out.println(Arrays.toString(Arrays.stream(probs).mapToObj(FMT::format).toArray()) + "\n");
    }

    public void printMatriz(String title, double[][] mat) {
        String str, sum;

        System.out.println(title);
        for (double[] doubles : mat) {
            str = Arrays.toString(Arrays.stream(doubles).mapToObj(FMT::format).toArray());
            sum = FMT.format(Arrays.stream(doubles).reduce(0, Double::sum));
            System.out.println(str + " : sum=" + sum);
        }
        System.out.println();
    }

    public double[] getProbA() {
        return probA;
    }

    public void setProbA(double[] probA) {
        this.probA = probA;
    }

    public double[] getProbB() {
        return probB;
    }

    public void setProbB(double[] probB) {
        this.probB = probB;
    }

    public double[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(double[][] matriz) {
        this.matriz = matriz;
    }

    public double[][] getProbPosteriori() {
        return probPosteriori;
    }

    public void setProbPosteriori(double[][] probPosteriori) {
        this.probPosteriori = probPosteriori;
    }

    public double[][] getProbSucesosSimultaneos() {
        return probSucesosSimultaneos;
    }

    public void setProbSucesosSimultaneos(double[][] probSucesosSimultaneos) {
        this.probSucesosSimultaneos = probSucesosSimultaneos;
    }
}
