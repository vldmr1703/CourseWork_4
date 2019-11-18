import Jama.Matrix;
import functions.Duffing;
import functions.RLC;
import methods.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

public class App {
    public static void main(String[] args) {

//        Method newton = new NewtonTwoStep(new RLC(), 2 * Math.PI, 1000);
        Method[] methods = new Method[]{
                new Newton(new Duffing(), 20 * Math.PI, 1000),
//                new Newton(new Duffing(), 2 * Math.PI, 1000),
                new NewtonTwoStep(new Duffing(), 20 * Math.PI, 1000),
                new NewtonTwoStepModified(new Duffing(), 20 * Math.PI, 1000),
                new Steffensen(new Duffing(), 20 * Math.PI, 1000),
                new SteffensenTwoStep(new Duffing(), 20 * Math.PI, 1000),
                new NewtonSteffensen(new Duffing(), 20 * Math.PI, 1000),
                new SteffensenNewton(new Duffing(), 20 * Math.PI, 1000)
        };
        for(Method method:methods)
        {
            double[][] A = method.calculate();
            method.print(A[0]);
            XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
            XYSeries[] series = new XYSeries[A[0].length];
            for (int j = 0; j < A[0].length; j++) {
                series[j] = new XYSeries("x" + (j + 1));
                for (int i = 0; i < A.length; i++) {
                    series[j].add(i * method.h, A[i][j]);
                }
            }
            for (int i = 0; i < A[0].length; i++) {
                xySeriesCollection.addSeries(series[i]);
            }
//        double max=0;
//        XYSeries series = new XYSeries("x");
//            for (int i = 0; i < A.length; i++) {
//                series.add(A[i][0], A[i][1]);
//                if(A[i][0]>max)
//                    max=A[i][0];
//            }
//        xySeriesCollection.addSeries(series);
            XYDataset dataset = xySeriesCollection;
            DrawGraph chart = new DrawGraph(
                    "",
                    "", dataset, method.T);

            chart.pack();
            chart.setVisible(true);
        }
    }


}

class DrawGraph extends ApplicationFrame {

    public DrawGraph(String applicationTitle, String chartTitle, XYDataset dataset, Double T) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Time", "Function value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        XYPlot xyPlot = lineChart.getXYPlot();
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setRange(0.00, T);
        domain.setTickUnit(new NumberTickUnit(T / 10));
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 500));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        int n = 10;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double fault = 0;
        return dataset;
    }
}

class Plot2D {
    Random seed = new Random();
    final int SIZE = 8;
    final double MAX = 10.0;
    PlotPanel plotPanel;

    private double[] getData(double min, double max) {
        double[] d = new double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            d[i] = min + seed.nextDouble() * (max - min);
            //System.out.printf("%5.1f ", d[i]);
        }
        //System.out.println();
        return d;
    }

    private JPanel getContent(Method method) {
//        double[] x = getData(-MAX, MAX);
        double[][] A = method.calculate();
//        double[] y = getData(-MAX, MAX);
        double[] y = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            y[i] = (i + 1) * method.h;
        }
//        double[] sum = new double[A.length];
        System.out.println("solution for " + method + " x0 = " + A[0][0] + " " + A[0][1]);
