import static java.lang.System.out;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

abstract class Criterion {
    public double value;
    public Double oneDimUtility;

    public static double a, b, c;

    protected Criterion(double value) {
        this.value = value;
    }

    public static double calcApproxOneDimUtility(double value) {
        return Criterion.a * value + Criterion.b * Math.pow(value, 2) + c;
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }
}

class K1 extends Criterion {
    public K1(double value) {
        super(value);
    }
}

class K2 extends Criterion {
    public K2(double value) {
        super(value);
    }
}

class UtilityFunction {
    private double a;
    private double b;
    private double c;

    private String name;

    public UtilityFunction(String name) {
        this.name = name;
    }

    private void calcCoefficients(double[] x, double[] y) {
        int n = x.length;
        double S0 = n, S1 = 0, S2 = 0, S3 = 0, S4 = 0;
        double T0 = 0, T1 = 0, T2 = 0;

        for (int i = 0; i < n; i++) {
            double xi = x[i];
            double yi = y[i];
            double xi2 = xi * xi;
            double xi3 = xi2 * xi;
            double xi4 = xi3 * xi;

            S1 += xi;
            S2 += xi2;
            S3 += xi3;
            S4 += xi4;
            T0 += yi;
            T1 += yi * xi;
            T2 += yi * xi2;
        }

        double[][] A = {
                { S0, S1, S2 },
                { S1, S2, S3 },
                { S2, S3, S4 }
        };
        double[] B = { T0, T1, T2 };

        double[] coefs = solveLinearSystem3(A, B);
        this.a = coefs[0];
        this.b = coefs[1];
        this.c = coefs[2];
    }

    private static double[] solveLinearSystem3(double[][] A, double[] B) {
        int n = 3;
        double[][] M = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = B[i];
        }

        for (int k = 0; k < n; k++) {
            int piv = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(M[i][k]) > Math.abs(M[piv][k]))
                    piv = i;
            }
            double[] tmp = M[k];
            M[k] = M[piv];
            M[piv] = tmp;

            double diag = M[k][k];
            for (int j = k; j < n + 1; j++)
                M[k][j] /= diag;
            for (int i = 0; i < n; i++) {
                if (i == k)
                    continue;
                double factor = M[i][k];
                for (int j = k; j < n + 1; j++)
                    M[i][j] -= factor * M[k][j];
            }
        }

        double[] sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = M[i][n];
        return sol;
    }

    public void approximateUtilityFunction(double[] x, double[] y) {
        this.calcCoefficients(x, y);
    }

    public double calcUtility(double value) {
        double utility = this.a * value + this.b * Math.pow(value, 2) + this.c;
        return utility;
    }

    public void outputFunction() {
        out.printf("\nАппроксимация для %s:\n", this.name);
        out.printf("U(%s) = %.6f + %.6f*%s + %.6f*(%s)^2\n", this.name, this.a, this.b, this.name.toLowerCase(), this.c,
                this.name.toLowerCase());
    }
}

class Alternative {
    public String name;

    public K1 k1;
    public K2 k2;

    public Alternative(String name, K1 k1, K2 k2) {
        this.name = name;
        this.k1 = k1;
        this.k2 = k2;
    }

    public double getTwoDimUtility() {
        return k1.oneDimUtility + k2.oneDimUtility;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", this.name, this.k1, this.k2);
    }
}

class IndifferenceCurve {
    public ArrayList<Alternative> alternatives;
    public double twoDimUtility;

    public void sort() {
        this.alternatives.sort(
                (a1, a2) -> {
                    return Double.compare(a1.k1.value, a2.k1.value);
                });
    }

    @Override
    public String toString() {
        return alternatives.toString();
    }
}

class Problem {
    private ArrayList<K1> K1_values;
    private ArrayList<K2> K2_values;

    private ArrayList<Alternative> alternatives;
    private ArrayList<IndifferenceCurve> curves;

    public Problem(double[] K1_scale, double[] K2_scale) {
        setK1Values(K1_scale);
        setK2Values(K2_scale);
        setAlternatives();
        this.curves = new ArrayList<>();
    }

    public ArrayList<IndifferenceCurve> getCurves() {
        return this.curves;
    }

