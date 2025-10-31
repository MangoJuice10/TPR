package decisions_theory;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import static java.lang.System.out;

class Alternative implements Comparable<Alternative> {
    private String name;
    private final int f1;
    private final int f2;

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
    private static ArrayList<Alternative> initAlternatives(int[][] points) {
        ArrayList<Alternative> alternatives = new ArrayList<>();

        int index = 1;
        for (int[] point : points) {
            Alternative alternative = new Alternative(String.format("X%d", index), point[0], point[1]);
            alternatives.add(alternative);
            index++;
        }
        return alternatives;
    }

    private static ArrayList<Alternative> findParetoSetX(ArrayList<Alternative> alternatives) {
        ArrayList<Alternative> paretoSet = new ArrayList<>();
        Collections.sort(alternatives);
        for (Alternative alternative : alternatives) {
            boolean isDominated = false;

            Iterator<Alternative> iterator = paretoSet.iterator();
            while (iterator.hasNext()) {
                Alternative paretoAlternative = iterator.next();
                if (alternative.getF1() == paretoAlternative.getF1()
                        && alternative.getF2() < paretoAlternative.getF2()) {
                    isDominated = true;
                    break;
                }
                if (alternative.getF2() > paretoAlternative.getF2()) {
                    iterator.remove();
                }
            }

            if (!isDominated) {
                paretoSet.add(alternative);
            }
        }
        return paretoSet;
    }

    private static Alternative findMaxF1Alternative(ArrayList<Alternative> alternatives) {
        Alternative maxF1Alternative = new NullAlternative();
        for (Alternative alternative : alternatives) {
            if (alternative.getF1() > maxF1Alternative.getF1()) {
                maxF1Alternative = alternative;
            }
        }
        return maxF1Alternative;
    }

    private static Alternative findMaxF2Alternative(ArrayList<Alternative> alternatives) {
        Alternative maxF2Alternative = new NullAlternative();
        for (Alternative alternative : alternatives) {
            if (alternative.getF2() > maxF2Alternative.getF2()) {
                maxF2Alternative = alternative;
            }
        }
        return maxF2Alternative;
    }

    private static int[] findIdealPoint(ArrayList<Alternative> alternatives) {
        Alternative maxF1Alternative = findMaxF1Alternative(alternatives);
        Alternative maxF2Alternative = findMaxF2Alternative(alternatives);
        return new int[] { maxF1Alternative.getF1(), maxF2Alternative.getF2() };
    }

    private static double findEuclideanDistance(int[] pointA, int[] pointB) {
        return Math.sqrt(Math.pow(pointB[0] - pointA[0], 2) + Math.pow(pointB[1] - pointA[1], 2));
    }

    private static ArrayList<Alternative> findPreferredAlternatives(ArrayList<Alternative> paretoSet) {
        ArrayList<Alternative> CX = new ArrayList<>();
        double minDist = Double.POSITIVE_INFINITY;
        final int[] idealPoint = findIdealPoint(paretoSet);
        for (Alternative alternative : paretoSet) {
            int[] point = new int[] {alternative.getF1(), alternative.getF2()};
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

    private static void printAlternatives(ArrayList<Alternative> alternatives) {
        String text = "";
        for (Alternative alternative : alternatives) {
            text += alternative + ", ";
        }
        out.println(text.substring(0, text.length() - 2));
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
        ArrayList<Alternative> alternatives = initAlternatives(points);
        ArrayList<Alternative> paretoSet = findParetoSetX(alternatives);
        ArrayList<Alternative> preferredAlternatives = findPreferredAlternatives(paretoSet);
        out.println("Эффективными решениями являются:");
        printAlternatives(preferredAlternatives);
    }
}