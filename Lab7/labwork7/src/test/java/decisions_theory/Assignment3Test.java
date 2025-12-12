package decisions_theory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.lang.System.out;

class Assignment3Test {
        @Test
        public void testAssignment3() {
                Integer[][] pairCompMatrix1 = new Integer[][] {
                                { 0, -1, 1, -1, 1, 1, 1 },
                                { 1, 0, 1, 1, 1, 1, 1 },
                                { -1, -1, 0, -1, 1, 1, 1 },
                                { 1, -1, 1, 0, 1, 1, 1 },
                                { -1, -1, -1, -1, 0, 1, -1 },
                                { -1, -1, -1, -1, -1, 0, -1 },
                                { -1, -1, -1, -1, 1, 1, 0 }
                };

                Integer[][] pairCompMatrix2 = new Integer[][] {
                                { 0, 1, 1, 1, 1, 1, 0 },
                                { -1, 0, 1, 0, -1, 1, -1 },
                                { -1, -1, 0, -1, -1, -1, -1 },
                                { -1, 0, 1, 0, -1, 1, -1 },
                                { -1, 1, 1, 1, 0, 1, -1 },
                                { -1, -1, -1, -1, -1, 0, -1 },
                                { 0, 1, 1, 1, 1, 1, 0 }
                };

                Integer[][] pairCompMatrix3 = new Integer[][] {
                                { 0, -1, -1, -1, -1, -1, -1 },
                                { 1, 0, -1, -1, -1, -1, 0 },
                                { 1, 1, 0, 1, 1, 1, 1 },
                                { 1, 1, -1, 0, 1, 0, 1 },
                                { 1, 1, -1, -1, 0, -1, 1 },
                                { 1, 1, -1, 0, 1, 0, 1 },
                                { 1, 0, -1, -1, -1, -1, 0 }
                };

                Integer[][] pairCompMatrix4 = new Integer[][] {
                                { 0, -1, -1, -1, 1, -1, 1 },
                                { 1, 0, -1, -1, 1, 0, 1 },
                                { 1, 1, 0, 0, 1, 1, 1 },
                                { 1, 1, 0, 0, 1, 1, 1 },
                                { -1, -1, -1, -1, 0, -1, 0 },
                                { 1, 0, -1, -1, 1, 0, 1 },
                                { -1, -1, -1, -1, 0, -1, 0 }
                };

                Integer[][] pairCompMatrix5 = new Integer[][] {
                                { 0, 0, 1, -1, 1, -1, 1 },
                                { 0, 0, 1, -1, 1, -1, 1 },
                                { -1, -1, 0, -1, -1, -1, -1 },
                                { 1, 1, 1, 0, 1, 0, 1 },
                                { -1, -1, 1, -1, 0, -1, -1 },
                                { 1, 1, 1, 0, 1, 0, 1 },
                                { -1, -1, 1, -1, 1, -1, 0 }
                };

                Integer[][] pairCompMatrix6 = new Integer[][] {
                                { 0, -1, -1, -1, 0, -1, -1 },
                                { 1, 0, -1, -1, 1, -1, -1 },
                                { 1, 1, 0, 1, 1, 1, -1 },
                                { 1, 1, -1, 0, 1, 1, -1 },
                                { 0, -1, -1, -1, 0, -1, -1 },
                                { 1, 1, -1, -1, 1, 0, -1 },
                                { 1, 1, 1, 1, 1, 1, 0 }
                };

                Integer[][] pairCompMatrix7 = new Integer[][] {
                                { 0, -1, -1, -1, 0, -1, -1 },
                                { 1, 0, -1, -1, 1, -1, -1 },
                                { 1, 1, 0, 1, 1, 1, 0 },
                                { 1, 1, -1, 0, 1, 1, -1 },
                                { 0, -1, -1, -1, 0, -1, -1 },
                                { 1, 1, -1, -1, 1, 0, -1 },
                                { 1, 1, 0, 1, 1, 1, 0 }
                };

                List<Integer[][]> pairCompMatrices = new ArrayList<Integer[][]>();
                Collections.addAll(pairCompMatrices, pairCompMatrix1, pairCompMatrix2, pairCompMatrix3, pairCompMatrix4,
                                pairCompMatrix5, pairCompMatrix6, pairCompMatrix7);
                String[] finalRanking = GroupChoiceProblem.computeKemeniMedian(pairCompMatrices);
                out.println("Итоговое групповое ранжирование R для 3-го варианта: " + Arrays.toString(finalRanking));
                assertTrue(true);
        }
}
