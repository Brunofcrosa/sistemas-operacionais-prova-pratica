package br.ufsm.politecnico.csi.so;

public class Main {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread());
        for (int j = 0; j < 5_000_000; j++) {
            final int jFinal = j;
            Thread t = Thread.ofVirtual().start(() -> {
                System.out.println("[" + Thread.currentThread() + "] j = " + jFinal);
                try {
                    Thread.sleep(1_000_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
        }


    }
}