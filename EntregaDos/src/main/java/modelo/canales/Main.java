package modelo.canales;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Canal canal = new Canal();
        String[] canales = {"canal1.txt", "canal2.txt", "canal3.txt"};

        for (String c : canales) {
            canal.infoCanal(c);
        }
    }
}
