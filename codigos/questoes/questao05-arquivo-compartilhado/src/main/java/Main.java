import java.util.concurrent.Semaphore;

public class Main {
    static int leitores = 0;
    static String conteudo = "Versao inicial do arquivo";
    static Semaphore mutexLeitores = new Semaphore(1);
    static Semaphore escrita = new Semaphore(1);

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[] {
                new Thread(new Escritor(1)),
                new Thread(new Escritor(2)),
                new Thread(new Leitor(1)),
                new Thread(new Leitor(2)),
                new Thread(new Leitor(3)),
                new Thread(new Leitor(4))
        };

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("Conteudo final do arquivo: " + conteudo);
    }

    static class Leitor implements Runnable {
        int id;

        Leitor(int id) {
            this.id = id;
        }

        public void run() {
            try {
                for (int i = 0; i < 3; i++) {
                    mutexLeitores.acquire();
                    leitores++;
                    if (leitores == 1) {
                        escrita.acquire();
                    }
                    mutexLeitores.release();

                    System.out.println("Leitor " + id + " leu: " + conteudo);
                    Thread.sleep(70);

                    mutexLeitores.acquire();
                    leitores--;
                    if (leitores == 0) {
                        escrita.release();
                    }
                    mutexLeitores.release();
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Escritor implements Runnable {
        int id;

        Escritor(int id) {
            this.id = id;
        }

        public void run() {
            try {
                for (int i = 1; i <= 2; i++) {
                    escrita.acquire();
                    conteudo = "Atualizacao " + i + " do escritor " + id;
                    System.out.println("Escritor " + id + " escreveu no arquivo.");
                    Thread.sleep(120);
                    escrita.release();
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
