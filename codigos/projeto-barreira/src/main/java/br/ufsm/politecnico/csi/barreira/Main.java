package br.ufsm.politecnico.csi.barreira;

import java.util.ArrayList;
import java.util.List;

public final class Main {

    private static final int TOTAL_THREADS = 4;
    private static final int TOTAL_FASES = 2;

    public static void main(String[] args) throws InterruptedException {
        new Main().executar();
    }

    private void executar() throws InterruptedException {
        System.out.println("Projeto de barreira pronto para prova.");
        System.out.println("Edite apenas este Main.java se o enunciado mudar.");
        System.out.println();

        Barreira barreira = new Barreira(TOTAL_THREADS);
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= TOTAL_THREADS; i++) {
            final int workerId = i;
            Thread thread = Thread.ofPlatform().name("worker-" + workerId).start(() -> {
                try {
                    executarWorker(barreira, workerId);
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

        System.out.println();
        System.out.println("Execucao finalizada.");
    }

    private void executarWorker(Barreira barreira, int workerId) throws InterruptedException {
        for (int fase = 1; fase <= TOTAL_FASES; fase++) {
            System.out.println("[worker-" + workerId + "] iniciou fase " + fase);
            Thread.sleep(workerId * 100L);
            System.out.println("[worker-" + workerId + "] aguardando fase " + fase);
            barreira.aguardar();
            System.out.println("[worker-" + workerId + "] liberado fase " + fase);
        }
    }

    private static final class Barreira {

        private final int participantes;
        private int aguardando = 0;
        private int geracao = 0;

        private Barreira(int participantes) {
            this.participantes = participantes;
        }

        private synchronized void aguardar() throws InterruptedException {
            int geracaoAtual = geracao;
            aguardando++;

            if (aguardando == participantes) {
                aguardando = 0;
                geracao++;
                notifyAll();
                return;
            }

            while (geracaoAtual == geracao) {
                wait();
            }
        }
    }
}
