package decisions_theory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TestMain {
    private static List<Alternative<Integer>> alternatives;
    private static int[][] criteriaPreferenceMatrix;
    private static int[][] criteriaEquivalenceMatrix;

    @BeforeAll
    static void initialize() {
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

        alternatives = Main.initAlternatives(alternativesArr);

        criteriaPreferenceMatrix = new int[][] {
                { 0, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0 },
        };

        criteriaEquivalenceMatrix = new int[][] {
                { 0, 0, 0, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 1, 0, 1, 0 },
                { 0, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 0 },
        };
    }

    @Test
    void testModifiedVector() {
        // Original vector is (3, 5, 5, 4, 4)
        Alternative<Integer> X1 = alternatives.get(0);
        X1.createModifiedVector(0, 4);
        // Modified vector should be (4, 5, 5, 4, 3)
        assertEquals(X1.getModifiedVector().toString(), "(4, 5, 5, 4, 3)");
    }

    @Test
    void testExclusionBasedOnPreferenceK1K2() {
        List<Alternative<Integer>> incomparableAlternatives = Main.findIncomparableAlternatives(alternatives);
        String dominatedAlternativesString = null;
        // Only incomparable alternatives are takes into consideration
        // If current alternative has the same values for both K1 and K2, it isn't taken
        // into consideration

        // X1 (5, 3, 5, 4, 4) dominates only (5, 3, 4, 3, 4)
        Alternative<Integer> X1 = alternatives.get(0);
        X1.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X1)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "X7 (5, 3, 4, 3, 4)");

        // X3 (4, 5, 3, 3, 5) dominates nobody
        Alternative<Integer> X3 = alternatives.get(2);
        X3.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X3)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");

        // X5 (2, 4, 4, 5, 5) dominates nobody
        Alternative<Integer> X5 = alternatives.get(4);
        X5.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X5)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");

        // X7 (3, 5, 4, 3, 4) dominates nobody
        Alternative<Integer> X7 = alternatives.get(6);
        X7.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X7)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");

        // X8 (5, 4, 3, 4, 3) dominates nobody
        Alternative<Integer> X8 = alternatives.get(7);
        X8.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X8)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");
    }

    @Test
    void testExclusionBasedOnPreferenceK4K5() {
        List<Alternative<Integer>> incomparableAlternatives = Main.findIncomparableAlternatives(alternatives);
        String dominatedAlternativesString = null;

        // Only incomparable alternatives are takes into consideration
        // If current alternative has the same values for both K4 and K5, it isn't taken
        // into consideration

        // X2 (4, 4, 4, 4, 5) dominates nobody
        Alternative<Integer> X2 = alternatives.get(1);
        X2.createModifiedVector(3, 4);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X2)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");

        // X3 (5, 4, 3, 5, 3) dominates nobody
        Alternative<Integer> X3 = alternatives.get(2);
        X3.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X3)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");

        // X7 (5, 3, 4, 4, 3) dominates nobody
        Alternative<Integer> X7 = alternatives.get(6);
        X7.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X7)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");

        // X8 (5, 4, 3, 3, 4) dominates nobody
        Alternative<Integer> X8 = alternatives.get(7);
        X8.createModifiedVector(0, 1);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X8)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString, "");
    }

    @Test
    void testIdealAlternativeDominatesAllAlternatives() {
        List<Alternative<Integer>> alternativesWithIdealAlternative = new ArrayList<Alternative<Integer>>(alternatives);
        Alternative<Integer> X_Ideal = new Alternative<Integer>("X_Ideal", new Integer[] { 5, 5, 5, 5, 5 });
        X_Ideal.createModifiedVector(0, 0);

        alternativesWithIdealAlternative.add(X_Ideal);
        
        List<Alternative<Integer>> incompAlts = Main.findIncomparableAlternatives(alternativesWithIdealAlternative);

        // (5, 5, 5, 5, 5) dominates everyone
        
        List<Alternative<Integer>> incompAltsBasedOnPref = Main.excludeAlternativesBasedOnPreference(incompAlts, criteriaPreferenceMatrix);
        List<Alternative<Integer>> incompAltsBasedOnEq = Main.excludeAlternativesBasedOnPreference(incompAltsBasedOnPref, criteriaEquivalenceMatrix);
        List<Alternative<Integer>> finalIncompAlts = new ArrayList<Alternative<Integer>>(incompAltsBasedOnEq);
        assertEquals(finalIncompAlts.toString().replace("[", "").replace("]", ""), "X_Ideal (5, 5, 5, 5, 5)");
    }

    @Test
    void testExclusionBasedOnPreferenceIdeal() {
        List<Alternative<Integer>> incomparableAlternatives = Main.findIncomparableAlternatives(alternatives);
        String dominatedAlternativesString = null;

        // All incomparable alternatives are dominated by (5, 5, 5, 5, 5) 
        Alternative<Integer> X_Ideal = new Alternative<Integer>("X_Ideal", new Integer[] { 5, 5, 5, 5, 5 });
        X_Ideal.createModifiedVector(0, 0);
        dominatedAlternativesString = Main.findDominatedAlternativesModified(incomparableAlternatives, X_Ideal)
                .stream()
                .map(Alternative::toString)
                .collect(Collectors.joining(", "));
        assertEquals(dominatedAlternativesString,
                "X1 (3, 5, 5, 4, 4), X2 (4, 4, 4, 5, 4), X3 (5, 4, 3, 3, 5), X4 (3, 5, 3, 5, 3), X5 (4, 2, 4, 5, 5), X6 (3, 5, 3, 5, 3), X7 (5, 3, 4, 3, 4), X8 (4, 5, 3, 4, 3)");
    }
}
