package br.ufsm.politecnico.csi.so;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProdutoresConsumidoresMonitores {

    private final int TAM_BUF = 5;
    private List<Integer> buffer = new ArrayList<>(TAM_BUF);

    public static void main(String[] args) {
        new ProdutoresConsumidoresMonitores();
    }

    public ProdutoresConsumidoresMonitores() {
        Thread.ofPlatform().start(new Produtor());
        Thread.ofPlatform().start(new Consumidor());
    }

    public class Produtor implements Runnable {

        private Random rnd = new SecureRandom();
        @Override
        public void run() {
            while (true) {
                synchronized (buffer) {
                    if (buffer.size() == TAM_BUF) {
                        //pridutor precisa esperar
                        try {
                            System.out.println("[PROD] Dormiu...");
                            buffer.wait();
                            System.out.println("[PROD] Acordou...");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int dado = produzDado();
                    System.out.println("[PROD] Produziu " + dado + ".");
                    buffer.add(dado);
                    if (buffer.size() == 1) {
                        buffer.notify();
                    }
                }
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
                synchronized (buffer) {
                    if (buffer.size() == 0) {
                        //buffer vazio, consumidor dorme
                        try {
                            System.out.println("[CONS] Dormiu...");
                            buffer.wait();
                            System.out.println("[CONS] Acordou...");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int dado = buffer.remove(0);
                    consomeDado(dado);
                    if (buffer.size() + 1 == TAM_BUF) {
                        buffer.notify();
                    }
                }
            }
        }

        private void consomeDado(int dado) {
            System.out.println("[CONS] Consumiu " + dado + ".");
        }

    }

}
