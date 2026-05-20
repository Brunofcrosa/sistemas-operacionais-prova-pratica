package br.ufsm.politecnico.csi.so;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

public class ExperimentoConcorrencia {

    private Long numGeral = 0L;
    private Collection<Incremento> incrementos = new ArrayList<>();
    private Collection<Thread> threads = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        long milis = System.currentTimeMillis();
        ExperimentoConcorrencia exp = new ExperimentoConcorrencia(2);
        exp.join();
        exp.confereNumeros();
        System.out.println("Tempo decorrido: " + (System.currentTimeMillis() - milis));
    }

    public class Incremento implements Runnable {

        private long numero = 0;
        @Override
        public void run() {
            while (numero < 10_000_000) {
                synchronized (ExperimentoConcorrencia.this) {
                    numero++;
                    numGeral++;
                }
            }
        }
    }

    public void join() throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }

    public void confereNumeros() {
        long nThreads = 0;
        for (Incremento incremento : incrementos) {
            nThreads += incremento.numero;
        }
        System.out.println("numGeral = " + numGeral +
                ", numeroTotalThreads = " + nThreads);
    }

    public ExperimentoConcorrencia(int numThreads) {
        for (int i = 0; i < numThreads; i++) {
            Incremento inc = new Incremento();
            incrementos.add(inc);
            Thread t = Thread.ofPlatform().start(inc);
            threads.add(t);
        }
    }

}
