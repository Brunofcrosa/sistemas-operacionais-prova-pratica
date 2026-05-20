public class cenario12 {
    static Conta contaA = new Conta(1, 1000);
    static Conta contaB = new Conta(2, 1000);

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Transferidor(contaA, contaB, 10));
        Thread t2 = new Thread(new Transferidor(contaB, contaA, 20));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Conta A = " + contaA.saldo);
        System.out.println("Conta B = " + contaB.saldo);
        System.out.println("Total = " + (contaA.saldo + contaB.saldo));
    }

    static class Conta {
        int id;
        int saldo;

        Conta(int id, int saldo) {
            this.id = id;
            this.saldo = saldo;
        }
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

    static class Transferidor implements Runnable {
        Conta origem;
        Conta destino;
        int valor;

        Transferidor(Conta origem, Conta destino, int valor) {
            this.origem = origem;
            this.destino = destino;
            this.valor = valor;
        }

        public void run() {
            for (int i = 0; i < 1000; i++) transferir(origem, destino, valor);
        }
    }
}

