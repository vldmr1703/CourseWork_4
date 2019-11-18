package methods;

import functions.Function;
import system.solver.Gauss;

public class NewtonTwoStepModified extends Method {

    public NewtonTwoStepModified(Function function, double t, int m) {
        super(function, t, m);
    }

    @Override
    public double[] doIteration(double[] Aprev, double i, double[][] M) {
        double[] f;
        double[] b = new double[n];
        double[] xy = new double[n];
        double[] y0;
        double[] mas;
        double[][] M1;
        double[] x0 = new double[n];
        double[] x1 = Aprev.clone();
        y0 = function.getFunction(x1, i * h);
        int q=0;
        do {
            if(i==m-1)
            ++q;
            for (int j = 0; j < n; j++) {
                x0[j] = x1[j];
                xy[j] = (x0[j] + y0[j]) / 2;
            }
            M1 = function.getJacobian(x0, i * h);
            for (int j = 0; j < n; j++) {
                M[j] = M1[j].clone();
            }
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M[k][t] *= h;
                }
                M[k][k] = 1 - M[k][k];
            }
            f = function.getFunction(x0, i * h);
            for (int k = 0; k < n; k++) {
                b[k] = x0[k] - Aprev[k] - h * f[k];
            }
//            Gauss g = new Gauss(copyMatrix(M),b);
//            mas = g.getX();
            mas = multiplyMatrixVector(copyMatrix(M),b);
            for (int j = 0; j < n; j++) {
                y0[j] = x0[j] - mas[j];
            }
            for (int j = 0; j < n; j++) {
                xy[j] = (x0[j] + y0[j]) / 2;
            }
            M1 = function.getJacobian(xy, i * h);
            for (int j = 0; j < n; j++) {
                M[j] = M1[j].clone();
            }
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M[k][t] *= h;
                }
                M[k][k] = 1 - M[k][k];
            }
            f = function.getFunction(xy, i * h);
            for (int k = 0; k < n; k++) {
                b[k] = xy[k] - Aprev[k] - h * f[k];
            }
//            g = new Gauss(copyMatrix(M),b);
//            mas = g.getX();
            mas = multiplyMatrixVector(copyMatrix(M),b);
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
