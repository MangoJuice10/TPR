import static java.lang.System.out;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

public class Lab2 {
    public static int chooseXnPlus1(int[][] a, ArrayList<Integer> Xn) {
        int card = a[0].length;
        ArrayList<Integer> options = new ArrayList<>();

        for (int x : Xn) {
            for (int i = 0; i < card; i++) {
                if (!Xn.contains(i) && !options.contains(i) && a[i][x] == 1) {
                    options.add(i);
                }
            }

            for (int j = 0; j < card; j++) {
                if (!Xn.contains(j) && !options.contains(j) && a[x][j] == 1) {
                    options.add(j);
                }
            }
        }

        if (!options.isEmpty()) return Collections.min(options);
        return -1;
        
    }

    public static ArrayList<Integer> getXnPlus(int[][] a, ArrayList<Integer> Xn, int j) {
        int card = a[0].length;
        ArrayList<Integer> XnPlus = new ArrayList<>();
        for (int i = 0; i < card; i++) {
            if (a[i][j] == 1 && Xn.contains(i)) {
                XnPlus.add(i);
            }
        }
        return XnPlus;
    }

    public static ArrayList<Integer> getXnMinus(int[][] a, ArrayList<Integer> Xn, int i) {
        int card = a[0].length;
        ArrayList<Integer> XnMinus = new ArrayList<>();
        for (int j = 0; j < card; j++) {
            if (a[i][j] == 1 && Xn.contains(j)) {
                XnMinus.add(j);
            }
        }
        return XnMinus;
    }

    public static double computeU(ArrayList<Integer> XnPlus, ArrayList<Integer> XnMinus, double[] Us) {
        if (XnPlus.isEmpty() && !XnMinus.isEmpty()) {
            double xDoubleQt = -1;
            for (int x : XnMinus) {
                if (Us[x] > xDoubleQt)
                    xDoubleQt = Us[x];
            }
            double U = xDoubleQt + 1;
            return U;
        }

        if (!XnPlus.isEmpty() && XnMinus.isEmpty()) {
            double xQt = Us[XnPlus.get(0)];
            for (int x : XnPlus) {
                if (Us[x] < xQt)
                    xQt = Us[x];
            }
            double U = xQt - 1;
            return U;
        }

        if (!XnPlus.isEmpty() && !XnMinus.isEmpty()) {
            ArrayList<Integer> inter = new ArrayList<>(XnPlus);
            inter.retainAll(XnMinus);
            boolean isInter = !inter.isEmpty();

            if (isInter) {
                return Us[inter.get(0)];
            } else {
                double xDoubleQt = -1;
                for (int x : XnMinus) {
                    if (Us[x] > xDoubleQt)
                        xDoubleQt = Us[x];
                }

                double xQt = Us[XnPlus.get(0)];
                for (int x : XnPlus) {
                    if (Us[x] < xQt)
                        xQt = Us[x];
                }

                double U = (xQt + xDoubleQt) / 2;
                return U;
            }
        }

        return -1;
    }

    public static double[] findUs(int[][] a) {
        int card = a[0].length;
        double[] Us = new double[card];
        ArrayList<Integer> Xn = new ArrayList<>();
        ArrayList<Integer> XnPlus;
        ArrayList<Integer> XnMinus;

        // U(x1) = 0
        Us[0] = 0;

        // Xn = {x1}
        Xn.add(0);

        for (int xS = 1; xS < card; xS++) {
            int xnPlus1 = chooseXnPlus1(a, Xn);
            if (xnPlus1 == -1) {
                out.println("Алгоритм не реализуется для заданной матрицы отношения.");
                break;
            }

            XnPlus = getXnPlus(a, Xn, xnPlus1);
            XnMinus = getXnMinus(a, Xn, xnPlus1);

            Us[xnPlus1] = computeU(XnPlus, XnMinus, Us);
            Xn.add(xnPlus1);
        }

        return Us;
    }

    public static void main(String[] args) {
        int[][] a = new int[][] {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 1 },
                { 0, 1, 0, 0, 1, 1, 0 },
                { 0, 1, 0, 0, 0, 0, 0 },
                { 1, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 1, 0, 0, 1, 0 },
        };

        a = new int[][] {
                { 1, 0, 1, 0, 0 },
                { 1, 1, 0, 0, 0 },
                { 0, 0, 1, 1, 0 },
                { 0, 0, 0, 1, 1 },
                { 0, 0, 0, 1, 1 },
        };

        double[] Us = findUs(a);
        out.printf("Матрица отношения R: \n%s\n\n",
                Arrays.deepToString(a).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
        out.printf("Полезность каждого решения U(xi): %s", Arrays.toString(Us));
    }
}