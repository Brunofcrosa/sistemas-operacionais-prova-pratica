package br.ufsm.politecnico.csi.monitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class Main {

    private static final int CAPACIDADE_BUFFER = 5;
    private static final int TOTAL_ITENS = 12;
    private static final int TOTAL_CONSUMIDORES = 2;
    private static final int POISON_PILL = Integer.MIN_VALUE;

    public static void main(String[] args) throws InterruptedException {
        new Main().executar();
    }

    private void executar() throws InterruptedException {
        System.out.println("Projeto de monitor pronto para prova.");
        System.out.println("Edite apenas este Main.java se o enunciado mudar.");
        System.out.println();

        BufferMonitorado buffer = new BufferMonitorado(CAPACIDADE_BUFFER);

        Thread produtor = Thread.ofPlatform().name("produtor").start(() -> {
            try {
                produzir(buffer);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });

        List<Thread> consumidores = new ArrayList<>();
        for (int i = 1; i <= TOTAL_CONSUMIDORES; i++) {
            final int consumidorId = i;
            Thread consumidor = Thread.ofPlatform().name("consumidor-" + consumidorId).start(() -> {
                try {
                    consumir(buffer, consumidorId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
            consumidores.add(consumidor);
        }

        produtor.join();
        for (Thread consumidor : consumidores) {
            consumidor.join();
        }

        System.out.println();
        System.out.println("Execucao finalizada.");
    }

    private void produzir(BufferMonitorado buffer) throws InterruptedException {
        for (int item = 1; item <= TOTAL_ITENS; item++) {
            buffer.put(item);
            System.out.println("[produtor] produziu " + item + " | buffer=" + buffer.size());
        }

        for (int i = 0; i < TOTAL_CONSUMIDORES; i++) {
            buffer.put(POISON_PILL);
        }
    }

    private void consumir(BufferMonitorado buffer, int consumidorId) throws InterruptedException {
        while (true) {
            int item = buffer.take();
            if (item == POISON_PILL) {
                System.out.println("[consumidor-" + consumidorId + "] encerrando.");
                return;
            }
            System.out.println("[consumidor-" + consumidorId + "] consumiu " + item + " | buffer=" + buffer.size());
        }
    }

    private static final class BufferMonitorado {

        private final int capacidade;
        private final Deque<Integer> fila;

        private BufferMonitorado(int capacidade) {
            this.capacidade = capacidade;
            this.fila = new ArrayDeque<>(capacidade);
        }

        private synchronized void put(int valor) throws InterruptedException {
            while (fila.size() == capacidade) {
                wait();
            }
            fila.addLast(valor);
            notifyAll();
        }

        private synchronized int take() throws InterruptedException {
            while (fila.isEmpty()) {
                wait();
            }
            int valor = fila.removeFirst();
            notifyAll();
            return valor;
        }

        private synchronized int size() {
            return fila.size();
        }
    }
}
