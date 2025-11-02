package decisions_theory;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import static java.lang.System.out;

class Alternative implements Comparable<Alternative> {
    private String name;
    private final int f1;
    private final int f2;

    public static final Comparator<Alternative> F1_COMPARATOR = Comparator.comparingInt(Alternative::getF1);
    public static final Comparator<Alternative> F2_COMPARATOR = Comparator.comparingInt(Alternative::getF2);

    public Alternative(String name, int f1, int f2) {
        this.name = name;
        this.f1 = f1;
        this.f2 = f2;
    }

    public String getName() {
        return this.name;
    }

    public int getF1() {
        return this.f1;
    }

    public int getF2() {
        return this.f2;
    }

    @Override
    public int compareTo(Alternative other) {
        return Integer.compare(this.f1, other.f1);
    }

    @Override
    public String toString() {
        return String.format("%s (%d, %d)", this.name, this.f1, this.f2);
    }
}

class NullAlternative extends Alternative {
    public NullAlternative() {
        super("", Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
}

public class Main {
    private static List<Alternative> initAlternatives(int[][] points) {
        List<Alternative> alternatives = new ArrayList<>();

        int index = 1;
        for (int[] point : points) {
            Alternative alternative = new Alternative(String.format("X%d", index), point[0], point[1]);
            alternatives.add(alternative);
            index++;
        }
        return alternatives;
    }

    private static boolean isDominatedByBothCriteria(Alternative a, Alternative pA) {
        return (a.getF1() < pA.getF1()) && (a.getF2() < pA.getF2());
    }

    private static boolean isDominatedByOneCriterion(Alternative a, Alternative pA) {
        boolean isDominatedByF1 = ((a.getF1() < pA.getF1()) && (a.getF2() == pA.getF2()));
        boolean isDominatedByF2 = (a.getF1() == pA.getF1()) && (a.getF2() < pA.getF2());
        return isDominatedByF1 || isDominatedByF2;
    }

    private static List<Alternative> findParetoSetX(List<Alternative> alternatives) {
        List<Alternative> paretoSet = new ArrayList<>(alternatives);
        List<Alternative> dominatedAlternatives = new ArrayList<>();

        for (Alternative a : alternatives) {
            for (Alternative pA : alternatives) {
                if (a == pA)
                    continue;
                if (isDominatedByBothCriteria(a, pA) || isDominatedByOneCriterion(a, pA)) {
                    dominatedAlternatives.add(a);
                    break;
                }
            }
        }

        paretoSet.removeAll(dominatedAlternatives);
        return paretoSet;
    }

    private static Alternative findMaxF1Alternative(List<Alternative> alternatives) {
        Alternative maxF1Alternative = new NullAlternative();
        for (Alternative alternative : alternatives) {
            if (alternative.getF1() > maxF1Alternative.getF1()) {
                maxF1Alternative = alternative;
            }
        }
        return maxF1Alternative;
    }

    private static Alternative findMaxF2Alternative(List<Alternative> alternatives) {
        Alternative maxF2Alternative = new NullAlternative();
        for (Alternative alternative : alternatives) {
            if (alternative.getF2() > maxF2Alternative.getF2()) {
                maxF2Alternative = alternative;
            }
        }
        return maxF2Alternative;
    }

    private static int[] findIdealPoint(List<Alternative> alternatives) {
        Alternative maxF1Alternative = findMaxF1Alternative(alternatives);
        Alternative maxF2Alternative = findMaxF2Alternative(alternatives);
        return new int[] { maxF1Alternative.getF1(), maxF2Alternative.getF2() };
    }

    private static double findEuclideanDistance(int[] pointA, int[] pointB) {
        return Math.sqrt(Math.pow(pointB[0] - pointA[0], 2) + Math.pow(pointB[1] - pointA[1], 2));
    }

    private static List<Alternative> findPreferredAlternatives(List<Alternative> paretoSet) {
        List<Alternative> CX = new ArrayList<>();
        double minDist = Double.POSITIVE_INFINITY;
        final int[] idealPoint = findIdealPoint(paretoSet);
        for (Alternative alternative : paretoSet) {
            int[] point = new int[] { alternative.getF1(), alternative.getF2() };
            double curMinDist = findEuclideanDistance(point, idealPoint);
            if (curMinDist < minDist) {
                CX.clear();
                CX.add(alternative);
                minDist = curMinDist;
            } else if (Math.abs(curMinDist - minDist) < 1e-9) {
                CX.add(alternative);
                minDist = curMinDist;
            }
        }
        return CX;
    }

    private static void printAlternatives(List<Alternative> alternatives) {
        String text = "";
        for (Alternative alternative : alternatives) {
            text += alternative + ", ";
        }
        out.println(text.substring(0, text.length() - 2));
    }

    private static void printEuclideanDistances(List<Alternative> alternatives) {
        final int[] idealPoint = findIdealPoint(alternatives);
        for (Alternative alternative : alternatives) {
            int[] point = new int[] { alternative.getF1(), alternative.getF2() };
            double dist = findEuclideanDistance(point, idealPoint);
            out.printf("Расстояние от точки %s до идеальной точки %s: %f\n", Arrays.toString(point), Arrays.toString(idealPoint), dist);
        }
    }

    public static void main(String[] args) {
        int[][] points = new int[][] {
                { 3, 2 },
                { 4, 5 },
                { 5, 3 },
                { 8, 3 },
                { 6, 2 },
                { 3, 8 },
                { 6, 4 },
                { 2, 5 },
                { 6, 4 },
                { 2, 5 },
        };

        List<Alternative> alternatives = initAlternatives(points);
        List<Alternative> paretoSet = findParetoSetX(alternatives);
        List<Alternative> preferredAlternatives = findPreferredAlternatives(paretoSet);
        out.println("Граница Парето образована решениями:");
        printAlternatives(paretoSet);
        out.println("Эффективными решениями являются:");
        printAlternatives(preferredAlternatives);
        out.printf("Идеальная точка: %s\n", Arrays.toString(findIdealPoint(paretoSet)));
        printEuclideanDistances(alternatives);
        out.printf("Расстояние от первого предпочитаемого решения %s до идеальной точки: %f", preferredAlternatives.getFirst(), findEuclideanDistance(new int [] { preferredAlternatives.getFirst().getF1(), preferredAlternatives.getFirst().getF2()}, findIdealPoint(paretoSet)));
    }
}