package br.ufsm.politecnico.csi.so.api;

public class Simulacao {

    public static void main(String[] args) throws InterruptedException {
        APILimitada api = new APILimitada();
        for (int i = 0; i < 200; i++) {
            Thread.ofVirtual().start(new Requisicao(api, i));
        }
        while (true) {
            Thread.sleep(5000);
            api.printLog();
        }
    }

}
