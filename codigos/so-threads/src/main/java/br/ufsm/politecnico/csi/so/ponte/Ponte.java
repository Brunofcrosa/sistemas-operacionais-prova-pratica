package br.ufsm.politecnico.csi.so.ponte;

import java.util.concurrent.Semaphore;

public class Ponte {

    private Boolean sentidoNorte = true;
    private Semaphore carros = new Semaphore(6, true);

    void entrarNorte() throws InterruptedException {
        synchronized (this) {
            if (carros.availablePermits() == 6) {
                if (!sentidoNorte) {
                    sentidoNorte = true;
                    System.out.println("[PONTE] Mudou sentido para norte.");
                    this.notifyAll();

                }
            }
            while (!sentidoNorte) {
                System.out.println("[PONTE] Carro aguardando sentido norte...");
                this.wait();
            }
            carros.acquire();
        }
    }

    void sairNorte() {
        carros.release();
    }

    void entrarSul() throws InterruptedException {
        synchronized (this) {
            if (carros.availablePermits() == 6) {
                if (sentidoNorte) {
                    sentidoNorte = false;
                    System.out.println("[PONTE] Mudou sentido para sul.");
                    this.notifyAll();
                }
            }
            while (sentidoNorte) {
                System.out.println("[PONTE] Carro aguardando sentido sul...");
                this.wait();
            }
            carros.acquire();
        }

    }

    void sairSul() {
        carros.release();
    }

}