//        plotPanel = new PlotPanel(new Matrix(A).transpose().getArray()[0], new Matrix(new Matrix(A).transpose().getArray()[1], 1).transpose().getArray());
//        for (int i = 0; i < A.length; i++) {
//            double s = 0;
//            for (int j = 0; j < A[0].length; j++) {
//                s +=A[i][j];
//            }
//            sum[i] = s;
//        }
//         plotPanel = new PlotPanel(y, new Matrix(sum, 1).transpose().getArray());

        plotPanel = new PlotPanel(y, new Matrix(A).getArray());
        return plotPanel;
    }

    private JPanel getUIPanel() {
        JButton button = new JButton("change data");
        JRadioButton[] rbs = new JRadioButton[5];
        final ButtonGroup group = new ButtonGroup();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(
                        group.getSelection().getActionCommand());
                double xMin = -MAX, xMax = MAX, yMin = -MAX, yMax = MAX;
                switch (index) {
                    case 0:
                        xMax = -5;
                        break;
                    case 1:
                        xMin = 5;
                        break;
                    case 2:
                        break;
                    case 3:
                        yMax = -5;
                        break;
                    case 4:
                        yMin = 5;
                }
                double[] x = getData(xMin, xMax);
                double[] y = getData(yMin, yMax);
//                plotPanel.setData(x, y);
            }
        });
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        String minus = "<html>\u2013";
        String[] ids = {
                "<html>\u2013x", "+x", "<html>+/\u2013x&y", "<html>\u2013y", "+y"
        };
        for (int i = 0; i < rbs.length; i++) {
            rbs[i] = new JRadioButton(ids[i], i == 2);
            rbs[i].setActionCommand(String.valueOf(i));
            group.add(rbs[i]);
            panel.add(rbs[i], gbc);
        }
        panel.setBorder(BorderFactory.createEtchedBorder());
        gbc.weightx = 1.0;
        panel.add(button, gbc);
        return panel;
    }

    public static void main(String[] args) {
//        NewtonTwoStep newton1 = new NewtonTwoStep(new Duffing(), 2 * Math.PI, 1000);
//        Newton newton2 = new Newton(new Duffing(), 2 * Math.PI, 1000);
//        Steffensen newton3 = new Steffensen(new Duffing(), 2 * Math.PI, 1000);
////        Steffensen newton = new Steffensen(new NerveMembrane(), Math.PI, 1000);
//
//        SteffensenNewton newton4 = new SteffensenNewton(new Duffing(), 2 * Math.PI, 1000);
        Method[] methods = new Method[]{
                new Newton(new RLC(), 2 * Math.PI, 1000),
//                new Newton(new Duffing(), 2 * Math.PI, 1000),
//                new NewtonTwoStep(new Duffing(), 2 * Math.PI, 1000),
//                new Steffensen(new Duffing(), 2 * Math.PI, 1000),
//                new NewtonSteffensen(new Duffing(), 2 * Math.PI, 1000)
        };
//        Newton newton = new Newton(new RLC(), Math.PI, 10000);
//        Newton newton = new Newton(new DC(), 1.0/60, 1000);
        for (Method method : methods) {
            Plot2D test = new Plot2D();
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(test.getContent(method));
            f.add(test.getUIPanel(), "Last");
            f.setSize(400, 400);
            f.setLocation(50, 50);
            f.setVisible(true);
        }
    }
}

class PlotPanel extends JPanel {
    double[] x;
    double[][] y;
    double xMin;
    double xMax;
    double yMin;
    double yMax;
    final int PAD = 20;
    final boolean DEBUG = false;
    boolean firstTime;  // Set at end of setData method.

    public PlotPanel(double[] x, double[][] y) {
        setData(x, y);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        double xScale = (w - 2 * PAD) / (xMax - xMin);
        double yScale = (h - 2 * PAD) / (yMax - yMin);
        if (firstTime)
            System.out.printf("xScale = %.1f  yScale = %.1f%n",
                    xScale, yScale);
        Point2D.Double origin = new Point2D.Double(); // Axes origin.
        Point2D.Double offset = new Point2D.Double(); // Locate data.
        if (xMax < 0) {
            origin.x = w - PAD;
            offset.x = origin.x - xScale * xMax;
        } else if (xMin < 0) {
            origin.x = PAD - xScale * xMin;
            offset.x = origin.x;
        } else {
            origin.x = PAD;
            offset.x = PAD - xScale * xMin;
        }
        if (yMax < 0) {
//            origin.y = h - PAD;
//            offset.y = origin.y - yScale * yMax;
            origin.y = PAD;
            offset.y = PAD - yScale * yMin;
        } else if (yMin < 0) {
            origin.y = PAD - yScale * yMin;
            offset.y = origin.y;
//            origin.y = h - PAD - yScale * yMin;
//            offset.y = origin.y;
        } else {
//            origin.y = PAD;
//            offset.y = PAD - yScale * yMin;
            origin.y = h - PAD;
            offset.y = origin.y - yScale * yMax;
        }
        if (firstTime) {
            System.out.printf("origin = [%6.1f, %6.1f]%n", origin.x, origin.y);
            System.out.printf("offset = [%6.1f, %6.1f]%n", offset.x, offset.y);
        }

        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, origin.y, w - PAD, origin.y));
        // Draw ordinate.
        g2.draw(new Line2D.Double(origin.x, PAD, origin.x, h - PAD));
        g2.setPaint(Color.red);
        // Mark origin.
        g2.fill(new Ellipse2D.Double(origin.x - 2, origin.y - 2, 4, 4));

        g2.fill(new Ellipse2D.Double(offset.x + xScale * x[0] - 2, h - offset.y - yScale * y[0][0] + 2, 8, 8));

