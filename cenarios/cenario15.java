import java.util.LinkedList;
import java.util.Queue;

public class cenario15 {
    static Queue<Integer> tarefas = new LinkedList<>();
    static boolean terminou = false;

    public static void main(String[] args) throws Exception {
        Thread[] workers = new Thread[3];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(new Worker(i + 1));
            workers[i].start();
        }
        synchronized (tarefas) {
            for (int i = 1; i <= 12; i++) tarefas.add(i);
            terminou = true;
            tarefas.notifyAll();
        }
        for (Thread w : workers) w.join();
    }

    static class Worker implements Runnable {
        int id;

        Worker(int id) {
            this.id = id;
        }

        public void run() {
            while (true) {
                int tarefa;
                synchronized (tarefas) {
                    while (tarefas.isEmpty() && !terminou) {
                        try {
                            tarefas.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (tarefas.isEmpty() && terminou) break;
                    tarefa = tarefas.remove();
                }
                try {
                    System.out.println("Worker " + id + " executando tarefa " + tarefa);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

