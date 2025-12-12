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
        String ranking1 = "x2, x4, x1, x3, x5, x8, x7, x6";
        String ranking2 = "x1, x5, x4~x2, x6, x7~x8, x3";
        String ranking3 = "x3, x4, x8~x7, x6, x5, x2, x1";
        String ranking4 = "x7, x8, x3~x4, x6~x2, x1, x5";
        String ranking5 = "x4, x8, x3~x7, x6~x1, x2, x5";
        List<Integer[][]> pairCompMatrices = new ArrayList<Integer[][]>();
        Collections.addAll(pairCompMatrices,
                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking1),
                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking2),
                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking3),
                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking4),
                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking5)
        );
        String[] finalRanking = GroupChoiceProblem.computeKemeniMedian(pairCompMatrices);
        out.println("Итоговое групповое ранжирование R для 1-го варианта: " + Arrays.toString(finalRanking));
        assertTrue(true);
    }
}
