import java.util.concurrent.Semaphore;

public class cenario06 {
    static Semaphore vagas = new Semaphore(3, true);

    public static void main(String[] args) throws Exception {
        Thread[] carros = new Thread[8];
        for (int i = 0; i < carros.length; i++) {
            carros[i] = new Thread(new Carro(i + 1));
            carros[i].start();
        }
        for (Thread carro : carros) carro.join();
    }

    static class Carro implements Runnable {
        int id;

        Carro(int id) {
            this.id = id;
        }

        public void run() {
            try {
                System.out.println("Carro " + id + " chegou");
                vagas.acquire();
                System.out.println("Carro " + id + " estacionou");
                Thread.sleep(300);
                System.out.println("Carro " + id + " saiu");
                vagas.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

