package decisions_theory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.lang.System.out;

class Assignment2Test {
        @Test
        public void testAssignment2() {
                String ranking1 = "x2, x4, x1, x3, x5, x6";
                String ranking2 = "x1, x5, x4~x2, x6, x3";
                String ranking3 = "x3, x4~x6, x5, x2, x1";
                String ranking4 = "x3~x4, x6~x2, x1, x5";
                String ranking5 = "x3~x4, x6~x2, x1, x5";
                String ranking6 = "x6~x4, x1~x2, x5, x3";
                String ranking7 = "x3, x4, x6, x2, x1~x5";
                String ranking8 = "x3, x4, x1~x2, x5, x6";
                String ranking9 = "x3, x4, x6~x2, x1~x5";
                List<Integer[][]> pairCompMatrices = new ArrayList<Integer[][]>();
                Collections.addAll(pairCompMatrices,
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking1),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking2),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking3),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking4),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking5),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking6),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking7),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking8),
                                GroupChoiceProblem.convertRankingToPairCompMatrix(ranking9)
                        );
                String[] finalRanking = GroupChoiceProblem.computeKemeniMedian(pairCompMatrices);
                out.println("Итоговое групповое ранжирование R для 2-го варианта: " + Arrays.toString(finalRanking));
                assertTrue(true);
        }
}
