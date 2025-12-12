package decisions_theory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.lang.System.out;

class Assignment1Test {
    @Test
    public void testAssignment1() {
        Integer[][] pairCompMatrix1 = new Integer[][] {
                { 0, -1, 1, -1, 1, 1, 1, 1 },
                { 1, 0, 1, 1, 1, 1, 1, 1 },
                { -1, -1, 0, -1, 1, 1, 1, 1 },
                { 1, -1, 1, 0, 1, 1, 1, 1 },
                { -1, -1, -1, -1, 0, 1, 1, 1 },
                { -1, -1, -1, -1, -1, 0, -1, -1 },
                { -1, -1, -1, -1, -1, 1, 0, -1 },
                { -1, -1, -1, -1, -1, 1, 1, 0 }
        };

        Integer[][] pairCompMatrix2 = new Integer[][] {
                { 0, 1, 1, 1, 1, 1, 1, 1 },
                { -1, 0, 1, 0, -1, 1, 1, 1 },
                { -1, -1, 0, -1, -1, -1, -1, -1 },
                { -1, 0, 1, 0, -1, 1, 1, 1 },
                { -1, 1, 1, 1, 0, 1, 1, 1 },
                { -1, -1, -1, -1, -1, 0, -1, -1 },
                { -1, -1, -1, -1, -1, 1, 0, 0 },
                { -1, -1, -1, -1, -1, 1, 0, 0 }
        };

        Integer[][] pairCompMatrix3 = new Integer[][] {
                { 0, -1, -1, -1, -1, -1, -1, -1 },
                { 1, 0, -1, -1, -1, -1, -1, -1 },
                { 1, 1, 0, 1, 1, 1, 1, 1 },
                { 1, 1, -1, 0, 1, 1, 1, 1 },
                { 1, 1, -1, -1, 0, -1, -1, -1 },
                { 1, 1, -1, -1, 1, 0, -1, -1 },
                { 1, 1, -1, -1, 1, 1, 0, 0 },
                { 1, 1, -1, -1, 1, 1, 0, 0 }
        };

        Integer[][] pairCompMatrix4 = new Integer[][] {
                { 0, -1, -1, -1, -1, -1, -1, -1 },
                { 1, 0, -1, -1, -1, 0, -1, -1 },
                { 1, 1, 0, 0, -1, 1, -1, -1 },
                { 1, 1, 0, 0, -1, 1, -1, -1 },
                { 1, 1, 1, 1, 0, 1, -1, -1 },
                { 1, 0, -1, -1, -1, 0, -1, -1 },
                { 1, 1, 1, 1, 1, 1, 0, 1 },
                { 1, 1, 1, 1, 1, 1, -1, 0 }
        };

        Integer[][] pairCompMatrix5 = new Integer[][] {
                { 0, -1, -1, -1, -1, 0, -1, -1 },
                { 1, 0, -1, -1, -1, -1, -1, -1 },
                { 1, 1, 0, -1, 1, 1, 0, -1 },
                { 1, 1, 1, 0, 1, 1, 1, 1 },
                { 1, 1, -1, -1, 0, -1, -1, -1 },
                { 0, 1, -1, -1, 1, 0, -1, -1 },
                { 1, 1, 0, -1, 1, 1, 0, -1 },
                { 1, 1, 1, -1, 1, 1, 1, 0 }
        };

        List<Integer[][]> pairCompMatrices = new ArrayList<Integer[][]>();
        Collections.addAll(pairCompMatrices, pairCompMatrix1, pairCompMatrix2, pairCompMatrix3, pairCompMatrix4,
                pairCompMatrix5);
        String[] finalRanking = GroupChoiceProblem.computeKemeniMedian(pairCompMatrices);
        out.println("Итоговое групповое ранжирование R для 1-го варианта: " + Arrays.toString(finalRanking));
        assertTrue(true);
    }
}