    public double[] getK1_scale() {
        double[] K1_scale = new double[this.K1_values.size()];
        for (int i = 0; i < this.K1_values.size(); i++) {
            K1_scale[i] = this.K1_values.get(i).value;
        }
        return K1_scale;
    }

    public double[] getK2_scale() {
        double[] K2_scale = new double[this.K2_values.size()];
        for (int i = 0; i < this.K2_values.size(); i++) {
            K2_scale[i] = this.K2_values.get(i).value;
        }
        return K2_scale;
    }

    public double[] getK1_utilities() {
        double[] K1_utilities = new double[this.K1_values.size()];
        for (int i = 0; i < this.K1_values.size(); i++) {
            K1_utilities[i] = this.K1_values.get(i).oneDimUtility;
        }
        return K1_utilities;
    }

    public double[] getK2_utilities() {
        double[] K2_utilities = new double[this.K2_values.size()];
        for (int i = 0; i < this.K2_values.size(); i++) {
            K2_utilities[i] = this.K2_values.get(i).oneDimUtility;
        }
        return K2_utilities;
    }

    private Alternative getAlternative(K1 k1, K2 k2) {
        for (Alternative alternative : this.alternatives) {
            if (alternative.k1 == k1 && alternative.k2 == k2) {
                return alternative;
            }
        }
        return null;
    }

    private void setK1Values(double[] scale) {
        ArrayList<K1> K1_values = new ArrayList<>();
        for (double val : scale) {
            K1_values.add(new K1(val));
        }
        this.K1_values = K1_values;
    }

    private void setK2Values(double[] scale) {
        ArrayList<K2> K2_values = new ArrayList<>();
        for (double val : scale) {
            K2_values.add(new K2(val));
        }
        this.K2_values = K2_values;
    }

    private void setAlternatives() {
        ArrayList<Alternative> alternatives = new ArrayList<>();
        int index = 0;
        for (K1 K1_val : this.K1_values) {
            for (K2 K2_val : this.K2_values) {
                alternatives.add(new Alternative("x" + ++index, K1_val, K2_val));
            }
        }
        this.alternatives = alternatives;
    }

    public ArrayList<Alternative> findEquivalentAlternatives(int targetTwoDimUtility, int count) {
        ArrayList<Alternative> equivalentAlternatives = new ArrayList<>();
        for (Alternative alternative : this.alternatives) {
            if (alternative.getTwoDimUtility() == targetTwoDimUtility) {
                equivalentAlternatives.add(alternative);
                if (equivalentAlternatives.size() == count)
                    break;
            }
        }
        return equivalentAlternatives;
    }

    private <C extends Criterion> C findNextCriterionValue(ArrayList<C> c_values) {
        for (C c_value : c_values) {
            if (c_value.oneDimUtility == null) {
                return c_value;
            }
        }
        return null;
    }

    private ArrayList<Alternative> findNextCurveAlternatives(IndifferenceCurve curve) {
        curve.sort();
        ArrayList<Alternative> baseAlternatives = curve.alternatives;
        ArrayList<Alternative> newAlternatives = new ArrayList<>();
        for (int i = 0; i < baseAlternatives.size() - 1; i++) {
            K1 k1 = baseAlternatives.get(i + 1).k1;
            K2 k2 = baseAlternatives.get(i).k2;
            Alternative alternative = this.getAlternative(k1, k2);
            newAlternatives.add(alternative);
        }
        return newAlternatives;
    }

