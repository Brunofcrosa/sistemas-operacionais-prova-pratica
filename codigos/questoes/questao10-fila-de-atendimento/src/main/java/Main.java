import java.util.LinkedList;
import java.util.Queue;

public class Main {
    static Queue<Integer> tarefas = new LinkedList<Integer>();

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 12; i++) {
            tarefas.add(Integer.valueOf(i));
        }

        Thread[] trabalhadores = new Thread[3];
        for (int i = 0; i < trabalhadores.length; i++) {
            trabalhadores[i] = new Thread(new Trabalhador(i + 1));
            trabalhadores[i].start();
        }

        for (int i = 0; i < trabalhadores.length; i++) {
            trabalhadores[i].join();
        }

        System.out.println("Fila de atendimento esvaziada.");
    }

    static class Trabalhador implements Runnable {
        int id;

        Trabalhador(int id) {
            this.id = id;
        }

        public void run() {
            try {
                while (true) {
                    int tarefa;
                    synchronized (tarefas) {
                        if (tarefas.isEmpty()) {
                            break;
                        }
                        tarefa = tarefas.remove().intValue();
                    }
                    System.out.println("Trabalhador " + id + " atendeu a tarefa " + tarefa + ".");
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
