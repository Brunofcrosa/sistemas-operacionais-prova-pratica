import java.util.PriorityQueue;

public class Main {
    static PriorityQueue<Paciente> fila = new PriorityQueue<Paciente>();

    public static void main(String[] args) throws Exception {
        fila.add(new Paciente("Ana", 2, 1));
        fila.add(new Paciente("Bruno", 1, 2));
        fila.add(new Paciente("Carla", 2, 3));
        fila.add(new Paciente("Daniel", 1, 4));
        fila.add(new Paciente("Eva", 2, 5));
        fila.add(new Paciente("Felipe", 1, 6));

        Thread[] atendentes = new Thread[2];
        for (int i = 0; i < atendentes.length; i++) {
            atendentes[i] = new Thread(new Atendente(i + 1));
            atendentes[i].start();
        }

        for (int i = 0; i < atendentes.length; i++) {
            atendentes[i].join();
        }

        System.out.println("Todos os pacientes foram atendidos.");
    }

    static class Paciente implements Comparable<Paciente> {
        String nome;
        int prioridade;
        int ordemChegada;

        Paciente(String nome, int prioridade, int ordemChegada) {
            this.nome = nome;
            this.prioridade = prioridade;
            this.ordemChegada = ordemChegada;
        }

        public int compareTo(Paciente outro) {
            if (this.prioridade != outro.prioridade) {
                return this.prioridade - outro.prioridade;
            }
            return this.ordemChegada - outro.ordemChegada;
        }
    }

    static class Atendente implements Runnable {
        int id;

        Atendente(int id) {
            this.id = id;
        }

        public void run() {
            try {
                while (true) {
                    Paciente paciente;
                    synchronized (fila) {
                        if (fila.isEmpty()) {
                            break;
                        }
                        paciente = fila.remove();
                    }
                    System.out.println("Atendente " + id + " chamou " + paciente.nome
                            + " com prioridade " + paciente.prioridade + ".");
                    Thread.sleep(120);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
