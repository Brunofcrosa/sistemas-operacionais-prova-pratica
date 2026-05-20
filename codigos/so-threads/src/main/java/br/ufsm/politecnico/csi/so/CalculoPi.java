package br.ufsm.politecnico.csi.so;

public class CalculoPi {

    public static double pi(int n){
        int acertos = 0;

        for(int i = 0; i < n; i++){
            double x = Math.random();
            double y = Math.random();

            if(x * x + y * y < 1.0){
                acertos++;
            }
        }
        return (4.0 * acertos / n);
    }

    public static void main(String[] args) {
        System.out.println(pi(1_000_000_000));
    }

}
