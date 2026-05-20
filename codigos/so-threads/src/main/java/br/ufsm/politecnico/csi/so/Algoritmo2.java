package br.ufsm.politecnico.csi.so;

import java.security.SecureRandom;
import java.util.Random;

public class Algoritmo2 {

    private boolean ca = false;
    private boolean cb = false;
    private long numero = 0;


    public class ProcessoA implements Runnable {
        private long numA = 0;
        @Override
        public void run() {
            while (numA < 10_000_000) {
                ca = true;
                while (cb) { }
                //acessou a regiao crítica
                //System.out.println("[PA] Acessou a região crítica.");
                numA++;
                numero++;
                ca = false;
                //System.out.println("[PA] Saiu da região crítica.");
            }
        }
    }

    public class ProcessoB implements Runnable {

        private long numB = 0;

        @Override
        public void run() {
            while (numB < 10_000_000) {
                cb = true;
                while (ca) { }
                //acessou a regiao crítica
                //System.out.println("[PB] Acessou a região crítica.");
                numB++;
                numero++;
                cb = false;
                //System.out.println("[PB] Saiu da região crítica.");
            }
        }
    }

    public Algoritmo2() throws InterruptedException {
        ProcessoA pa = new ProcessoA();
        ProcessoB pb = new ProcessoB();
        Thread t1 = Thread.ofPlatform().start(pa);
        Thread t2 = Thread.ofPlatform().start(pb);
        t1.join();
        t2.join();
        //as duas threads já terminaram
        System.out.println("numero = " + numero + ", somaA+B = " + (pa.numA + pb.numB));
    }

    public static void main(String[] args) throws InterruptedException {
        new Algoritmo2();
    }

}
