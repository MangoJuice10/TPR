package decisions_theory;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestExample {
    public static List<Alternative<Integer>> alternatives;
    public static int[][] criteriaPreferenceMatrix;
    public static int[][] criteriaEquivalenceMatrix;

    @BeforeAll
    static void initialize() {
        Integer[][] alternativesArr = new Integer[][] {
                { 3, 5, 5, 4 },
                { 4, 4, 4, 5 },
                { 5, 4, 3, 3 },
                { 3, 5, 3, 5 },
                { 4, 2, 4, 5 },
                { 3, 5, 3, 5 },
                { 5, 3, 4, 3 },
        };
        alternatives = Main.initAlternatives(alternativesArr);

        criteriaPreferenceMatrix = new int[][] {
                { 0, 1, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 1 },
                { 0, 0, 0, 0 },
        };

        criteriaEquivalenceMatrix = new int[][] {
                { 0, 0, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 0, 0 },
        };
    }

    @Test
    void testExample() {
        List<Alternative<Integer>> incompAlts = Main.findIncomparableAlternatives(alternatives);
        List<Alternative<Integer>> incompAltsBasedOnCritPref = Main.excludeAlternativesBasedOnPreference(incompAlts,
                criteriaPreferenceMatrix);
        List<Alternative<Integer>> incompAltsBasedOnCritEq = Main
                .excludeAlternativesBasedOnEquivalence(incompAltsBasedOnCritPref, criteriaEquivalenceMatrix);
        List<Alternative<Integer>> finalIncompAlts = new ArrayList<Alternative<Integer>>(incompAltsBasedOnCritEq);
        assertEquals(finalIncompAlts.toString().replace("[", "").replace("]", ""),
                "X1 (3, 5, 5, 4), X2 (4, 4, 4, 5), X3 (5, 4, 3, 3), X7 (5, 3, 4, 3)");
    }
}