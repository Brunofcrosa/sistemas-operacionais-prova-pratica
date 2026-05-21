public class cenario08 {
    static Barreira barreira = new Barreira(4);

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[4];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Trabalhador(i + 1));
            threads[i].start();
        }
        for (Thread t : threads) t.join();
    }

    static class Barreira {
        int total;
        int chegaram = 0;
        int geracao = 0;

        Barreira(int total) {
            this.total = total;
        }

        synchronized void esperar() throws InterruptedException {
            int minhaGeracao = geracao;
            chegaram++;
            if (chegaram == total) {
                chegaram = 0;
                geracao++;
                notifyAll();
            } else {
                while (minhaGeracao == geracao) wait();
            }
        }
    }

    static class Trabalhador implements Runnable {
        int id;

        Trabalhador(int id) {
            this.id = id;
        }

        public void run() {
            try {
                System.out.println("Thread " + id + " terminou fase 1");
                Thread.sleep(100L * id);
                barreira.esperar();
                System.out.println("Thread " + id + " iniciou fase 2");
                Thread.sleep(80L * id);
                barreira.esperar();
                System.out.println("Thread " + id + " terminou tudo");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

