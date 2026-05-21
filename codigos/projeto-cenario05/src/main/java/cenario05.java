import java.util.concurrent.Semaphore;

public class cenario05 {
    static final int N = 5;
    static Semaphore[] garfos = new Semaphore[N];
    static Semaphore sala = new Semaphore(N - 1);

    public static void main(String[] args) throws Exception {
        Thread[] filosofos = new Thread[N];
        for (int i = 0; i < N; i++) garfos[i] = new Semaphore(1);
        for (int i = 0; i < N; i++) {
            filosofos[i] = new Thread(new Filosofo(i));
            filosofos[i].start();
        }
        for (int i = 0; i < N; i++) filosofos[i].join();
    }

    static class Filosofo implements Runnable {
        int id;

        Filosofo(int id) {
            this.id = id;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {
                try {
                    System.out.println("Filosofo " + id + " pensando");
                    Thread.sleep(80);
                    sala.acquire();
                    garfos[id].acquire();
                    garfos[(id + 1) % N].acquire();
                    System.out.println("Filosofo " + id + " comendo");
                    Thread.sleep(100);
                    garfos[(id + 1) % N].release();
                    garfos[id].release();
                    sala.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

