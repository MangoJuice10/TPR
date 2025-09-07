import static java.lang.System.out;

import java.util.Arrays;

public class Lab1 {
    public static int[] findmaxRX(int[][] a, int card) {
        int[] maxRX = new int[card];
        for (int i = 0; i < card; i++) {
            maxRX[i] = 1;
        }
        for (int i = 0; i < card; i++) {
            for (int j = 0; j < card; j++) {
                if (a[i][j] == 1) {
                    if (a[j][i] == 0)
                        maxRX[j] = 0;
                    if (a[j][i] == 1 && maxRX[i] == 0)
                        maxRX[j] = 0;
                }
            }
        }
        return maxRX;
    }

    public static void main(String[] args) {
        int[][] a = new int[][] {
                { 0, 1, 0, 0, 0, 0 },
                { 1, 0, 1, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0 },
        };

        /* 
        a = new int[][] {
        { 0, 0, 0, 1, 0 },
        { 0, 0, 1, 0, 0 },
        { 0, 0, 0, 1, 1 },
        { 0, 0, 1, 0, 0 },
        { 0, 0, 1, 0, 0 },
        };
        
        a = new int[][] {
        { 0, 1, 0, 1, 0 },
        { 1, 0, 1, 0, 0 },
        { 0, 0, 0, 1, 1 },
        { 0, 0, 1, 0, 0 },
        { 0, 0, 0, 0, 0 },
        };
         */

        int card = 6;

        int[] res = Lab1.findmaxRX(a, card);
        out.printf("Матрица отношения R: \n%s\n\n", Arrays.deepToString(a).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
        out.printf("Множество максимальных элементов MaxRX: %s", Arrays.toString(res));
    }
}