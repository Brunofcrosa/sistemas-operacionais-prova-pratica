package br.ufsm.politecnico.csi.so;

import java.util.ArrayList;
import java.util.Collection;

public class CalculoPiParalelo {

    public static void main(String[] args) throws InterruptedException {
        Collection<CalculoPi> calculos = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CalculoPi cPi = new CalculoPi();
            calculos.add(cPi);
            Thread.ofPlatform().start(cPi);
        }
        long milis = System.currentTimeMillis();
        while (true) {
            System.out.println("Tempo decorrido: " + (System.currentTimeMillis() - milis) + "ms.");
            System.out.println("Pi = " + calculaPiColaborativo(calculos));
            Thread.sleep(10000);
        }
    }

    public static double calculaPiColaborativo(Collection<CalculoPi> calculos) {
        long nTotal = 0;
        long nDentro = 0;
        for (CalculoPi cPi : calculos) {
            nDentro += cPi.nDentro;
            nTotal += cPi.nTotal;
        }
        return 4.0 * nDentro / nTotal;
    }

    public static class CalculoPi implements Runnable {

        private long nTotal;
        private long nDentro;

        @Override
        public void run() {
            while (true) {
                double x = Math.random();
                double y = Math.random();
                nTotal++;
                if(x * x + y * y < 1.0){
                    nDentro++;
                }
            }
        }

        public void printPi() {
            System.out.println("[" + Thread.currentThread() + "] PI = " + getPi());
        }

        public double getPi() {
            return 4.0 * nDentro / nTotal;
        }

    }

}
