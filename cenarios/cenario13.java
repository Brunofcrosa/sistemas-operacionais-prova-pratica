import java.util.concurrent.Semaphore;

public class cenario13 {
    static final int CADEIRAS = 3;
    static Semaphore clientes = new Semaphore(0);
    static Semaphore barbeiro = new Semaphore(0);
    static Semaphore mutex = new Semaphore(1);
    static int esperando = 0;
    static int atendidos = 0;
    static int totalClientes = 10;

    public static void main(String[] args) throws Exception {
        Thread b = new Thread(new Barbeiro());
        b.start();
        Thread[] cs = new Thread[totalClientes];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = new Thread(new Cliente(i + 1));
            cs[i].start();
            Thread.sleep(40);
        }
        for (Thread c : cs) c.join();
        clientes.release();
        b.join();
    }

    static class Barbeiro implements Runnable {
        public void run() {
            try {
                while (true) {
                    clientes.acquire();
                    mutex.acquire();
                    if (atendidos >= totalClientes) {
                        mutex.release();
                        break;
                    }
                    esperando--;
                    barbeiro.release();
                    mutex.release();
                    System.out.println("Barbeiro cortando cabelo");
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Cliente implements Runnable {
        int id;

        Cliente(int id) {
            this.id = id;
        }

        public void run() {
            try {
                mutex.acquire();
                if (esperando < CADEIRAS) {
                    esperando++;
                    System.out.println("Cliente " + id + " sentou");
                    clientes.release();
                    mutex.release();
                    barbeiro.acquire();
                    System.out.println("Cliente " + id + " atendido");
                } else {
                    System.out.println("Cliente " + id + " foi embora");
                    mutex.release();
                }
                mutex.acquire();
                atendidos++;
                mutex.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

