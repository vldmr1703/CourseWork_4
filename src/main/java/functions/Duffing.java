package functions;

public class Duffing extends Function {

    public Duffing() {
        super(2);
    }

    public double[] getX0() {
        double[] x0 = new double[n];
//        x0[0] = 0.027;
//        x0[1] = 1.1;

        x0[0] = 0.027;
        x0[1] = 1.1;
        return x0;
    }

    public double[] getFunction(double[] x, double t) {
        double[] f = new double[n];
        f[0] = x[1];
        f[1] = -0.2 * x[1] - Math.pow(x[0], 3) + 0.3 * Math.cos(t);
        return f;
    }

    public double[][] getJacobian(double[] x, double t) {
        double[][] a = new double[n][n];
        a[0][0] = 0;
        a[0][1] = 1;
        a[1][0] = -3 * Math.pow(x[0], 2);
        a[1][1] = -0.2;
        return a;
    }
}
