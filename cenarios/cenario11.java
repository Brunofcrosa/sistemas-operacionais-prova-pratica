import java.util.concurrent.Semaphore;

public class cenario11 {
    static Semaphore api = new Semaphore(3, true);

    public static void main(String[] args) throws Exception {
        Thread[] requisicoes = new Thread[12];
        for (int i = 0; i < requisicoes.length; i++) {
            requisicoes[i] = new Thread(new Requisicao(i + 1));
            requisicoes[i].start();
        }
        for (Thread t : requisicoes) t.join();
    }

    static class Requisicao implements Runnable {
        int id;

        Requisicao(int id) {
            this.id = id;
        }

        public void run() {
            try {
                api.acquire();
                System.out.println("Requisicao " + id + " entrou. Simultaneas = " + (3 - api.availablePermits()));
                Thread.sleep(200);
                System.out.println("Requisicao " + id + " saiu");
                api.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

