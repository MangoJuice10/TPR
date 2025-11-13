package decisions_theory;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.System.out;

class Alternative<T extends Number> {
    private String name;
    private final List<T> criteria;

    public Alternative(String name, T[] criteriaArr) {
        this.name = name;
        this.criteria = Arrays.asList(criteriaArr);
    }

    public List<T> getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, criteria.toString()
                .replace('[', '(')
                .replace(']', ')'));
    }
}

public class Main {
    public static <T extends Number> List<Alternative<T>> initAlternatives(T[][] alternativesArr) {
        String baseName = "X";
        List<Alternative<T>> alternatives = new ArrayList<Alternative<T>>();
        for (int i = 0; i < alternativesArr.length; i++) {
            Alternative<T> alternative = new Alternative<T>(String.format("%s%d", baseName, i + 1), alternativesArr[i]);
            alternatives.add(alternative);
        }
        return alternatives;
    }

    public static <T extends Number> List<Alternative<T>> findIncomparableAlternatives(List<Alternative<T>> alternatives) {
        List<Alternative<T>> incomparabAlternatives = new ArrayList<Alternative<T>>(alternatives);
        List<Alternative<T>> dominatedAlternatives = new ArrayList<Alternative<T>>();
        for (Alternative<T> a : alternatives) {
            for (Alternative<T> pA : alternatives) {
                if (a == pA) {
                    continue;
                }

                boolean isDominated = true;

                List<T> criteriaA = a.getCriteria();
                List<T> criteriapA = pA.getCriteria();

                for (int i = 0; i < criteriaA.size(); i++) {
                    if (criteriaA.get(i).doubleValue() > criteriapA.get(i).doubleValue()) {
                        isDominated = false;
                    }
                }

                if (isDominated) {
                    dominatedAlternatives.add(a);
                }
            }
        }
        incomparabAlternatives.removeAll(dominatedAlternatives);
        return incomparabAlternatives;
    }

    public static void main(String[] args) {
        Integer[][] alternativesArr = {
                { 3, 5, 5, 4, 4 },
                { 4, 4, 4, 5, 4 },
                { 5, 4, 3, 3, 5 },
                { 3, 5, 3, 5, 3 },
                { 4, 2, 4, 5, 5 },
                { 3, 5, 3, 5, 3 },
                { 5, 3, 4, 3, 4 },
                { 4, 5, 3, 4, 3 },
        };

        List<Alternative<Integer>> alternatives = initAlternatives(alternativesArr);

        int[][] criteriaPreferenceMatrix = {
            { 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0 },
        };

        int[][] criteriaEquivalenceMatrix = {
            { 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0 },
            { 0, 1, 0, 1, 0 },
            { 0, 0, 1, 0, 1 },
            { 0, 0, 0, 0, 0 },
        };

        List<Alternative<Integer>> incomparableAlternatives = findIncomparableAlternatives(alternatives);
        out.println(incomparableAlternatives);
    }
}