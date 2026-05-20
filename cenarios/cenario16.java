public class cenario16 {
    static int ingressos = 20;

    public static void main(String[] args) throws Exception {
        Thread[] vendedores = new Thread[4];
        for (int i = 0; i < vendedores.length; i++) {
            vendedores[i] = new Thread(new Vendedor(i + 1));
            vendedores[i].start();
        }
        for (Thread v : vendedores) v.join();
        System.out.println("Ingressos restantes = " + ingressos);
    }

    static synchronized boolean vender(int vendedor) {
        if (ingressos <= 0) return false;
        ingressos--;
        System.out.println("Vendedor " + vendedor + " vendeu. Restam " + ingressos);
        return true;
    }

    static class Vendedor implements Runnable {
        int id;

        Vendedor(int id) {
            this.id = id;
        }

        public void run() {
            while (vender(id)) {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

