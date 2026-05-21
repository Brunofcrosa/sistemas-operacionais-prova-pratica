public class Main {
    static int[] numeros = new int[400000];
    static long[] parciais = new long[4];

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < numeros.length; i++) {
            numeros[i] = 1;
        }

        Thread[] threads = new Thread[4];
        int bloco = numeros.length / threads.length;

        for (int i = 0; i < threads.length; i++) {
            int inicio = i * bloco;
            int fim = i == threads.length - 1 ? numeros.length : inicio + bloco;
            threads[i] = new Thread(new Somador(i, inicio, fim));
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        long total = 0;
        for (int i = 0; i < parciais.length; i++) {
            total += parciais[i];
        }

        System.out.println("Soma total = " + total);
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
            for (int i = inicio; i < fim; i++) {
                soma += numeros[i];
            }
            parciais[id] = soma;
            System.out.println("Thread " + (id + 1) + " calculou " + soma + ".");
        }
    }
}