//        g2.setColor(Color.orange);
//        g2.fill(new Ellipse2D.Double(offset.x + xScale * x[x.length - 1] - 2, offset.y + yScale * y[x.length - 1] - 2, 8, 8));
        // Plot data.

//        g2.fill(new Ellipse2D.Double(x1 - 2, h - y1 + 2, 4, 4));
        g2.setPaint(Color.blue);
        System.out.println("==================");
        for (int j = 0; j < y[0].length; j++) {
            for (int i = 0; i < x.length - 1; i++) {
                double x1 = offset.x + xScale * x[i];
                double y1 = offset.y + yScale * y[i][j];
                double x2 = offset.x + xScale * x[i + 1];
                double y2 = offset.y + yScale * y[i + 1][j];
                if (firstTime)
                    System.out.printf("i = %d  x1 = %6.1f  y1 = %.1f%n", i, x1, y1);
                g2.fill(new Ellipse2D.Double(x1 - 2, h - y1 + 2, 4, 4));
                g2.draw(new Line2D.Double(x1 - 1, h - y1 + 1, x2 - 1, h - y2 + 2));
//            g2.drawString(String.valueOf(i), (float) x1 + 3, (float) y1 - 3);
            }
        }

        // Draw extreme data values.
        g2.setPaint(Color.black);
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        String s = String.format("%.1f", xMin);
        float width = (float) font.getStringBounds(s, frc).getWidth();
        double x = offset.x + xScale * xMin;
        g2.drawString(s, (float) x, (float) origin.y + lm.getAscent());
        s = String.format("%.1f", xMax);
        width = (float) font.getStringBounds(s, frc).getWidth();
        x = offset.x + xScale * xMax;
        g2.drawString(s, (float) x - width, (float) origin.y + lm.getAscent());
        s = String.format("%.1f", yMin);
        width = (float) font.getStringBounds(s, frc).getWidth();
        double y = h - offset.y - yScale * yMin;
        g2.drawString(s, (float) origin.x + 1, (float) y + lm.getAscent());
        s = String.format("%.1f", yMax);
        width = (float) font.getStringBounds(s, frc).getWidth();
        y = h - offset.y - yScale * yMax;
        g2.drawString(s, (float) origin.x + 1, (float) y);
        if (firstTime)
            System.out.println("------------------------------");
        firstTime = false;
    }

    public void setData(double[] x, double[][] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("x and y data arrays " +
                    "must be same length.");
        }
        this.x = x;
        this.y = y;
        double[] xVals = getExtremeValues(x);
        xMin = xVals[0];
        xMax = xVals[1];
        if (DEBUG)
            System.out.printf("xMin = %5.1f  xMax = %5.1f%n", xMin, xMax);
        double[] yVals = getExtremeValues(y);
        yMin = yVals[0];
        yMax = yVals[1];
        if (DEBUG)
            System.out.printf("yMin = %5.1f  yMax = %5.1f%n", yMin, yMax);
        firstTime = DEBUG;
        repaint();
    }

    private double[] getExtremeValues(double[] d) {
        double min = Double.MAX_VALUE;
        double max = -min;
        for (int i = 0; i < d.length; i++) {
            if (d[i] < min) {
                min = d[i];
            }
            if (d[i] > max) {
                max = d[i];
            }
        }
        return new double[]{min, max};
    }

    private double[] getExtremeValues(double[][] d) {
        double min = Double.MAX_VALUE;
        double max = -min;
        for (int i = 0; i < d.length; i++)
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] < min) {
                    min = d[i][j];
                }
                if (d[i][j] > max) {
                    max = d[i][j];
                }
            }
        return new double[]{min, max};
    }
}
