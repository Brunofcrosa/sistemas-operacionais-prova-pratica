package br.ufsm.politecnico.csi.so.ponte;

public class Simulacao {

    public static void main(String[] args) {
        Ponte ponte = new Ponte();
        for (int i = 0; i < 200; i++) {
            Thread.ofPlatform().start(new Carro(ponte, i));
        }
    }

}
