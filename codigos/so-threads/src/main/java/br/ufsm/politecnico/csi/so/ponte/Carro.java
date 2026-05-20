package br.ufsm.politecnico.csi.so.ponte;

import java.security.SecureRandom;
import java.util.Random;

public class Carro implements Runnable {

    private Random rnd = new SecureRandom();
    private Ponte ponte;
    private int numCarro;

    public Carro(Ponte ponte, int numCarro) {
        this.ponte = ponte;
        this.numCarro = numCarro;
    }

    @Override
    public void run() {
        while (true) {
            boolean sentinoNorte = rnd.nextBoolean();
            if (sentinoNorte) {
                try { ponte.entrarNorte(); } catch (InterruptedException e) { throw new RuntimeException(e); }
            } else {
                try { ponte.entrarSul(); } catch (InterruptedException e) { throw new RuntimeException(e); }
            }
            geraLog(sentinoNorte);
            atravessarPonte();
            if (sentinoNorte) {
                ponte.sairSul();
            } else {
                ponte.sairNorte();
            }
            try {
                Thread.sleep(rnd.nextLong(100));
            } catch (InterruptedException e) { }
        }
    }

    private void geraLog(boolean sentinoNorte) {
        System.out.println("[CARRO " + numCarro + "]" +
                (sentinoNorte ? " norte -> sul." : " sul -> norte"));
    }

    private void atravessarPonte() {
        try {
            Thread.sleep(rnd.nextLong(1000));
        } catch (InterruptedException e) {

        }
    }
}
