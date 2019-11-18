package functions;

public class NerveMembrane extends Function {

    public NerveMembrane() {
        super(2);
    }

    public double[] getX0() {
        double[] x0 = new double[n];
        x0[0] = 3;
        x0[1] = 1.5;
        return x0;
    }

    public double[] getFunction(double[] x, double t) {
        double[] f = new double[n];
        f[0] = 3 * x[0] - Math.pow(x[0], 3) + 3 * x[1] - 3;
        f[1] = -(x[0] + 0.8 * x[1] - 0.7) / 3;
        return f;
    }

    public double[][] getJacobian(double[] x, double t) {
        double[][] a = new double[n][n];
        a[0][0] = 3 - 3 * Math.pow(x[0], 2);
        a[0][1] = 3;
        a[1][0] = -1.0 / 3;
        a[1][1] = -4.0 / 15;
        return a;
    }
}
