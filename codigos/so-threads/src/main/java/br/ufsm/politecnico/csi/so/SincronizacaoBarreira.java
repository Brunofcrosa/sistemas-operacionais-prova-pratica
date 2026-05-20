package br.ufsm.politecnico.csi.so;

import java.security.SecureRandom;
import java.util.Random;

public class SincronizacaoBarreira {

    public static void main(String[] args) {
        new SincronizacaoBarreira();
    }

    public SincronizacaoBarreira() {
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread.ofPlatform().start(new ThreadBarreira());
        }
    }

    private final int NUM_THREADS = 5;
    private int threadsQueTerminaram = 0;

    private void barreira() {
        synchronized (this) {
            System.out.println("[" + Thread.currentThread().getName() + "] Chegou no fim.");
            threadsQueTerminaram++;
            if (threadsQueTerminaram == NUM_THREADS) {
                this.notifyAll();
            } else {
                try { this.wait(); } catch (InterruptedException e) { throw new RuntimeException(e); }
            }
        }
    }

    public class ThreadBarreira implements Runnable {

        private Random rnd = new SecureRandom();
        @Override
        public void run() {
            fazAlgumaCoisa();
            barreira();
            System.out.println("[" + Thread.currentThread().getName() + "] sincronizou, continuando...");
            synchronized (SincronizacaoBarreira.this) {
                SincronizacaoBarreira.this.threadsQueTerminaram = 0;
            }
            fazAlgumaCoisa();
            barreira();
            System.out.println("[" + Thread.currentThread().getName() + "] sincronizou, continuando...");
        }

        private void fazAlgumaCoisa() {
            try { Thread.sleep(rnd.nextLong(5000)); } catch (InterruptedException e) { throw new RuntimeException(e); }
        }
    }

}
