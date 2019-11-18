package methods;

import Jama.Matrix;
import functions.Function;
import system.solver.Gauss;

public class NewtonSteffensen extends Method {

    public NewtonSteffensen(Function function, double t, int m) {
        super(function, t, m);
    }

    @Override
    public double[] doIteration(double[] Aprev, double i, double[][] M) {
        double[][] M1;
        double[][] M2;
        double[] b = new double[n];
        double[] y0 = new double[n];
        double[] a1 = new double[n];
        double[] a2 = new double[n];
        double[] f;
        double[] mas;
        double[] x0 = new double[n];
        double[] x1 = Aprev.clone();
        int q=0;
        do {
            if(i==m-1)
                ++q;
            for (int j = 0; j < n; j++) {
                x0[j] = x1[j];
            }
            M1 = function.getJacobian(x0, i * h);
            f = function.getFunction(x0, i * h);
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M1[k][t] *= h;
                }
            }
            for (int k = 0; k < n; k++) {
                M1[k][k] = 1 - M1[k][k];
                b[k] = x0[k] - Aprev[k] - h * f[k];
            }
            for (int j = 0; j < n; j++) {
                a1[j] = x0[j] - b[j];
                a2[j] = x0[j] + b[j];
            }
//            Gauss g = new Gauss(copyMatrix(M1), b);
//            mas = g.getX();
            mas = multiplyMatrixVector(copyMatrix(M),b);
            for (int j = 0; j < n; j++) {
                y0[j] = x0[j] - mas[j];
            }
            f = function.getFunction(y0, i * h);
            M2 = copyMatrix(divDifferences(a1, a2, i * h));
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M2[k][t] *= h;
                }
            }
            for (int k = 0; k < n; k++) {
                M2[k][k] = 1 - M2[k][k];
                b[k] = y0[k] - Aprev[k] - h * f[k];
            }
//            g = new Gauss(copyMatrix(M2), b);
//            mas = g.getX();
            mas = multiplyMatrixVector(copyMatrix(M),b);
            for (int j = 0; j < n; j++) {
                x1[j] = y0[j] - mas[j];
            }
        }
        while (mNorm(x0, x1) >= EPS_IN);
//        M = copyMatrix(M2);
        for (int j = 0; j < n; j++) {
            M[j] = M2[j].clone();
        }
        if(i==m-1)
            System.out.println(" " + q + " iterations");
        return x0.clone();
    }
}
