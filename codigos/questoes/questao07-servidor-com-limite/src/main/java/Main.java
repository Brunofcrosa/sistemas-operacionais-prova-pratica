import java.util.concurrent.Semaphore;

public class Main {
    static Semaphore servidor = new Semaphore(3, true);

    public static void main(String[] args) throws Exception {
        Thread[] consultas = new Thread[20];

        for (int i = 0; i < consultas.length; i++) {
            consultas[i] = new Thread(new Consulta(i + 1));
            consultas[i].start();
        }

        for (int i = 0; i < consultas.length; i++) {
            consultas[i].join();
        }

        System.out.println("Todas as consultas terminaram.");
    }

    static class Consulta implements Runnable {
        int id;

        Consulta(int id) {
            this.id = id;
        }

        public void run() {
            try {
                System.out.println("Thread " + id + " quer consultar o servidor.");
                servidor.acquire();
                try {
                    System.out.println("Thread " + id + " entrou. Simultaneas = " + (3 - servidor.availablePermits()));
                    Thread.sleep(180);
                    System.out.println("Thread " + id + " saiu.");
                } finally {
                    servidor.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
