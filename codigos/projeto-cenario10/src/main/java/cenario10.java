import java.util.LinkedList;
import java.util.Queue;

public class cenario10 {
    static Queue<Integer> fila1 = new LinkedList<>();
    static Queue<Integer> fila2 = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        Thread produtor = new Thread(new Produtor());
        Thread processador = new Thread(new Processador());
        Thread consumidor = new Thread(new Consumidor());
        produtor.start();
        processador.start();
        consumidor.start();
        produtor.join();
        processador.join();
        consumidor.join();
    }

    static void colocar(Queue<Integer> fila, int valor) {
        synchronized (fila) {
            fila.add(valor);
            fila.notifyAll();
        }
    }

    static int retirar(Queue<Integer> fila) throws InterruptedException {
        synchronized (fila) {
            while (fila.isEmpty()) fila.wait();
            return fila.remove();
        }
    }

    static class Produtor implements Runnable {
        public void run() {
            for (int i = 1; i <= 8; i++) {
                colocar(fila1, i);
                System.out.println("Produziu " + i);
            }
            colocar(fila1, -1);
        }
    }

    static class Processador implements Runnable {
        public void run() {
            try {
                while (true) {
                    int valor = retirar(fila1);
                    if (valor == -1) {
                        colocar(fila2, -1);
                        break;
                    }
                    int novo = valor * valor;
                    colocar(fila2, novo);
                    System.out.println("Processou " + valor + " para " + novo);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Consumidor implements Runnable {
        public void run() {
            try {
                while (true) {
                    int valor = retirar(fila2);
                    if (valor == -1) break;
                    System.out.println("Consumiu " + valor);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

