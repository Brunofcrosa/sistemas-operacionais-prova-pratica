package br.ufsm.politecnico.csi.prototipo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

public final class Main {

    /*
     * Base pensada para prova:
     * - voce executa sempre esta classe Main
     * - pode alterar apenas este arquivo
     * - os modelos de concorrencia estao todos aqui dentro
     *
     * Modos disponiveis:
     * - basico
     * - semaforo
     * - monitor
     * - barreira
     *
     * Se preferir, no dia da prova voce pode ignorar o switch do main
     * e escrever a solucao direto no metodo main.
     */

    private static final int TOTAL_THREADS = 4;
    private static final int TOTAL_INCREMENTOS_POR_THREAD = 250_000;

    private Main() {
    }

    public static void main(String[] args) throws InterruptedException {
        String modo = args.length == 0 ? "basico" : args[0].trim().toLowerCase();

        switch (modo) {
            case "basico" -> executarBasico();
            case "semaforo" -> executarSemaforo();
            case "monitor" -> executarMonitor();
            case "barreira" -> executarBarreira();
            case "ajuda" -> mostrarAjuda();
            default -> {
                System.out.println("Modo desconhecido: " + modo);
                mostrarAjuda();
            }
        }
    }

    private static void executarBasico() throws InterruptedException {
        System.out.println("Executando modo basico.");
        System.out.println("Use este trecho quando a questao pedir criacao de threads e join.");
        System.out.println();

        Cronometro cronometro = Cronometro.iniciar();
        ContadorSeguro contador = new ContadorSeguro();

        List<Thread> workers = Threads.iniciar(
                TOTAL_THREADS,
                indice -> Threads.interrompivel(() -> executarIncrementos(contador, indice + 1)),
                "worker",
                false
        );

        Threads.aguardar(workers);

        long esperado = (long) TOTAL_THREADS * TOTAL_INCREMENTOS_POR_THREAD;
        System.out.println();
        System.out.println("Contador final  : " + contador.valor());
        System.out.println("Valor esperado  : " + esperado);
        System.out.println("Tempo decorrido : " + cronometro.milissegundos() + " ms");
        System.out.println();
        System.out.println("Outros modos: semaforo | monitor | barreira");
    }

    private static void executarIncrementos(ContadorSeguro contador, int workerId) {
        for (int i = 0; i < TOTAL_INCREMENTOS_POR_THREAD; i++) {
            contador.incrementar();
        }
        System.out.println("worker-" + workerId + " terminou.");
    }

    private static void executarSemaforo() throws InterruptedException {
        new ModeloSemaforo(4, 150_000).executar();
    }

    private static void executarMonitor() throws InterruptedException {
        new ModeloMonitorBuffer(5, 12, 2).executar();
    }

    private static void executarBarreira() throws InterruptedException {
        new ModeloBarreira(4, 2).executar();
    }

    private static void mostrarAjuda() {
        System.out.println("Uso:");
        System.out.println("  Main          -> roda o modo basico");
        System.out.println("  Main semaforo -> secao critica com Semaphore");
        System.out.println("  Main monitor  -> produtor/consumidor com monitor");
        System.out.println("  Main barreira -> sincronizacao por barreira");
        System.out.println();
        System.out.println("Fluxo rapido de prova:");
        System.out.println("  1. Edite apenas este arquivo Main.java");
        System.out.println("  2. Deixe o codigo da questao dentro do metodo main ou dos metodos abaixo");
        System.out.println("  3. Clique Run no IntelliJ");
    }

    private static final class ModeloSemaforo {

        private final Semaphore mutex = new Semaphore(1);
        private final int totalThreads;
        private final int totalIncrementosPorThread;
        private long contador;

        private ModeloSemaforo(int totalThreads, int totalIncrementosPorThread) {
            this.totalThreads = totalThreads;
            this.totalIncrementosPorThread = totalIncrementosPorThread;
        }

        private void executar() throws InterruptedException {
            Cronometro cronometro = Cronometro.iniciar();
            List<Thread> threads = Threads.iniciar(
                    totalThreads,
                    indice -> Threads.interrompivel(() -> incrementarComMutex(indice + 1)),
                    "semaforo",
                    false
            );

            Threads.aguardar(threads);

            long esperado = (long) totalThreads * totalIncrementosPorThread;
            System.out.println("Demo de semaforo finalizada.");
            System.out.println("Contador final  : " + contador);
            System.out.println("Valor esperado  : " + esperado);
            System.out.println("Tempo decorrido : " + cronometro.milissegundos() + " ms");
        }

        private void incrementarComMutex(int workerId) throws InterruptedException {
            for (int i = 0; i < totalIncrementosPorThread; i++) {
                mutex.acquire();
                try {
                    contador++;
                } finally {
                    mutex.release();
                }
            }
            System.out.println("semaforo-" + workerId + " terminou.");
        }
    }

    private static final class ModeloMonitorBuffer {

        private static final int POISON_PILL = Integer.MIN_VALUE;

        private final int capacidadeBuffer;
        private final int totalItens;
        private final int totalConsumidores;

        private ModeloMonitorBuffer(int capacidadeBuffer, int totalItens, int totalConsumidores) {
            this.capacidadeBuffer = capacidadeBuffer;
            this.totalItens = totalItens;
            this.totalConsumidores = totalConsumidores;
        }

