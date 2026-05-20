import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class cenario02 {
    static final int TAMANHO = 5;
    static Queue<Integer> buffer = new LinkedList<>();
    static Semaphore mutex = new Semaphore(1);
    static Semaphore vazio = new Semaphore(TAMANHO);
    static Semaphore cheio = new Semaphore(0);

    public static void main(String[] args) throws Exception {
        Thread p1 = new Thread(new Produtor(1));
        Thread p2 = new Thread(new Produtor(2));
        Thread c1 = new Thread(new Consumidor(1));
        p1.start();
        p2.start();
        c1.start();
        p1.join();
        p2.join();
        c1.join();
    }

    static class Produtor implements Runnable {
        int id;

        Produtor(int id) {
            this.id = id;
        }

        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    vazio.acquire();
                    mutex.acquire();
                    int valor = id * 100 + i;
                    buffer.add(valor);
                    System.out.println("Produtor " + id + " produziu " + valor);
                    mutex.release();
                    cheio.release();
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class Consumidor implements Runnable {
        int id;

        Consumidor(int id) {
            this.id = id;
        }

        public void run() {
            for (int i = 1; i <= 20; i++) {
                try {
                    cheio.acquire();
                    mutex.acquire();
                    int valor = buffer.remove();
                    System.out.println("Consumidor " + id + " consumiu " + valor);
                    mutex.release();
                    vazio.release();
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

