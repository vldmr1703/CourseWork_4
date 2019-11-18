package methods;

import Jama.Matrix;
import functions.Function;
import system.solver.Gauss;

public class SteffensenNewton extends Method {

    public SteffensenNewton(Function function, double t, int m) {
        super(function, t, m);
    }

    @Override
    public double[] doIteration(double[] Aprev, double i, double[][] M) {
        double[] b = new double[n];
        double[] y0 = new double[n];
        double[] a1 = new double[n];
        double[] a2 = new double[n];
        double[] f;
        double[] mas;
        double[] x0 = new double[n];
        double[] x1 = Aprev.clone();
        double[][] M1;
        int q=0;
        do {
            if(i==m-1)
                ++q;
            for (int j = 0; j < n; j++) {
                x0[j] = x1[j];
            }
            f = function.getFunction(x0, i * h);
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M[k][t] *= h;
                }
            }
            for (int k = 0; k < n; k++) {
                b[k] = x0[k] - Aprev[k] - h * f[k];
            }
            for (int j = 0; j < n; j++) {
                a1[j] = x0[j] - b[j];
                a2[j] = x0[j] + b[j];
            }
            M1 = copyMatrix(divDifferences(a1, a2, i * h));
            mas = multiplyMatrixVector(M1, b);
            for (int j = 0; j < n; j++) {
                y0[j] = x0[j] - mas[j];
            }
            f = function.getFunction(y0, i * h);
            M1 = function.getJacobian(y0, i * h);
            for (int j = 0; j < n; j++) {
                M[j] = M1[j].clone();
            }
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M[k][t] *= h;
                }
                M[k][k] = 1 - M[k][k];
            }
            for (int k = 0; k < n; k++) {
                b[k] = y0[k] - Aprev[k] - h * f[k];
            }
            mas = multiplyMatrixVector(M, b);
            for (int j = 0; j < n; j++) {
                x1[j] = y0[j] - mas[j];
            }
        }
        while (mNorm(x0, x1) >= EPS_IN);
        if(i==m-1)
            System.out.println(" " + q + " iterations");
        return x0.clone();
    }
}
