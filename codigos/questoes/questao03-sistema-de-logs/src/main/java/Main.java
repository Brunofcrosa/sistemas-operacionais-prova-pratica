import java.util.LinkedList;
import java.util.Queue;

public class Main {
    static Queue<String> filaDeLogs = new LinkedList<String>();
    static boolean geracaoTerminou = false;

    public static void main(String[] args) throws Exception {
        Thread gravadora = new Thread(new Gravadora());
        gravadora.start();

        Thread[] geradores = new Thread[4];
        for (int i = 0; i < geradores.length; i++) {
            geradores[i] = new Thread(new Gerador(i + 1));
            geradores[i].start();
        }

        for (int i = 0; i < geradores.length; i++) {
            geradores[i].join();
        }

        synchronized (filaDeLogs) {
            geracaoTerminou = true;
            filaDeLogs.notifyAll();
        }

        gravadora.join();
        System.out.println("Todos os logs foram gravados.");
    }

    static class Gerador implements Runnable {
        int id;

        Gerador(int id) {
            this.id = id;
        }

        public void run() {
            try {
                for (int i = 1; i <= 5; i++) {
                    String mensagem = "Gerador " + id + " criou a mensagem " + i;
                    synchronized (filaDeLogs) {
                        filaDeLogs.add(mensagem);
                        filaDeLogs.notifyAll();
                    }
                    System.out.println("Enfileirado: " + mensagem);
                    Thread.sleep(40L * id);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Gravadora implements Runnable {
        public void run() {
            try {
                while (true) {
                    String mensagem;
                    synchronized (filaDeLogs) {
                        while (filaDeLogs.isEmpty() && !geracaoTerminou) {
                            System.out.println("Gravadora dormindo sem logs.");
                            filaDeLogs.wait();
                        }
                        if (filaDeLogs.isEmpty() && geracaoTerminou) {
                            break;
                        }
                        mensagem = filaDeLogs.remove();
                    }
                    System.out.println("Gravadora imprimiu: " + mensagem);
                    Thread.sleep(70);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
