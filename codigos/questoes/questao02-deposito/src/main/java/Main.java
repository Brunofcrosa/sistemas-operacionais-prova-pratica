import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Main {
    static final int CAPACIDADE = 6;
    static final int PRODUTORES = 2;
    static final int CONSUMIDORES = 3;
    static final int CAIXAS_POR_PRODUTOR = 6;
    static final int CAIXA_FIM = -1;

    static Queue<Integer> deposito = new LinkedList<Integer>();
    static Semaphore mutex = new Semaphore(1);
    static Semaphore vazio = new Semaphore(CAPACIDADE);
    static Semaphore cheio = new Semaphore(0);

    public static void main(String[] args) throws Exception {
        Thread[] produtores = new Thread[PRODUTORES];
        Thread[] consumidores = new Thread[CONSUMIDORES];

        for (int i = 0; i < produtores.length; i++) {
            produtores[i] = new Thread(new Produtor(i + 1));
            produtores[i].start();
        }

        for (int i = 0; i < consumidores.length; i++) {
            consumidores[i] = new Thread(new Consumidor(i + 1));
            consumidores[i].start();
        }

        for (int i = 0; i < produtores.length; i++) {
            produtores[i].join();
        }

        for (int i = 0; i < CONSUMIDORES; i++) {
            colocarCaixa(CAIXA_FIM);
        }

        for (int i = 0; i < consumidores.length; i++) {
            consumidores[i].join();
        }

        System.out.println("Deposito encerrado sem caixas pendentes.");
    }

    static void colocarCaixa(int valor) throws InterruptedException {
        vazio.acquire();
        mutex.acquire();
        try {
            deposito.add(Integer.valueOf(valor));
        } finally {
            mutex.release();
        }
        cheio.release();
    }

    static int retirarCaixa() throws InterruptedException {
        cheio.acquire();
        mutex.acquire();
        try {
            return deposito.remove().intValue();
        } finally {
            mutex.release();
            vazio.release();
        }
    }

    static class Produtor implements Runnable {
        int id;

        Produtor(int id) {
            this.id = id;
        }

        public void run() {
            try {
                for (int i = 1; i <= CAIXAS_POR_PRODUTOR; i++) {
                    int caixa = id * 100 + i;
                    colocarCaixa(caixa);
                    System.out.println("Produtor " + id + " colocou a caixa " + caixa + ".");
                    Thread.sleep(60);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Consumidor implements Runnable {
        int id;

        Consumidor(int id) {
            this.id = id;
        }

        public void run() {
            try {
                while (true) {
                    int caixa = retirarCaixa();
                    if (caixa == CAIXA_FIM) {
                        System.out.println("Consumidor " + id + " encerrou.");
                        break;
                    }
                    System.out.println("Consumidor " + id + " retirou a caixa " + caixa + ".");
                    Thread.sleep(90);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
