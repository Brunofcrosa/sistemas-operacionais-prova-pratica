# Templates Rapidos

Copie o bloco mais parecido e adapte nomes e quantidades.

## Classe Base Com Threads

```java
public class Prova {
    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Trabalho(1));
        Thread t2 = new Thread(new Trabalho(2));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class Trabalho implements Runnable {
        int id;

        Trabalho(int id) {
            this.id = id;
        }

        public void run() {
            System.out.println("Thread " + id);
        }
    }
}
```

## Mutex Com Semaphore

```java
import java.util.concurrent.Semaphore;

public class Prova {
    static Semaphore mutex = new Semaphore(1);
    static int contador = 0;

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[4];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Trabalho());
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println(contador);
    }

    static class Trabalho implements Runnable {
        public void run() {
            for (int i = 0; i < 1000; i++) {
                try {
                    mutex.acquire();
                    contador++;
                    mutex.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
```

## Recurso Limitado

```java
import java.util.concurrent.Semaphore;

public class Prova {
    static Semaphore limite = new Semaphore(3, true);

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 10; i++) new Thread(new Usuario(i)).start();
    }

    static class Usuario implements Runnable {
        int id;

        Usuario(int id) {
            this.id = id;
        }

        public void run() {
            try {
                limite.acquire();
                System.out.println(id + " entrou");
                Thread.sleep(200);
                System.out.println(id + " saiu");
                limite.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

## Monitor Com Fila

```java
import java.util.LinkedList;
import java.util.Queue;

public class Prova {
    static Queue<Integer> fila = new LinkedList<>();
    static boolean terminou = false;

    static int retirar() throws InterruptedException {
        synchronized (fila) {
            while (fila.isEmpty() && !terminou) fila.wait();
            if (fila.isEmpty() && terminou) return -1;
            return fila.remove();
        }
    }

    static void colocar(int valor) {
        synchronized (fila) {
            fila.add(valor);
            fila.notifyAll();
        }
    }
}
```

## Produtor Consumidor Com Semaforos

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Prova {
    static Queue<Integer> buffer = new LinkedList<>();
    static Semaphore mutex = new Semaphore(1);
    static Semaphore vazio = new Semaphore(5);
    static Semaphore cheio = new Semaphore(0);

    static void produzir(int valor) throws InterruptedException {
        vazio.acquire();
        mutex.acquire();
        buffer.add(valor);
        mutex.release();
        cheio.release();
    }

    static int consumir() throws InterruptedException {
        cheio.acquire();
        mutex.acquire();
        int valor = buffer.remove();
        mutex.release();
        vazio.release();
        return valor;
    }
}
```

## Barreira Simples

```java
static class Barreira {
    int total;
    int chegaram = 0;

    Barreira(int total) {
        this.total = total;
    }

    synchronized void esperar() throws InterruptedException {
        chegaram++;
        if (chegaram == total) {
            notifyAll();
        } else {
            while (chegaram < total) wait();
        }
    }
}
```

## Duas Travas Sem Deadlock

```java
static void usar(Conta a, Conta b) {
    Conta primeira = a.id < b.id ? a : b;
    Conta segunda = a.id < b.id ? b : a;

    synchronized (primeira) {
        synchronized (segunda) {
            System.out.println("usando as duas");
        }
    }
}
```

