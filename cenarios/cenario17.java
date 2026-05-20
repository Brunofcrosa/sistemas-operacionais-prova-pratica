public class cenario17 {
    static int[] numeros = new int[1_000_000];
    static long[] parciais = new long[4];

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < numeros.length; i++) numeros[i] = 1;
        Thread[] threads = new Thread[4];
        int bloco = numeros.length / threads.length;
        for (int i = 0; i < threads.length; i++) {
            int inicio = i * bloco;
            int fim = i == threads.length - 1 ? numeros.length : inicio + bloco;
            threads[i] = new Thread(new Somador(i, inicio, fim));
            threads[i].start();
        }
        long total = 0;
        for (Thread t : threads) t.join();
        for (long parcial : parciais) total += parcial;
        System.out.println("Soma = " + total);
    }

    static class Somador implements Runnable {
        int id;
        int inicio;
        int fim;

        Somador(int id, int inicio, int fim) {
            this.id = id;
            this.inicio = inicio;
            this.fim = fim;
        }

        public void run() {
            long soma = 0;
            for (int i = inicio; i < fim; i++) soma += numeros[i];
            parciais[id] = soma;
        }
    }
}

