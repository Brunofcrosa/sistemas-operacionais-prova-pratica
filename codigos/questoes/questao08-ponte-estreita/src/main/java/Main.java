public class Main {
    static Ponte ponte = new Ponte(2);

    public static void main(String[] args) throws Exception {
        Thread[] carros = new Thread[10];

        for (int i = 0; i < carros.length; i++) {
            String sentido = i % 2 == 0 ? "NORTE" : "SUL";
            carros[i] = new Thread(new Carro(i + 1, sentido));
            carros[i].start();
            Thread.sleep(30);
        }

        for (int i = 0; i < carros.length; i++) {
            carros[i].join();
        }

        System.out.println("Todos os carros atravessaram a ponte.");
    }

    static class Ponte {
        int capacidade;
        int naPonte = 0;
        int esperandoNorte = 0;
        int esperandoSul = 0;
        String sentidoAtual = "";
        String ultimoSentido = "SUL";

        Ponte(int capacidade) {
            this.capacidade = capacidade;
        }

        synchronized void entrar(int id, String sentido) throws InterruptedException {
            if ("NORTE".equals(sentido)) {
                esperandoNorte++;
            } else {
                esperandoSul++;
            }

            try {
                while (naPonte == capacidade
                        || (naPonte > 0 && !sentidoAtual.equals(sentido))
                        || (naPonte == 0 && deveEsperarPorAlternancia(sentido))) {
                    wait();
                }

                if (naPonte == 0) {
                    sentidoAtual = sentido;
                }
                naPonte++;
                System.out.println("Carro " + id + " entrou no sentido " + sentido + ". Na ponte = " + naPonte);
            } finally {
                if ("NORTE".equals(sentido)) {
                    esperandoNorte--;
                } else {
                    esperandoSul--;
                }
            }
        }

        synchronized void sair(int id, String sentido) {
            naPonte--;
            System.out.println("Carro " + id + " saiu no sentido " + sentido + ". Na ponte = " + naPonte);
            if (naPonte == 0) {
                ultimoSentido = sentido;
                sentidoAtual = "";
            }
            notifyAll();
        }

        boolean deveEsperarPorAlternancia(String sentido) {
            if ("NORTE".equals(sentido)) {
                return esperandoSul > 0 && "NORTE".equals(ultimoSentido);
            }
            return esperandoNorte > 0 && "SUL".equals(ultimoSentido);
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
                ponte.entrar(id, sentido);
                Thread.sleep(150);
                ponte.sair(id, sentido);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
