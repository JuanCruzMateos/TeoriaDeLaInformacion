package modelo.compresion;

import modelo.fuente.Fuente;

import java.io.*;

public class RLC {
    public void encode(String filename) throws IOException {
        ByteArrayOutputStream encodebytes = new ByteArrayOutputStream();
        Reader in = new FileReader(Fuente.RESOURCESPATH + filename);
        int ant, act = 0;
        int frec;

        act = in.read();
        while (act != -1) {
            ant = act;
            frec = 1;
            while ((act = in.read()) == ant) {
                ant = act;
                frec += 1;
            }
            this.saveData(encodebytes, ant, frec);
        }
        encodebytes.writeTo(new FileOutputStream(Fuente.RESULTSPATH + filename.substring(0, filename.lastIndexOf('.')) + ".rlc"));
        in.close();
    }

    private void saveData(ByteArrayOutputStream encodebytes, int character, int frec) {
        encodebytes.write((byte) ((character >> 8) & 0xff));
        encodebytes.write((byte) (character & 0xff));
        encodebytes.write((byte) ((frec >> 24) & 0xff));
        encodebytes.write((byte) ((frec >> 16) & 0xff));
        encodebytes.write((byte) ((frec >> 8) & 0xff));
        encodebytes.write((byte) (frec & 0xff));
    }

    public void decode(String filename) throws IOException {
        FileInputStream in = new FileInputStream(Fuente.RESULTSPATH + filename);
        Writer out = new FileWriter(Fuente.RESOURCESPATH + "recoveryRLC" + filename.substring(0, filename.lastIndexOf('.')) + ".txt");
        int car, frec = 0;

        car = in.read();
        while (car != -1) {
            car = car << 8 | (in.read() & 0xff);
            frec = in.read();
            frec = frec << 8 | (in.read() & 0xff);
            frec = frec << 16 | (in.read() & 0xff);
            frec = frec << 24 | (in.read() & 0xff);
            for (int i = 0; i < frec; i++) {
                out.write((char) car);
            }
            car = in.read();
        }
        in.close();
        out.close();
    }
}
