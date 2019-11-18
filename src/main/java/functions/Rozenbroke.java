//package functions;
//
//import functions.Function;
//
//public class Rozenbroke extends Function {
//
//    public Rozenbroke(int n) {
//        super(n);
//    }
//
//    public double[] getX0() {
//        double[] x0 = new double[n];
//        for (int i = 0; i < n; i += 2) {
//            x0[i] = -1.2;
//            x0[i + 1] = 1;
//        }
//        return x0;
//    }
//
//    public double[] getFunction(double[] x) {
//        double[] f = new double[n];
//        for (int i = 0; i < n; i++) {
//            if (i % 2 != 0) {
//                f[i] = 1 - x[i - 1];
//            } else {
//                f[i] = 10 * (x[i + 1] - Math.pow(x[i], 2));
//            }
//        }
//        return f;
//    }
//
//    public double[][] getJacobian(double[] x0) {
//        double[][] jac = new double[n][n];
//        for (int i = 0; i < n; i++) {
//            if (i % 2 != 0)
//                jac[i][i - 1] = -1;
//            else {
//                jac[i][i + 1] = 10;
//                jac[i][i] = -20 * x0[i];
//            }
//        }
//        //for (int i = 0; i < n; i++)
//        //{
//        //    textBox1.Text += "\r\n";
//        //    for (int j = 0; j < n; j++)
//        //        textBox1.Text += Jac[i, j] + "\t\t";
//        //}
//        return jac;
//    }
//}
