package functions;

public class RLC extends Function {

    public RLC() {
        super(2);
    }

    public double[] getX0() {
        double[] x0 = new double[n];
        x0[0] = 10;
        x0[1] = 10;
        return x0;
    }

    public double[] getFunction(double[] x, double t) {
        double[] f = new double[n];
        f[0] = 0.1 * Math.sin(32 * Math.PI * t) - x[0] - x[1];
        f[1] = 10 * x[0];
        return f;
    }

    public double[][] getJacobian(double[] x, double t) {
        double[][] a = new double[n][n];
        a[0][0] = -1;
        a[0][1] = -1;
        a[1][0] = 10;
        a[1][1] = 0;
        return a;
    }
}
