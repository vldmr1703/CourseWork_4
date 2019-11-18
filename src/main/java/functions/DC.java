package functions;

public class DC extends Function {

    public DC() {
        super(4);
    }

    public double[] getX0() {
        double[] x0 = new double[n];
        x0[0]=0.001;
        x0[1]=0.001;
        x0[2]=0.001;
        x0[3]=0.001;
        return x0;
    }

    public double[] getFunction(double[] x, double t) {
        double[] f = new double[n];
        f[0] = Math.pow(10, 6) * 0.2 * (-x[0] - x[1] + 10 * Math.sin(120 * Math.PI * t)) - Math.exp(40 * x[0]) + 1;
        f[1] = Math.pow(10, 3) * (0.2 * (-x[0] - x[1] + 10 * Math.sin(120 * Math.PI * t)) - x[2]);
        f[2] = 10 * (x[1] - x[3]);
        f[3] = Math.pow(10, 3) * x[2] - x[3];
        return f;
    }

    public double[][] getJacobian(double[] x, double t) {
        double[][] a = new double[n][n];
        a[0][0] = (-Math.pow(10, 6) / 5) - (40 * Math.exp(40 * x[0]));
        a[0][1] = -Math.pow(10, 6) / 5;
//        a[0][2] = 0;
//        a[0][3] = 0;
        a[1][0] = -Math.pow(10, 3) / 5;
        a[1][1] = -Math.pow(10, 3) / 5;
        a[1][2] = -Math.pow(10, 3);
//        a[1][3]=0;
//        a[2][0] = 0;
        a[2][1] = 10;
//        a[2][2]=0;
        a[2][3] = -10;
//        a[3][0]=0;
        a[3][2] = Math.pow(10, 3);
        a[3][3] = -1;
        return a;
    }
}
