import java.util.concurrent.Semaphore;

public class Main {
    static Semaphore aTerminouEtapa1 = new Semaphore(0);
    static Semaphore bTerminouEtapa1 = new Semaphore(0);

    public static void main(String[] args) throws Exception {
        Thread a = new Thread(new ProcessoA());
        Thread b = new Thread(new ProcessoB());

        a.start();
        b.start();

        a.join();
        b.join();

        System.out.println("As duas threads respeitaram a ordem entre as etapas.");
    }

    static class ProcessoA implements Runnable {
        public void run() {
            try {
                System.out.println("A executou a etapa 1.");
                aTerminouEtapa1.release();
                bTerminouEtapa1.acquire();
                System.out.println("A executou a etapa 2 depois de B.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class ProcessoB implements Runnable {
        public void run() {
            try {
                System.out.println("B executou a etapa 1.");
                bTerminouEtapa1.release();
                aTerminouEtapa1.acquire();
                System.out.println("B executou a etapa 2 depois de A.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
