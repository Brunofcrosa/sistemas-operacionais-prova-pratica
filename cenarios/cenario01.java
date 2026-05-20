import java.util.concurrent.Semaphore;

public class cenario01 {
    static long contador = 0;
    static Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Incrementador());
        Thread t2 = new Thread(new Incrementador());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("contador = " + contador);
    }

    static class Incrementador implements Runnable {
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                try {
                    mutex.acquire();
                    contador++;
                    mutex.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

