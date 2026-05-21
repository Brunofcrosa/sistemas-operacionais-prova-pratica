public class Main {
    static BarreiraDeLargada barreira = new BarreiraDeLargada(5);

    public static void main(String[] args) throws Exception {
        Thread[] corredores = new Thread[5];

        for (int i = 0; i < corredores.length; i++) {
            corredores[i] = new Thread(new Corredor(i + 1));
            corredores[i].start();
        }

        for (int i = 0; i < corredores.length; i++) {
            corredores[i].join();
        }

        System.out.println("Todos os corredores largaram.");
    }

    static class BarreiraDeLargada {
        int total;
        int prontos = 0;

        BarreiraDeLargada(int total) {
            this.total = total;
        }

        synchronized void esperarTodos() throws InterruptedException {
            prontos++;
            if (prontos == total) {
                notifyAll();
            } else {
                while (prontos < total) {
                    wait();
                }
            }
        }
    }

    static class Corredor implements Runnable {
        int id;

        Corredor(int id) {
            this.id = id;
        }

        public void run() {
            try {
                Thread.sleep(120L * id);
                System.out.println("Corredor " + id + " esta pronto.");
                barreira.esperarTodos();
                System.out.println("Corredor " + id + " largou.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
