import static java.lang.System.out;

import java.lang.reflect.Array;
import java.util.ArrayList;

abstract class Criterion<T extends Number, U extends Number> {
    public T value;
    public U oneDimUtility;

    protected Criterion(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }
}

class K1 extends Criterion<Double, Double> {
    public K1(double value) {
        super(value);
    }
}

class K2 extends Criterion<Double, Double> {
    public K2(double value) {
        super(value);
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
        return String.format("(%s, %s)", this.k1, this.k2);
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

public class Lab3 {
    private static ArrayList<K1> getK1Values(double[] scale) {
        ArrayList<K1> K1_values = new ArrayList<>();
        for (double val : scale) {
            K1_values.add(new K1(val));
        }
        return K1_values;
    }

    private static ArrayList<K2> getK2Values(double[] scale) {
        ArrayList<K2> K2_values = new ArrayList<>();
        for (double val : scale) {
            K2_values.add(new K2(val));
        }
        return K2_values;
    }

    private static ArrayList<Alternative> getAlternatives(ArrayList<K1> K1_values, ArrayList<K2> K2_values) {
        ArrayList<Alternative> alternatives = new ArrayList<>();
        int index = 0;
        for (K1 K1_val : K1_values) {
            for (K2 K2_val : K2_values) {
                alternatives.add(new Alternative("x" + ++index, K1_val, K2_val));
            }
        }
        return alternatives;
    }

    private static <C extends Criterion<Double, Double>> C getNextCriterionValue(ArrayList<C> c_values) {
        for (C c_value : c_values) {
            if (c_value.oneDimUtility == null) {
                return c_value;
            }
        }
        return null;
    }

    private static Alternative getAlternative(ArrayList<Alternative> alternatives, K1 k1, K2 k2) {
        for (Alternative alternative : alternatives) {
            if (alternative.k1 == k1 && alternative.k2 == k2) {
                return alternative;
            }
        }
        return null;
    }

    private static ArrayList<Alternative> findNextCurveAlternatives(ArrayList<Alternative> alternatives,
            IndifferenceCurve curve) {
        curve.sort();
        ArrayList<Alternative> baseAlternatives = curve.alternatives;
        ArrayList<Alternative> newAlternatives = new ArrayList<>();
        for (int i = 0; i < baseAlternatives.size() - 1; i++) {
            K1 k1 = baseAlternatives.get(i + 1).k1;
            K2 k2 = baseAlternatives.get(i).k2;
            Alternative alternative = getAlternative(alternatives, k1, k2);
            newAlternatives.add(alternative);
        }
        return newAlternatives;
    }

    private static ArrayList<IndifferenceCurve> getIndifferenceCurves(ArrayList<K1> K1_values, ArrayList<K2> K2_values,
            ArrayList<Alternative> alternatives) {
        K1_values.get(0).oneDimUtility = 0.0;
        K2_values.get(0).oneDimUtility = 0.0;
        K1_values.get(1).oneDimUtility = 1.0;
        K2_values.get(1).oneDimUtility = 1.0;

        IndifferenceCurve initialCurve = new IndifferenceCurve();
        initialCurve.alternatives = new ArrayList<>();
        initialCurve.alternatives.add(getAlternative(alternatives, K1_values.get(1), K2_values.get(0)));
        initialCurve.alternatives.add(getAlternative(alternatives, K1_values.get(0), K2_values.get(1)));

        ArrayList<IndifferenceCurve> curves = new ArrayList<>();
        curves.add(initialCurve);

        IndifferenceCurve curCurve = new IndifferenceCurve();
        curCurve.alternatives = findNextCurveAlternatives(alternatives, initialCurve);
        IndifferenceCurve nextCurve;

        K1 K1_val = getNextCriterionValue(K1_values);
        K2 K2_val = getNextCriterionValue(K2_values);

        while (K1_val != null && K2_val != null) {
            nextCurve = new IndifferenceCurve();
            curCurve.alternatives.add(getAlternative(alternatives, K1_val, K2_values.get(0)));
            curCurve.alternatives.add(getAlternative(alternatives, K1_values.get(0), K2_val));
            K1_val.oneDimUtility = K2_val.oneDimUtility = curCurve.alternatives.get(0).getTwoDimUtility();
            nextCurve.alternatives = findNextCurveAlternatives(alternatives, curCurve);
            curves.add(curCurve);
            K1_val = getNextCriterionValue(K1_values);
            K2_val = getNextCriterionValue(K2_values);
            curCurve = nextCurve;
        }

        while (!curCurve.alternatives.isEmpty()) {
            curves.add(curCurve);
            nextCurve = new IndifferenceCurve();
            nextCurve.alternatives = findNextCurveAlternatives(alternatives, curCurve);
            curCurve = nextCurve;
        }

        return curves;
    }

    public static void outputAlternatives(ArrayList<Alternative> alternatives) {
        out.println("Значения полезностей решений:");
        for (int i = 0; i < alternatives.size(); i++) {
            out.printf("%s (%.4f, %.4f): %f\n", alternatives.get(i).name, alternatives.get(i).k1.value, alternatives.get(i).k2.value,
                    alternatives.get(i).getTwoDimUtility());
        }
    }

    public static <C extends Criterion<Double, Double>> void outputCriterionOneDimUtilities(ArrayList<C> c_values) {
        out.println("Значения одномерной полезности для дискретных значений критерия:");
        for (C c_value : c_values) {
            out.printf("U(%f) = %.2f\n", c_value.value, c_value.oneDimUtility);
        }
    }

    public static void main(String[] args) {

        double[] K1_scale = new double[] {
                0.01, 0.013, 0.02, 0.04
        };

        double[] K2_scale = new double[] {
                0.0125, 0.017, 0.025, 0.05
        };

        ArrayList<K1> K1_values = getK1Values(K1_scale);
        ArrayList<K2> K2_values = getK2Values(K2_scale);

        ArrayList<Alternative> alternatives = getAlternatives(K1_values, K2_values);

        ArrayList<IndifferenceCurve> curves = getIndifferenceCurves(K1_values, K2_values, alternatives);

        outputCriterionOneDimUtilities(K1_values);
        out.println();
        outputCriterionOneDimUtilities(K2_values);
        out.println();
        out.println(curves.toString());
        out.println();
        outputAlternatives(alternatives);
    }
}