package br.ufsm.politecnico.csi.so.api;

import java.security.SecureRandom;
import java.util.Random;

public class Requisicao implements Runnable {

    private APILimitada api;
    private int numero;
    private Random rnd = new SecureRandom();

    public Requisicao(APILimitada api, int numero) {
        this.api = api;
        this.numero = numero;
    }

    @Override
    public void run() {
        while (true) {
            try { long score = api.getScore("000.000.000-00"); } catch (InterruptedException e) { throw new RuntimeException(e); }
            try { boolean neg = api.isClienteNegativado("000.000.000-00"); } catch (InterruptedException e) { throw new RuntimeException(e); }
            try {
                Thread.sleep(rnd.nextLong(2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
