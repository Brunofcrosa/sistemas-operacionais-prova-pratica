import java.util.PriorityQueue;

public class cenario19 {
    static PriorityQueue<Tarefa> fila = new PriorityQueue<>();
    static boolean acabou = false;

    public static void main(String[] args) throws Exception {
        Thread c1 = new Thread(new Consumidor(1));
        Thread c2 = new Thread(new Consumidor(2));
        c1.start();
        c2.start();
        synchronized (fila) {
            fila.add(new Tarefa("backup", 3));
            fila.add(new Tarefa("login usuario", 1));
            fila.add(new Tarefa("relatorio", 2));
            fila.add(new Tarefa("pagamento", 1));
            fila.add(new Tarefa("email", 4));
            acabou = true;
            fila.notifyAll();
        }
        c1.join();
        c2.join();
    }

    static class Tarefa implements Comparable<Tarefa> {
        String nome;
        int prioridade;

        Tarefa(String nome, int prioridade) {
            this.nome = nome;
            this.prioridade = prioridade;
        }

        public int compareTo(Tarefa outra) {
            return this.prioridade - outra.prioridade;
        }
    }

    static class Consumidor implements Runnable {
        int id;

        Consumidor(int id) {
            this.id = id;
        }

        public void run() {
            while (true) {
                Tarefa tarefa;
                synchronized (fila) {
                    while (fila.isEmpty() && !acabou) {
                        try {
                            fila.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (fila.isEmpty() && acabou) break;
                    tarefa = fila.remove();
                }
                System.out.println("Consumidor " + id + " executou " + tarefa.nome + " prioridade " + tarefa.prioridade);
            }
        }
    }
}

