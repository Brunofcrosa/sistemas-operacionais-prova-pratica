package br.ufsm.politecnico.csi.so;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Semaforo {

    private Semaphore mutex = new Semaphore(1);
    private long numero = 0;


    public class ProcessoA implements Runnable {
        private long numA = 0;
        private Random rnd = new SecureRandom();
        @Override
        public void run() {
            while (numA < 10_000_000) {
                try { mutex.acquire(); } catch (InterruptedException e) { throw new RuntimeException(e); }
                //acessou a regiao crítica
                numA++;
                numero++;
                mutex.release();
            }
        }
    }

    public class ProcessoB implements Runnable {

        private long numB = 0;

        @Override
        public void run() {
            while (numB < 10_000_000) {
                try { mutex.acquire(); } catch (InterruptedException e) { throw new RuntimeException(e); }
                numB++;
                numero++;
                mutex.release();
            }
        }
    }

    public Semaforo() throws InterruptedException {
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
        new Semaforo();
    }

}
