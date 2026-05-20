public class cenario18 {
    static Latch latch = new Latch(5);

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) new Thread(new Servico(i)).start();
        System.out.println("Principal aguardando servicos");
        latch.aguardar();
        System.out.println("Todos os servicos terminaram");
    }

    static class Latch {
        int faltam;

        Latch(int faltam) {
            this.faltam = faltam;
        }

        synchronized void finalizar() {
            faltam--;
            if (faltam == 0) notifyAll();
        }

        synchronized void aguardar() throws InterruptedException {
            while (faltam > 0) wait();
        }
    }

    static class Servico implements Runnable {
        int id;

        Servico(int id) {
            this.id = id;
        }

        public void run() {
            try {
                Thread.sleep(100L * id);
                System.out.println("Servico " + id + " pronto");
                latch.finalizar();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

