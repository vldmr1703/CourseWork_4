package methods;

import Jama.Matrix;
import functions.Function;
import system.solver.Gauss;

public class Steffensen extends Method {

    public Steffensen(Function function, double t, int m) {
        super(function, t, m);
    }

    @Override
    public double[] doIteration(double[] Aprev, double i, double[][] M) {
        double[] b = new double[n];
        double[] d = new double[n];
        double[] f;
        double[] mas;
        double[][] M1;
        double[] x0 = new double[n];
        double[] x1 = Aprev.clone();
        int q=0;
        do {
            if(i==m-1)
                ++q;
            for (int j = 0; j < n; j++) {
                x0[j] = x1[j];
            }
            f = function.getFunction(x0, i * h);
            for (int k = 0; k < n; k++) {
                b[k] = x0[k] - Aprev[k] - h * f[k];
                d[k] = x0[k] + b[k];
            }
            M1 = copyMatrix(divDifferences(x0.clone(), d, i * h));
            for (int j = 0; j < n; j++) {
                M[j] = M1[j].clone();
            }
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M[k][t] *= h;
                }
                M[k][k] = 1 - M[k][k];
            }
//            Gauss gauss = new Gauss(copyMatrix(M), b);
//            mas = gauss.getX();
            mas = multiplyMatrixVector(copyMatrix(M),b);
            for (int j = 0; j < n; j++) {
                x1[j] = x0[j] - mas[j];
            }
        }
        while (mNorm(x0, x1) >= EPS_IN);
        if(i==m-1)
            System.out.println(" " + q + " iterations");
        return x0.clone();
    }
}
