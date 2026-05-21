public class cenario20 {
    static Object recursoA = new Object();
    static Object recursoB = new Object();

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Processo("P1", recursoA, recursoB));
        Thread t2 = new Thread(new Processo("P2", recursoB, recursoA));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Terminou sem deadlock porque os recursos foram ordenados");
    }

    static class Processo implements Runnable {
        String nome;
        Object primeiroPedido;
        Object segundoPedido;

        Processo(String nome, Object primeiroPedido, Object segundoPedido) {
            this.nome = nome;
            this.primeiroPedido = primeiroPedido;
            this.segundoPedido = segundoPedido;
        }

        public void run() {
            Object primeiro = System.identityHashCode(primeiroPedido) < System.identityHashCode(segundoPedido) ? primeiroPedido : segundoPedido;
            Object segundo = primeiro == primeiroPedido ? segundoPedido : primeiroPedido;
            synchronized (primeiro) {
                dormir();
                synchronized (segundo) {
                    System.out.println(nome + " usou os dois recursos");
                }
            }
        }

        void dormir() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

