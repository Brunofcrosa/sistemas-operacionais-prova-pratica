import java.util.concurrent.Semaphore;

public class Main {
    static Semaphore impressoras = new Semaphore(4, true);

    public static void main(String[] args) throws Exception {
        Thread[] alunos = new Thread[12];

        for (int i = 0; i < alunos.length; i++) {
            alunos[i] = new Thread(new Aluno(i + 1));
            alunos[i].start();
        }

        for (int i = 0; i < alunos.length; i++) {
            alunos[i].join();
        }

        System.out.println("Todas as impressoes terminaram.");
    }

    static class Aluno implements Runnable {
        int id;

        Aluno(int id) {
            this.id = id;
        }

        public void run() {
            try {
                System.out.println("Aluno " + id + " chegou ao laboratorio.");
                impressoras.acquire();
                try {
                    System.out.println("Aluno " + id + " comecou a imprimir.");
                    Thread.sleep(150 + (id % 4) * 50);
                    System.out.println("Aluno " + id + " terminou a impressao.");
                } finally {
                    impressoras.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
