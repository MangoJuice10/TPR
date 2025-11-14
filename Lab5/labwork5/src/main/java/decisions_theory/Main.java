package decisions_theory;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.System.out;

class Alternative<T extends Number> {
    private String name;

    private final Vector<T> vector;
    private Vector<T> modifiedVector;

    public Alternative(String name, T[] criteriaArr) {
        this.name = name;
        this.vector = new Vector<T>(Arrays.asList(criteriaArr));
    }

    public String getName() {
        return name;
    }

    public Vector<T> getVector() {
        return vector;
    }

    public Vector<T> getModifiedVector() {
        return modifiedVector;
    }

    public void createModifiedVector(int criterionIndexA, int criterionIndexB) {
        Vector<T> modifiedVector = new Vector<T>(vector);

        int vectorSize = vector.getCriteriaCount();
        boolean criterionIndexAOutOfBounds = criterionIndexA < 0 || criterionIndexA > vectorSize - 1;
        boolean criterionIndexBOutOfBounds = criterionIndexB < 0 || criterionIndexB > vectorSize - 1;

        if (!(criterionIndexAOutOfBounds || criterionIndexBOutOfBounds)) {
            modifiedVector.swapCriteria(criterionIndexA, criterionIndexB);
        }

        this.modifiedVector = modifiedVector;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, vector);
    }

    public String toStringModifiedVector() {
        return String.format("%s %s", name, modifiedVector);
    }
}

class Vector<T extends Number> implements Cloneable {
    private List<T> criteria;

    public Vector(List<T> criteria) {
        this.criteria = criteria;
    }

    public Vector(Vector<T> vector) {
        this.criteria = new ArrayList<T>(vector.getCriteria());
    }

    public List<T> getCriteria() {
        return criteria;
    }

    public int getCriteriaCount() {
        return criteria.size();
    }

    public void swapCriteria(int criterionIndexA, int criterionIndexB) {
        T criterionA = criteria.get(criterionIndexA);
        T criterionB = criteria.get(criterionIndexB);
        criteria.set(criterionIndexA, criterionB);
        criteria.set(criterionIndexB, criterionA);
    }

    @Override
    public Vector<T> clone() {
        return new Vector<T>(new ArrayList<T>(criteria));
    }

    @Override
    public String toString() {
        return criteria.toString()
                .replace('[', '(')
                .replace(']', ')');
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

    public static <T extends Number> boolean isVectorDominated(Vector<T> vectorA, Vector<T> vectorB) {
        boolean result = true;

        List<T> criteriaA = vectorA.getCriteria();
        List<T> criteriapA = vectorB.getCriteria();
        for (int i = 0; i < criteriaA.size(); i++) {
            if (criteriaA.get(i).doubleValue() > criteriapA.get(i).doubleValue()) {
                result = false;
            }
        }
        return result;
    }

    public static <T extends Number> List<Alternative<T>> findIncomparableAlternatives(
            List<Alternative<T>> alternatives) {
        List<Alternative<T>> incomparabAlternatives = new ArrayList<Alternative<T>>(alternatives);
        List<Alternative<T>> dominatedAlternatives = new ArrayList<Alternative<T>>();
        for (Alternative<T> a : alternatives) {
            for (Alternative<T> pA : alternatives) {
                if (a == pA) {
                    continue;
                }

                boolean isDominated = isVectorDominated(a.getVector(), pA.getVector());

                if (isDominated) {
                    dominatedAlternatives.add(a);
                }
            }
        }
        incomparabAlternatives.removeAll(dominatedAlternatives);
        return incomparabAlternatives;
    }

    public static <T extends Number> List<Alternative<T>> findDominatedAlternativesModified(
            List<Alternative<T>> alternatives, Alternative<T> aModified) {
        List<Alternative<T>> incomparableAlternatives = new ArrayList<Alternative<T>>(alternatives);
        List<Alternative<T>> dominatedAlternatives = new ArrayList<Alternative<T>>();
        for (Alternative<T> a : incomparableAlternatives) {
            if (a.getName() == aModified.getName()) {
                continue;
            }

            boolean isDominated = isVectorDominated(a.getVector(), aModified.getModifiedVector());

            if (isDominated) {
                dominatedAlternatives.add(a);
            }
        }

        return dominatedAlternatives;
    }

    public static <T extends Number> boolean isModifiedVectorValid(T dominantCriterion, T dominatedCriterion) {
        return dominantCriterion.doubleValue() > dominatedCriterion.doubleValue();
    }

    public static <T extends Number> List<Alternative<T>> excludeAlternativesBasedOnPreference(List<Alternative<T>> alternatives, int[][] criteriaPreferenceMatrix) {
        List<Alternative<T>> incomparableAlternatives = new ArrayList<Alternative<T>>(alternatives);
        List<Alternative<T>> dominatedAlternatives = new ArrayList<Alternative<T>>();
        for (int i = 0; i < criteriaPreferenceMatrix.length; i++) {
            for (int j = 0; j < criteriaPreferenceMatrix.length; j++) {
                if (criteriaPreferenceMatrix[i][j] == 1) {
                    for (Alternative<T> a : alternatives) {
                        T dominantCriterion = a.getVector().getCriteria().get(i);
                        T dominatedCriterion = a.getVector().getCriteria().get(j);
                        if (!isModifiedVectorValid(dominantCriterion, dominatedCriterion)) {
                            continue;
                        }

                        a.createModifiedVector(i, j);
                        dominatedAlternatives.addAll(findDominatedAlternativesModified(incomparableAlternatives, a));
                    }
                }
            }
        }

        incomparableAlternatives.removeAll(dominatedAlternatives);

        return incomparableAlternatives;
    }

    public static <T extends Number> List<Alternative<T>> excludeAlternativesBasedOnEquivalence(List<Alternative<T>> alternatives, int[][] criteriaEquivalenceMatrix) {
        List<Alternative<T>> incomparableAlternatives = new ArrayList<Alternative<T>>(alternatives);
        List<Alternative<T>> dominatedAlternatives = new ArrayList<Alternative<T>>();
        for (int i = 0; i < criteriaEquivalenceMatrix.length; i++) {
            for (int j = 0; j < criteriaEquivalenceMatrix.length; j++) {
                if (criteriaEquivalenceMatrix[i][j] == 1) {
                    for (Alternative<T> a : alternatives) {
                        a.createModifiedVector(i, j);
                        dominatedAlternatives.addAll(findDominatedAlternativesModified(incomparableAlternatives, a));
                    }
                }
            }
        }

        incomparableAlternatives.removeAll(dominatedAlternatives);

        return incomparableAlternatives;
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
                { 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0 },
        };

        List<Alternative<Integer>> incomparableAlternatives = findIncomparableAlternatives(alternatives);
        out.println(incomparableAlternatives);
        
        List<Alternative<Integer>> incomparableAlternativesBasedOnCriteriaPreference = excludeAlternativesBasedOnPreference(incomparableAlternatives, criteriaPreferenceMatrix);
        out.println(incomparableAlternativesBasedOnCriteriaPreference);

        List<Alternative<Integer>> incomparableAlternativesBasedOnCriteriaEquivalence = excludeAlternativesBasedOnEquivalence(incomparableAlternativesBasedOnCriteriaPreference, criteriaEquivalenceMatrix);
        out.println(incomparableAlternativesBasedOnCriteriaEquivalence);
    }
}