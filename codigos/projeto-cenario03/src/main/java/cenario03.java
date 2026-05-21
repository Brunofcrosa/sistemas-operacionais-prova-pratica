import java.util.LinkedList;
import java.util.Queue;

public class cenario03 {
    static final int TAMANHO = 4;
    static Queue<Integer> buffer = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        Thread p = new Thread(new Produtor());
        Thread c = new Thread(new Consumidor());
        p.start();
        c.start();
        p.join();
        c.join();
    }

    static class Produtor implements Runnable {
        public void run() {
            for (int i = 1; i <= 12; i++) {
                synchronized (buffer) {
                    while (buffer.size() == TAMANHO) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    buffer.add(i);
                    System.out.println("Produziu " + i);
                    buffer.notifyAll();
                }
            }
        }
    }

    static class Consumidor implements Runnable {
        public void run() {
            for (int i = 1; i <= 12; i++) {
                synchronized (buffer) {
                    while (buffer.isEmpty()) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int valor = buffer.remove();
                    System.out.println("Consumiu " + valor);
                    buffer.notifyAll();
                }
            }
        }
    }
}

