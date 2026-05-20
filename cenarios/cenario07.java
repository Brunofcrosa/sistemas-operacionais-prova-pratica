public class cenario07 {
    static Ponte ponte = new Ponte();

    public static void main(String[] args) throws Exception {
        Thread[] carros = new Thread[10];
        for (int i = 0; i < carros.length; i++) {
            String sentido = i % 2 == 0 ? "NORTE" : "SUL";
            carros[i] = new Thread(new Carro(i + 1, sentido));
            carros[i].start();
            Thread.sleep(30);
        }
        for (Thread carro : carros) carro.join();
    }

    static class Ponte {
        String sentidoAtual = "NORTE";
        int naPonte = 0;
        int capacidade = 3;

        synchronized void entrar(String sentido) throws InterruptedException {
            while ((naPonte > 0 && !sentidoAtual.equals(sentido)) || naPonte == capacidade) {
                wait();
            }
            if (naPonte == 0) sentidoAtual = sentido;
            naPonte++;
            System.out.println("Entrou " + sentido + ", na ponte = " + naPonte);
        }

        synchronized void sair(String sentido) {
            naPonte--;
            System.out.println("Saiu " + sentido + ", na ponte = " + naPonte);
            notifyAll();
        }
    }

    static class Carro implements Runnable {
        int id;
        String sentido;

        Carro(int id, String sentido) {
            this.id = id;
            this.sentido = sentido;
        }

        public void run() {
            try {
                ponte.entrar(sentido);
                Thread.sleep(150);
                ponte.sair(sentido);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