        private void executar() throws InterruptedException {
            MonitorBuffer<Integer> buffer = new MonitorBuffer<>(capacidadeBuffer);

            Thread produtor = Thread.ofPlatform()
                    .name("produtor")
                    .start(Threads.interrompivel(() -> produzir(buffer)));

            List<Thread> consumidores = Threads.iniciar(
                    totalConsumidores,
                    indice -> Threads.interrompivel(() -> consumir(buffer, indice + 1)),
                    "consumidor",
                    false
            );

            produtor.join();
            Threads.aguardar(consumidores);

            System.out.println("Demo de monitor finalizada.");
        }

        private void produzir(MonitorBuffer<Integer> buffer) throws InterruptedException {
            for (int item = 1; item <= totalItens; item++) {
                buffer.put(item);
                System.out.println("[produtor] produziu " + item + " | buffer=" + buffer.size());
            }

            for (int i = 0; i < totalConsumidores; i++) {
                buffer.put(POISON_PILL);
            }
        }

        private void consumir(MonitorBuffer<Integer> buffer, int consumidorId) throws InterruptedException {
            while (true) {
                int item = buffer.take();
                if (item == POISON_PILL) {
                    System.out.println("[consumidor-" + consumidorId + "] encerrando.");
                    return;
                }
                System.out.println("[consumidor-" + consumidorId + "] consumiu " + item + " | buffer=" + buffer.size());
            }
        }
    }

    private static final class ModeloBarreira {

        private final int participantes;
        private final int fases;

        private ModeloBarreira(int participantes, int fases) {
            this.participantes = participantes;
            this.fases = fases;
        }

        private void executar() throws InterruptedException {
            BarreiraReutilizavel barreira = new BarreiraReutilizavel(participantes);
            List<Thread> threads = Threads.iniciar(
                    participantes,
                    indice -> Threads.interrompivel(() -> executarFases(barreira, indice + 1)),
                    "barreira",
                    false
            );

            Threads.aguardar(threads);
            System.out.println("Demo de barreira finalizada.");
        }

        private void executarFases(BarreiraReutilizavel barreira, int workerId) throws InterruptedException {
            for (int fase = 1; fase <= fases; fase++) {
                System.out.println("[barreira-" + workerId + "] iniciou fase " + fase);
                Thread.sleep(workerId * 100L);
                System.out.println("[barreira-" + workerId + "] aguardando fase " + fase);
                barreira.aguardar();
                System.out.println("[barreira-" + workerId + "] liberado fase " + fase);
            }
        }
    }

    private static final class Cronometro {

        private final long inicioNano;

        private Cronometro() {
            this.inicioNano = System.nanoTime();
        }

        private static Cronometro iniciar() {
            return new Cronometro();
        }

        private long milissegundos() {
            return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicioNano);
        }
    }

    private static final class ContadorSeguro {

        private long valor;

        private synchronized void incrementar() {
            valor++;
        }

        private synchronized long valor() {
            return valor;
        }
    }

    private static final class MonitorBuffer<T> {

        private final int capacidade;
        private final Deque<T> fila;

        private MonitorBuffer(int capacidade) {
            if (capacidade <= 0) {
                throw new IllegalArgumentException("A capacidade deve ser maior que zero.");
            }
            this.capacidade = capacidade;
            this.fila = new ArrayDeque<>(capacidade);
        }

        private synchronized void put(T item) throws InterruptedException {
            while (fila.size() == capacidade) {
                wait();
            }
            fila.addLast(item);
            notifyAll();
        }

        private synchronized T take() throws InterruptedException {
            while (fila.isEmpty()) {
                wait();
            }
            T item = fila.removeFirst();
            notifyAll();
            return item;
        }

        private synchronized int size() {
            return fila.size();
        }
    }

    private static final class BarreiraReutilizavel {

        private final int participantes;
        private int aguardando;
        private int geracao;

        private BarreiraReutilizavel(int participantes) {
            if (participantes <= 0) {
                throw new IllegalArgumentException("Participantes devem ser maiores que zero.");
            }
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

    private static final class Threads {

        private Threads() {
        }

        private static List<Thread> iniciar(
                int quantidade,
                IntFunction<Runnable> fabrica,
                String prefixo,
                boolean virtuais
        ) {
            List<Thread> threads = new ArrayList<>(quantidade);
            for (int i = 0; i < quantidade; i++) {
                String nome = prefixo + "-" + (i + 1);
                Runnable tarefa = fabrica.apply(i);
                Thread thread = virtuais
                        ? Thread.ofVirtual().name(nome).start(tarefa)
                        : Thread.ofPlatform().name(nome).start(tarefa);
                threads.add(thread);
            }
            return threads;
        }

        private static void aguardar(Collection<Thread> threads) throws InterruptedException {
            for (Thread thread : threads) {
                thread.join();
            }
        }

        private static Runnable interrompivel(TarefaInterrompivel tarefa) {
            return () -> {
                try {
                    tarefa.executar();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            };
        }
    }

    @FunctionalInterface
    private interface TarefaInterrompivel {
        void executar() throws InterruptedException;
    }
}
