package br.ufsm.politecnico.csi.so;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProdutoresConsumidores {

    private final int TAM_BUF = 5;
    private List<Integer> buffer = new ArrayList<>(TAM_BUF);
    private Semaphore mutex = new Semaphore(1);
    private Semaphore vazio = new Semaphore(TAM_BUF);
    private Semaphore cheio = new Semaphore(0);

    public static void main(String[] args) {
        new ProdutoresConsumidores();
    }

    public ProdutoresConsumidores() {
        Thread.ofPlatform().start(new Produtor());
        Thread.ofPlatform().start(new Consumidor());
    }

    public class Produtor implements Runnable {

        private Random rnd = new SecureRandom();
        @Override
        public void run() {
            while (true) {
                int dado = produzDado();
                System.out.println("[PROD] Produziu " + dado + ".");
                try { vazio.acquire(); } catch (InterruptedException e) { throw new RuntimeException(e); }
                try { mutex.acquire(); } catch (InterruptedException e) { throw new RuntimeException(e); }
                buffer.add(dado);
                mutex.release();
                if (cheio.hasQueuedThreads()) {
                    System.out.println("[CONS] zzzZZZZzzzz");
                    System.out.println("[PROD] Acorda o consumidor.");
                }
                cheio.release();
            }
        }

        private int produzDado() {
            return rnd.nextInt();
        }
    }

    public class Consumidor implements Runnable {

        @Override
        public void run() {
            while (true) {

                try { cheio.acquire(); } catch (InterruptedException e) { throw new RuntimeException(e); }
                try { mutex.acquire(); } catch (InterruptedException e) { throw new RuntimeException(e); }
                int dado = buffer.remove(0);
                consomeDado(dado);
                mutex.release();
                if (vazio.hasQueuedThreads()) {
                    System.out.println("[PROD] zzzZZZZzzzz");
                    System.out.println("[CONS] Acorda o produtor.");
                }
                vazio.release();
            }
        }

        private void consomeDado(int dado) {
            System.out.println("[CONS] Consumiu " + dado + ".");
        }

    }

}
