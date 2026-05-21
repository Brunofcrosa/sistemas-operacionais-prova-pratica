package br.ufsm.politecnico.csi.semaforo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class Main {

    private static final int TOTAL_THREADS = 4;
    private static final int TOTAL_INCREMENTOS_POR_THREAD = 250_000;

    private final Semaphore mutex = new Semaphore(1);
    private long contador = 0;

    public static void main(String[] args) throws InterruptedException {
        new Main().executar();
    }

    private void executar() throws InterruptedException {
        System.out.println("Projeto de semaforo pronto para prova.");
        System.out.println("Edite apenas este Main.java se o enunciado mudar.");
        System.out.println();

        long inicio = System.nanoTime();
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= TOTAL_THREADS; i++) {
            final int workerId = i;
            Thread thread = Thread.ofPlatform().name("worker-" + workerId).start(() -> {
                try {
                    executarWorker(workerId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long esperado = (long) TOTAL_THREADS * TOTAL_INCREMENTOS_POR_THREAD;
        long tempoMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);

        System.out.println();
        System.out.println("Contador final  : " + contador);
        System.out.println("Valor esperado  : " + esperado);
        System.out.println("Tempo decorrido : " + tempoMs + " ms");
    }

    private void executarWorker(int workerId) throws InterruptedException {
        for (int i = 0; i < TOTAL_INCREMENTOS_POR_THREAD; i++) {
            mutex.acquire();
            try {
                contador++;
            } finally {
                mutex.release();
            }
        }
        System.out.println("worker-" + workerId + " terminou.");
    }
}
