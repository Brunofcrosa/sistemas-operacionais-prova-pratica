import java.util.concurrent.Semaphore;

public class cenario04 {
    static int leitores = 0;
    static int dado = 0;
    static Semaphore mutexLeitores = new Semaphore(1);
    static Semaphore escrita = new Semaphore(1);

    public static void main(String[] args) throws Exception {
        Thread e1 = new Thread(new Escritor(1));
        Thread l1 = new Thread(new Leitor(1));
        Thread l2 = new Thread(new Leitor(2));
        Thread l3 = new Thread(new Leitor(3));
        e1.start();
        l1.start();
        l2.start();
        l3.start();
        e1.join();
        l1.join();
        l2.join();
        l3.join();
    }

    static class Leitor implements Runnable {
        int id;

        Leitor(int id) {
            this.id = id;
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    mutexLeitores.acquire();
                    leitores++;
                    if (leitores == 1) escrita.acquire();
                    mutexLeitores.release();
                    System.out.println("Leitor " + id + " leu " + dado);
                    Thread.sleep(80);
                    mutexLeitores.acquire();
                    leitores--;
                    if (leitores == 0) escrita.release();
                    mutexLeitores.release();
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class Escritor implements Runnable {
        int id;

        Escritor(int id) {
            this.id = id;
        }

        public void run() {
            for (int i = 1; i <= 5; i++) {
                try {
                    escrita.acquire();
                    dado = i * 10;
                    System.out.println("Escritor " + id + " escreveu " + dado);
                    Thread.sleep(120);
                    escrita.release();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