    public void computeIndifferenceCurves() {
        this.K1_values.get(0).oneDimUtility = 0.0;
        this.K2_values.get(0).oneDimUtility = 0.0;
        K1_values.get(1).oneDimUtility = 1.0;
        K2_values.get(1).oneDimUtility = 1.0;

        IndifferenceCurve initialCurve = new IndifferenceCurve();
        initialCurve.alternatives = new ArrayList<>();
        initialCurve.alternatives.add(getAlternative(K1_values.get(1), K2_values.get(0)));
        initialCurve.alternatives.add(getAlternative(K1_values.get(0), K2_values.get(1)));

        this.curves.add(initialCurve);

        IndifferenceCurve curCurve = new IndifferenceCurve();
        curCurve.alternatives = this.findNextCurveAlternatives(initialCurve);
        IndifferenceCurve nextCurve;

        K1 K1_val = this.findNextCriterionValue(K1_values);
        K2 K2_val = this.findNextCriterionValue(K2_values);

        while (K1_val != null && K2_val != null) {
            nextCurve = new IndifferenceCurve();
            curCurve.alternatives.add(getAlternative(K1_val, K2_values.get(0)));
            curCurve.alternatives.add(getAlternative(K1_values.get(0), K2_val));
            K1_val.oneDimUtility = K2_val.oneDimUtility = curCurve.alternatives.get(0).getTwoDimUtility();
            nextCurve.alternatives = this.findNextCurveAlternatives(curCurve);
            this.curves.add(curCurve);
            K1_val = this.findNextCriterionValue(K1_values);
            K2_val = this.findNextCriterionValue(K2_values);
            curCurve = nextCurve;
        }

        while (!curCurve.alternatives.isEmpty()) {
            this.curves.add(curCurve);
            nextCurve = new IndifferenceCurve();
            nextCurve.alternatives = this.findNextCurveAlternatives(curCurve);
            curCurve = nextCurve;
        }
    }

    public double computeScalingCoefficient(Alternative xi, Alternative xj) {
        double k1i = xi.k1.oneDimUtility;
        double k2i = xi.k2.oneDimUtility;
        double k1j = xj.k1.oneDimUtility;
        double k2j = xj.k2.oneDimUtility;
        double scalingCoef = (k2j - k2i) / (k1i - k2i - k1j + k2j);
        return scalingCoef;
    }

    public void outputAlternativesTwoDimUtilities() {
        out.println("Значения полезностей решений:");
        for (int i = 0; i < this.alternatives.size(); i++) {
            out.printf("%s: %f\n", this.alternatives.get(i).toString(), this.alternatives.get(i).getTwoDimUtility());
        }
    }

    public void outputK1OneDimUtilitities() {
        out.println("Значения одномерной полезности для дискретных значений критерия K1:");
        for (K1 k1_val : this.K1_values) {
            out.printf("U(%f) = %.2f\n", k1_val.value, k1_val.oneDimUtility);
        }
    }

    public void outputK2OneDimUtilitities() {
        out.println("Значения одномерной полезности для дискретных значений критерия K2:");
        for (K2 k2_val : this.K2_values) {
            out.printf("U(%f) = %.2f\n", k2_val.value, k2_val.oneDimUtility);
        }
    }
}

// Код позаимствован у ChatGPT с целью построения линий одинаковой полезности
class CurvePlotter extends JPanel {
    private ArrayList<IndifferenceCurve> curves;
    private double maxK1, maxK2;

