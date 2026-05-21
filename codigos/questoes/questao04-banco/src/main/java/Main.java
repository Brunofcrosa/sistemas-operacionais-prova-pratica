public class Main {
    static Conta contaA = new Conta(1, 1000);
    static Conta contaB = new Conta(2, 1000);

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Transferidor(contaA, contaB, 10, 1000));
        Thread t2 = new Thread(new Transferidor(contaB, contaA, 20, 1000));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Saldo final conta A = " + contaA.saldo);
        System.out.println("Saldo final conta B = " + contaB.saldo);
        System.out.println("Total do sistema = " + (contaA.saldo + contaB.saldo));
    }

    static void transferir(Conta origem, Conta destino, int valor) {
        Conta primeira = origem.id < destino.id ? origem : destino;
        Conta segunda = origem.id < destino.id ? destino : origem;

        synchronized (primeira) {
            synchronized (segunda) {
                if (origem.saldo >= valor) {
                    origem.saldo -= valor;
                    destino.saldo += valor;
                }
            }
        }
    }

    static class Conta {
        int id;
        int saldo;

        Conta(int id, int saldo) {
            this.id = id;
            this.saldo = saldo;
        }
    }

    static class Transferidor implements Runnable {
        Conta origem;
        Conta destino;
        int valor;
        int repeticoes;

        Transferidor(Conta origem, Conta destino, int valor, int repeticoes) {
            this.origem = origem;
            this.destino = destino;
            this.valor = valor;
            this.repeticoes = repeticoes;
        }

        public void run() {
            for (int i = 0; i < repeticoes; i++) {
                transferir(origem, destino, valor);
            }
        }
    }
}
