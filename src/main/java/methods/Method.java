package methods;

import Jama.Matrix;
import functions.Function;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import system.solver.Gauss;

public abstract class Method {

    protected Function function;
    private static final double EPS = 0.01;
    protected int n;
    protected static final double EPS_IN = 0.00001;
    protected static final double EPS_OUT = 0.00000001;
    public double T;
    protected int m;
    public double h;
    public double[][] A;

    public Method(Function function, double t, int m) {
        this.function = function;
        T = t;
        this.m = m;
        this.h = T / m;
        this.n = function.getN();
    }

    public double[][] calculate() {
        System.out.println(this);
        Gauss gauss;
        double[] x0 = function.getX0();
        double[] x1 = new double[n];
        for (int i = 0; i < n; i++) {
            x1[i] = x0[i];
        }
        double[][] A = new double[m][n];//x^k(tj)
        double[][] M = new double[n][n];//I-F(x^(k, l)(tj), tj)
        double[][] Fi;
        int p = 0;
        double[] xx=new double[n];
        do {
            ++p;
            xx=x0.clone();
//            System.out.println(p + " iteration");
            A[0] = x1.clone();
            Fi = null;

            System.out.print(p + " ");
            for (int i = 1; i < m; i++) {
//                x1 = A[i - 1].clone();
                x0 = doIteration(A[i - 1], i, M);
                Fi = multiplyFi(Fi, M);
                A[i] = x0.clone();
            }
//            System.out.print("x0 = ");
//            print(A[0]);
            gauss = new Gauss(makeA(Fi), makeB(Fi, A[0], A[m - 1]));
            x1 = gauss.getX();
            for (int i = 0; i < n; i++) {
                x1[i] = A[0][i] - x1[i];

            }
//            System.out.print("x1 = ");
//            print(x1);
//            for (int i = 0; i < n; i++){
//                System.out.print("& $" + String.format("%.14f", x1[i]) + "$ ");
//            }
//            System.out.print("& $" + error(A[0], x1) + "$ ");
//            System.out.println("\\\\");
//            System.out.println("\\hline");
//            System.out.println("poh = " + error(A[0], x1));
//            dataset.addValue((Number) error(A[0], x1), "error", p);
        }
        while (mNorm(A[0], x1) >= EPS_OUT || mNorm(A[0], A[m - 1]) >= 0.000001);

        System.out.println("poh = " + error(A[0], xx));
        this.A = A;
        System.out.println("-------------------------------------");
        return A;
    }

    public void printF(double[] x){
        for (int i = 1; i < x.length; i++){
            System.out.print("& $" + x[i] + "$ ");
            System.out.println("\\");
            System.out.println("\\hline");
        }
    }

    public abstract double[] doIteration(double[] Aprev, double i, double[][] M);

    public void print(double[] x) {
        for (double xi : x) {
            System.out.print(xi + " ");
        }
        System.out.println();
    }

    protected double mNorm(double[] x0, double[] x1) {
        double s = 0;
        for (int i = 0; i < x0.length; i++) {
            s += Math.pow(x0[i] - x1[i], 2);
        }
        return Math.sqrt(s);
    }

    protected double error(double[] x0, double[] x1) {
        double max = Math.abs(x0[0] - x1[0]);
        for (int i = 1; i < n; i++) {
            double abs = Math.abs(x0[i] - x1[i]);
            if (abs > max)
                max = abs;
        }
        return max;
    }

    protected double[][] divDifferences(double[] x0, double[] x1, double t) {
        int n = x0.length;
        double[][] jac = new double[n][n];
        double[] F0;
        double[] F1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[] mas0 = new double[n];
                for (int k = 0; k < j + 1; k++) {
                    mas0[k] = x0[k];
                }
                for (int k = j + 1; k < n; k++) {
                    mas0[k] = x1[k];
                }
                double[] mas1 = new double[n];
                for (int k = 0; k < j; k++)
                    mas1[k] = x0[k];
                for (int k = j; k < n; k++)
                    mas1[k] = x1[k];
                F0 = function.getFunction(mas0, t);
                F1 = function.getFunction(mas1, t);
                double r = (x0[j] - x1[j]);
                if (r == 0)
                    r = 1e-8;
                jac[i][j] = (F0[i] - F1[i]) / (r);
            }
        }
        return jac;
    }

    protected double[][] makeA(double[][] Fi) {
//        double[][] A = copyMatrix(Fi);
//        for (int i = 0; i < n; i++) {
//            A[i][i] -= 1;
//        }
//        return A;
        double[][] A = new Matrix(Fi).inverse().getArray();
        for (int i = 0; i < n; i++) {
            A[i][i] = 1 - A[i][i];
        }
        return A;
    }

    protected double[] makeB(double[][] Fi, double[] x0, double[] xm) {
//        double[] b = multiplyMatrixVector(Fi, xm);
//        for (int i = 0; i < n; i++) {
//            b[i] -= x0[i];
//        }
//        return b;
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = x0[i] - xm[i];
        }
        return b;
    }

    public double[] multiplyMatrixVector(double[][] A, double[] b) {
        double[] mas = new double[n];
        for (int k = 0; k < n; k++) {
            double s = 0;
            for (int t = 0; t < n; t++) {
                s += A[k][t] * b[t];
            }
            mas[k] = s;
        }
//        return mas;
        return new Matrix(A).times(new Matrix(b, 1).transpose()).transpose().getArray()[0];
    }

    protected double[][] multiplyFi(double[][] Fi, double[][] M) {
        double[][] res = new double[n][n];
        if (Fi == null) {
//            res = copyMatrix(M);
            return copyMatrix(M);
//            return new Matrix(function.getJacobian(function.getX0(), 0)).inverse().getArray();
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        res[i][j] += Fi[i][k] * M[k][j];
                    }
                }
            }
        }
//        return res;
        return new Matrix(Fi).times(new Matrix(M)).getArray();
//        return new Matrix(Fi).inverse().times(new Matrix(Fi)).getArray();
    }

    public void print(double[][] A) {
        for (int i = 0; i < A.length; i++) {
            System.out.println();
            for (int j = 0; j < A[0].length; j++) {
                System.out.print(A[i][j] + " ");
            }
        }
        System.out.println();
    }

    protected double[][] copyMatrix(double[][] a) {
        double[][] m = new double[a.length][];
        for (int i = 0; i < a.length; i++) {
            m[i] = a[i].clone();
        }
        return m;
    }
}
