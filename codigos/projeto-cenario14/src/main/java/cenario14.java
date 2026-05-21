import java.util.concurrent.Semaphore;

public class cenario14 {
    static Semaphore tabacoPapel = new Semaphore(0);
    static Semaphore tabacoFosforo = new Semaphore(0);
    static Semaphore papelFosforo = new Semaphore(0);
    static Semaphore mesaLivre = new Semaphore(1);
    static volatile boolean terminou = false;

    public static void main(String[] args) throws Exception {
        Thread agente = new Thread(new Agente());
        Thread f1 = new Thread(new Fumante("fosforo", tabacoPapel));
        Thread f2 = new Thread(new Fumante("papel", tabacoFosforo));
        Thread f3 = new Thread(new Fumante("tabaco", papelFosforo));
        agente.start();
        f1.start();
        f2.start();
        f3.start();
        agente.join();
        f1.join();
        f2.join();
        f3.join();
    }

    static class Agente implements Runnable {
        public void run() {
            try {
                for (int i = 0; i < 9; i++) {
                    mesaLivre.acquire();
                    int tipo = i % 3;
                    if (tipo == 0) {
                        System.out.println("Agente colocou tabaco e papel");
                        tabacoPapel.release();
                    } else if (tipo == 1) {
                        System.out.println("Agente colocou tabaco e fosforo");
                        tabacoFosforo.release();
                    } else {
                        System.out.println("Agente colocou papel e fosforo");
                        papelFosforo.release();
                    }
                }
                mesaLivre.acquire();
                terminou = true;
                tabacoPapel.release();
                tabacoFosforo.release();
                papelFosforo.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Fumante implements Runnable {
        String possui;
        Semaphore liberacao;

        Fumante(String possui, Semaphore liberacao) {
            this.possui = possui;
            this.liberacao = liberacao;
        }

        public void run() {
            try {
                while (true) {
                    liberacao.acquire();
                    if (terminou) break;
                    System.out.println("Fumante com " + possui + " fumou");
                    Thread.sleep(80);
                    mesaLivre.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
