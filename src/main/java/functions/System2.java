//package functions;
//
//public class System2 extends Function {
//
//    public System2() {
//        super(2);
//    }
//
//    public double[] getX0() {
//        return new double[]{1.2, 1.1};
//    }
//
//    public double[] getFunction(double[] x) {
//        double[] F = new double[n];
//        F[0] = x[0] * x[0] - 2 * x[1] * x[1] - x[0] * x[1] + 2 * x[0] - x[1] + 1;
//        F[1] = 2 * x[0] * x[0] - x[1] * x[1] + x[0] * x[1] + 3 * x[1] - 5;
//        return F;
//    }
//
//    public double[][] getJacobian(double[] x) {
//        double[][] jac = new double[n][n];
//        jac[0][0] = 2 * x[0] - x[1] + 2;
//        jac[0][1] = -2 * x[1] - x[0] - 1;
//        jac[1][0] = 2 * x[0] + x[1];
//        jac[1][1] = -2 * x[1] + x[0] + 3;
//        return jac;
//    }
//}
