package methods;

import Jama.Matrix;
import functions.Function;
import org.jfree.data.category.DefaultCategoryDataset;
import system.solver.Gauss;

public class Newton extends Method {

    public Newton(Function function, double T, int m) {
        super(function, T, m);
    }

    public double[] doIteration(double[] Aprev, double i, double[][] M) {
        double[] f;
        double[] b = new double[n];
        double[] mas;
        double[][] M1;
        int q=0;
        double[] x0 = new double[n];
        double[] x1 = Aprev.clone();
        do {
            if(i==m-1)
                ++q;
            for (int j = 0; j < n; j++) {
                x0[j] = x1[j];
            }
            f = function.getFunction(x0, i * h);
            M1 = function.getJacobian(x0, i * h);
            for (int j = 0; j < n; j++) {
                M[j] = M1[j].clone();
            }
            for (int k = 0; k < n; k++) {
                for (int t = 0; t < n; t++) {
                    M[k][t] *= h;
                }
            }
            for (int k = 0; k < n; k++) {
                M[k][k] = 1 - M[k][k];
                b[k] = x0[k] - Aprev[k] - h * f[k];
            }
                    mas = multiplyMatrixVector(M, b);
//            Gauss g = new Gauss(copyMatrix(M), b);
//            mas = g.getX();
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