    public CurvePlotter(ArrayList<IndifferenceCurve> curves, double maxK1, double maxK2) {
        this.curves = curves;
        this.maxK1 = maxK1;
        this.maxK2 = maxK2;
        setPreferredSize(new Dimension(700, 700));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int margin = 60;

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(margin, h - margin, w - margin, h - margin); // X-axis
        g2.drawLine(margin, margin, margin, h - margin); // Y-axis

        // Axis labels
        g2.drawString("K1", w - 40, h - 30);
        g2.drawString("K2", 25, 40);

        // Scaling factors
        double scaleX = (w - 2.0 * margin) / maxK1;
        double scaleY = (h - 2.0 * margin) / maxK2;

        int numTicks = 6; // number of tick marks per axis
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));

        // === DRAW GRID LINES ===
        g2.setColor(new Color(220, 220, 220)); // light gray grid
        for (int i = 0; i <= numTicks; i++) {
            // vertical grid lines (K1)
            double valX = (maxK1 / numTicks) * i;
            int x = margin + (int) (valX * scaleX);
            g2.drawLine(x, h - margin, x, margin);

            // horizontal grid lines (K2)
            double valY = (maxK2 / numTicks) * i;
            int y = h - margin - (int) (valY * scaleY);
            g2.drawLine(margin, y, w - margin, y);
        }

        // === DRAW AXIS TICKS AND VALUES ===
        g2.setColor(Color.DARK_GRAY);

        // X-axis ticks (K1)
        for (int i = 0; i <= numTicks; i++) {
            double val = (maxK1 / numTicks) * i;
            int x = margin + (int) (val * scaleX);
            int y = h - margin;
            g2.drawLine(x, y - 5, x, y + 5);
            String label = String.format("%.3f", val);
            g2.drawString(label, x - 12, y + 20);
        }

        // Y-axis ticks (K2)
        for (int i = 0; i <= numTicks; i++) {
            double val = (maxK2 / numTicks) * i;
            int x = margin;
            int y = h - margin - (int) (val * scaleY);
            g2.drawLine(x - 5, y, x + 5, y);
            String label = String.format("%.3f", val);
            g2.drawString(label, x - 45, y + 5);
        }

        // === DRAW CURVES ===
        Color[] colors = { Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK };
        int colorIndex = 0;

        for (IndifferenceCurve curve : curves) {
            if (curve.alternatives.isEmpty())
                continue;
            g2.setColor(colors[colorIndex % colors.length]);
            colorIndex++;

            int prevX = -1, prevY = -1;
            for (Alternative a : curve.alternatives) {
                int x = margin + (int) (a.k1.value * scaleX);
                int y = h - margin - (int) (a.k2.value * scaleY);

                g2.fillOval(x - 4, y - 4, 8, 8);
                if (prevX != -1)
                    g2.drawLine(prevX, prevY, x, y);

                prevX = x;
                prevY = y;
            }
        }
    }

    public static void showPlot(ArrayList<IndifferenceCurve> curves, double maxK1, double maxK2) {
        JFrame frame = new JFrame("Indifference Curves");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CurvePlotter(curves, maxK1, maxK2));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

public class Lab3 {
    public static void main(String[] args) {

        double[] K1_scale = new double[] {
                0.01, 0.013, 0.02, 0.04
        };

        double[] K2_scale = new double[] {
                0.0125, 0.017, 0.025, 0.05
        };

        Problem p1 = new Problem(K1_scale, K2_scale);
        p1.computeIndifferenceCurves();
        ArrayList<Alternative> equivalentAlternatives = p1.findEquivalentAlternatives(1, 2);
        double j = p1.computeScalingCoefficient(equivalentAlternatives.get(0), equivalentAlternatives.get(1));
        p1.outputAlternativesTwoDimUtilities();
        out.println();
        p1.outputK1OneDimUtilitities();
        out.println();
        p1.outputK2OneDimUtilitities();
        out.println();
        out.printf("Значение коэффициента шкалирования j: %.2f", j);
        out.println();
        CurvePlotter.showPlot(p1.getCurves(), K1_scale[K1_scale.length - 1], K2_scale[K2_scale.length - 1]);

        ArrayList<Alternative> candidates = new ArrayList<>();
        candidates.add(new Alternative("x1", new K1(30.0), new K2(45.0)));
        candidates.add(new Alternative("x2", new K1(50.0), new K2(30.0)));
        candidates.add(new Alternative("x3", new K1(80.0), new K2(20.0)));
        candidates.add(new Alternative("x4", new K1(25.0), new K2(55.0)));

        UtilityFunction K1_function = new UtilityFunction("k1");
        K1_function.approximateUtilityFunction(p1.getK1_scale(), p1.getK1_utilities());
        UtilityFunction K2_function = new UtilityFunction("k2");
        K2_function.approximateUtilityFunction(p1.getK2_scale(), p1.getK2_utilities());

        K1_function.outputFunction();
        K2_function.outputFunction();
        out.println();

        Alternative bestAlternative = null;
        double bestUtility = 0.0;
        for (Alternative alternative : candidates) {
            double currentUtility = K1_function.calcUtility(alternative.k1.value)
                    + K2_function.calcUtility(alternative.k2.value);
            if (currentUtility > bestUtility) {
                bestAlternative = alternative;
                bestUtility = currentUtility;
            }
        }
        out.printf("Решение %s имеет наибольшее аппроксимированное значение двумерной функции полезности %f",
                bestAlternative.toString(), bestUtility);
    }
}