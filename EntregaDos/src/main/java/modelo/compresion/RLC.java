package modelo.compresion;

import modelo.fuente.Fuente;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RLC extends Fuente {
    public static final String RESULTSPATH = Fuente.RESULTSPATH + "rlc" + File.separator;
    protected String fileToEncode;

    public void compressTxt(String filename, boolean generateTxtFile) throws IOException {
        ByteArrayOutputStream encodebytes = new ByteArrayOutputStream();
        Reader in = new FileReader(RESOURCESPATH + filename);
        Writer out = null;
        int ant, act;
        int frec;

        this.fileToEncode = filename;
        if (generateTxtFile) {
            out = new FileWriter(RESULTSPATH + filename.substring(0, filename.lastIndexOf('.')) + "RLC.txt");
        }
        act = in.read();
        while (act != -1) {
            ant = act;
            frec = 1;
            while ((act = in.read()) == ant) {
                frec += 1;
            }
            this.saveData(encodebytes, ant, frec);
            if (generateTxtFile) {
                out.write((char) ant);
                out.write(Integer.toString(frec));
            }
        }
        encodebytes.writeTo(new FileOutputStream(RESULTSPATH + filename.substring(0, filename.lastIndexOf('.')) + ".rlc"));
        in.close();
        if (generateTxtFile)
            out.close();
    }

    /**
     * Escribe el caracter (16 bits) y la frecuencia (32 bits).
     *
     * @param encodebytes stream de bytes
     * @param character   caracter
     * @param frec        frecuencia
     */
    private void saveData(ByteArrayOutputStream encodebytes, int character, int frec) {
        encodebytes.write((byte) ((character >> 8) & 0xff));
        encodebytes.write((byte) (character & 0xff));
        encodebytes.write((byte) ((frec >> 24) & 0xff));
        encodebytes.write((byte) ((frec >> 16) & 0xff));
        encodebytes.write((byte) ((frec >> 8) & 0xff));
        encodebytes.write((byte) (frec & 0xff));
    }

    public void decompressTxt(String filename) throws IOException {
        FileInputStream in = new FileInputStream(RESULTSPATH + filename);
        Writer out = new FileWriter(RESOURCESPATH + "recovery" + File.separator + filename.substring(0, filename.lastIndexOf('.')) + ".txt");
        int car, frec;

        car = in.read();
        while (car != -1) {
            car = car << 8 | (in.read() & 0xff);
            frec = in.read() & 0xff;
            frec = frec << 8 | (in.read() & 0xff);
            frec = frec << 8 | (in.read() & 0xff); // 16
            frec = frec << 8 | (in.read() & 0xff); // 24
            for (int i = 0; i < frec; i++) {
                out.write((char) car);
            }
            car = in.read();
        }
        in.close();
        out.close();
    }

    public void compressRaw(String imageFileName) throws IOException {
        Scanner scan = new Scanner(new FileReader(RESOURCESPATH + imageFileName));
        ByteArrayOutputStream encodebytes = new ByteArrayOutputStream();
        int ant, act;
        int frecuencia;

        this.fileToEncode = imageFileName;
        try {
            act = scan.nextInt();
            while (scan.hasNext()) {
                ant = act;
                frecuencia = 1;
                while (scan.hasNext() && (act = scan.nextInt()) == ant) {
                    frecuencia += 1;
                }
                this.saveData(encodebytes, ant, frecuencia);
            }
            scan.close();
            encodebytes.writeTo(new FileOutputStream(RESULTSPATH + imageFileName.substring(0, imageFileName.lastIndexOf('.')) + ".rlc"));
        } catch (NoSuchElementException e) {
            System.out.println("Empty file");
        }
    }

    public void decompressRaw(String compressImageFileName) throws IOException {
        FileInputStream in = new FileInputStream(RESULTSPATH + compressImageFileName);
        Writer out = new FileWriter(RESOURCESPATH + "recovery" + File.separator + "recoveryRLC" + compressImageFileName.substring(0, compressImageFileName.lastIndexOf('.')) + ".txt");
        int numero, frecuencia;

        numero = in.read();
        while (numero != -1) {
            numero = numero << 8 | (in.read() & 0xff);
            frecuencia = in.read() & 0xff;
            frecuencia = frecuencia << 8 | in.read() & 0xff;
            frecuencia = frecuencia << 8 | in.read() & 0xff;
            frecuencia = frecuencia << 8 | in.read() & 0xff;
            for (int i = 0; i < frecuencia; i++) {
                out.write(numero + "\r\n");
            }
            numero = in.read();
        }
        in.close();
        out.close();
    }

    public double getTasaDeCompresion(String original, String compressed) throws IOException {
        long originalFileSize = Files.size(Paths.get(RESOURCESPATH + original));
        long compressedFileSize = Files.size(Paths.get(RESULTSPATH + compressed));
        return (double) originalFileSize / compressedFileSize;
    }

    public void compressInfo() throws IOException {
        PrintWriter out = new PrintWriter(RESULTSPATH + this.fileToEncode.substring(0, this.fileToEncode.lastIndexOf('.')) + "CodeInfo.txt");
        out.println("Entropia:");
        out.println("H(S) = " + this.entropia() + " bits\n");
        out.println("Kraft:");
        out.println("k = " + this.kraft() + "\n");
        out.println("Longitud media:");
        out.println("L = " + this.longitudMedia() + "\n");
        out.println("Rendimiento:");
        out.println("n = " + this.rendimiento() + "\n");
        out.println("Rendundancia:");
        out.println("n = " + this.redundancia() + "\n");
        out.println("Tasa de compresion = " + this.getTasaDeCompresion(fileToEncode, fileToEncode.substring(0, fileToEncode.lastIndexOf('.')) + ".rlc"));
        out.close();
    }

    @Override
    public double kraft() {
        return 0;
    }

    @Override
    public double longitudMedia() {
        return 0;
    }
}
