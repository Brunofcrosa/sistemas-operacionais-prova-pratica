package br.ufsm.politecnico.csi.so;

import java.security.SecureRandom;
import java.util.Random;

public class Algoritmo4 {

    private boolean ca = false;
    private boolean cb = false;
    private long numero = 0;


    public class ProcessoA implements Runnable {
        private long numA = 0;
        private Random rnd = new SecureRandom();
        @Override
        public void run() {
            while (numA < 10_000_000) {
                ca = true;
                while (cb) {
                    ca = false;
                    try {
                        Thread.sleep(rnd.nextLong(2));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ca = true;
                }
                //acessou a regiao crítica
                numA++;
                numero++;
                ca = false;
            }
        }
    }

    public class ProcessoB implements Runnable {

        private long numB = 0;
        private Random rnd = new SecureRandom();

        @Override
        public void run() {
            while (numB < 10_000_000) {
                cb = true;
                while (ca) {
                    cb = false;
                    try {
                        Thread.sleep(rnd.nextLong(2));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    cb = true;
                }
                //acessou a regiao crítica
                //System.out.println("[PB] Acessou a região crítica.");
                numB++;
                numero++;
                cb = false;
                //System.out.println("[PB] Saiu da região crítica.");
            }
        }
    }

    public Algoritmo4() throws InterruptedException {
        long milis = System.currentTimeMillis();
        ProcessoA pa = new ProcessoA();
        ProcessoB pb = new ProcessoB();
        Thread t1 = Thread.ofPlatform().start(pa);
        Thread t2 = Thread.ofPlatform().start(pb);
        t1.join();
        t2.join();
        //as duas threads já terminaram
        System.out.println("numero = " + numero + ", somaA+B = " + (pa.numA + pb.numB));
        System.out.println("Tempo decorrido: " + (System.currentTimeMillis() - milis));
    }

    public static void main(String[] args) throws InterruptedException {
        new Algoritmo4();
    }

}
