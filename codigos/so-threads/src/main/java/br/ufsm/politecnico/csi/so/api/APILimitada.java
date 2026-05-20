package br.ufsm.politecnico.csi.so.api;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class APILimitada {

    private Semaphore acessosAPI = new Semaphore(5, true);

    private void acessaAPI() throws InterruptedException {
        acessosAPI.acquire();
    }

    public void printLog() {
        System.out.println("[SERASA] " + (5 - acessosAPI.availablePermits()) + " acessos simultaneos agora.");
        System.out.println("[SERASA] " + acessosAPI.getQueueLength() + " requisições aguardando na fila.");
    }


    private void terminaAcessoAPI() {
        acessosAPI.release();
    }

    private Random rnd = new SecureRandom();

    public boolean isClienteNegativado(String cpf) throws InterruptedException {
        acessaAPI();
        Thread.sleep(rnd.nextLong(10));
        terminaAcessoAPI();
        return false;
    }

    public long getScore(String cpf) throws InterruptedException {
        acessaAPI();
        Thread.sleep(rnd.nextLong(10));
        terminaAcessoAPI();
        return 100;
    }

}
