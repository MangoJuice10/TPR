package decisions_theory;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import static java.lang.System.out;

class Alternative {
    private String name;

    public Alternative(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class GroupChoiceProblem {

    public static Integer[][] convertRankingToPairCompMatrix(String ranking) {
        Integer[][] ranks = Arrays.stream(ranking.split(", "))
                .map(rank -> Arrays.stream(rank.split("~"))
                        .map(altName -> Integer.valueOf(altName.substring(1)) - 1)
                        .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
        Integer[] altsInds = Arrays.stream(ranking.split(", "))
                .flatMap(rank -> Arrays.stream(rank.split("~"))
                        .map(altName -> Integer.valueOf(altName.substring(1)) - 1))
                .toArray(Integer[]::new);

        int altsCount = altsInds.length;

        Integer[][] pairCompMatrix = new Integer[altsCount][altsCount];

        for (Integer[] rankAltsInds : ranks) {
            for (int rankAltIdx : rankAltsInds) {
                for (int i = 0; i < altsCount; i++) {
                    if (Arrays.asList(rankAltsInds).contains(i)) {
                        pairCompMatrix[rankAltIdx][i] = 0;
                    } else if (Arrays.asList(altsInds).indexOf(i) > Arrays.asList(altsInds).indexOf(rankAltIdx)) {
                        pairCompMatrix[rankAltIdx][i] = 1;
                    } else {
                        pairCompMatrix[rankAltIdx][i] = -1;
                    }
                }
            }
        }
        return pairCompMatrix;
    }

    public static Integer[][] computeMaxMatrix(List<Integer[][]> pairCompMatrices) {
        int altsCount = pairCompMatrices.get(0).length;
        int expertsCount = pairCompMatrices.size();
        Integer[][] maxMatrix = new Integer[altsCount][altsCount];
        for (int k = 0; k < altsCount; k++) {
            for (int l = 0; l < altsCount; l++) {
                int maxValue = Integer.MIN_VALUE;
                for (int i = 0; i < expertsCount; i++) {
                    Integer[][] pairCompMatrix = pairCompMatrices.get(i);
                    if (pairCompMatrix[k][l] > maxValue) {
                        maxValue = pairCompMatrix[k][l];
                    }
                }
                maxMatrix[k][l] = maxValue;
            }
        }
        return maxMatrix;
    }

    public static Integer[][] computeLossMatrix(List<Integer[][]> pairCompMatrices, Integer[][] maxMatrix) {
        int altsCount = pairCompMatrices.get(0).length;
        int expertsCount = pairCompMatrices.size();
        Integer[][] lossMatrix = new Integer[altsCount][altsCount];
        for (int k = 0; k < altsCount; k++) {
            for (int l = 0; l < altsCount; l++) {
                int lossSum = 0;
                for (int i = 0; i < expertsCount; i++) {
                    lossSum += Math.abs(pairCompMatrices.get(i)[k][l] - maxMatrix[k][l]);
                }
                lossMatrix[k][l] = lossSum;
            }
        }
        return lossMatrix;
    }

    public static Integer[][] computeModifiedLossMatrix(Integer[][] lossMatrix) {
        int altsCount = lossMatrix.length;
        Integer[][] modifiedLossMatrix = new Integer[altsCount][altsCount];
        Integer[][] specialMatrix = computeSpecialMatrix(altsCount, altsCount);

        Integer[][] intermediateModifiedLossMatrix = multiplyMatrices(specialMatrix, lossMatrix);
        modifiedLossMatrix = multiplyMatrices(intermediateModifiedLossMatrix, specialMatrix);
        return modifiedLossMatrix;
    }

    public static Integer[][] computeSpecialMatrix(int rowsCount, int colsCount) {
        Integer[][] specialMatrix = new Integer[rowsCount][colsCount];
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                if (i == j) {
                    specialMatrix[i][j] = 0;
                } else {
                    specialMatrix[i][j] = 1;
                }
            }
        }
        return specialMatrix;
    }

    public static Integer[][] multiplyMatrices(Integer[][] matrixA, Integer[][] matrixB) {
        int rowsCount = matrixA.length;
        int colsCount = matrixB[0].length;
        Integer[][] result = new Integer[rowsCount][colsCount];
        for (int rowA = 0; rowA < rowsCount; rowA++) {
            for (int colB = 0; colB < colsCount; colB++) {
                int sum = 0;
                for (int i = 0; i < colsCount; i++) {
                    sum += matrixA[rowA][i] * matrixB[i][colB];
                }
                result[rowA][colB] = sum;
            }
        }
        return result;
    }

    public static int[] findMatrixMinElementIndicies(Integer[][] matrix) {
        int minValue = Integer.MAX_VALUE;
        int l = -1, k = -1;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i == j) {
                    continue;
                }
                if (matrix[i][j] < minValue) {
                    minValue = matrix[i][j];
                    l = i;
                    k = j;
                }
            }
        }
        return new int[] { l, k };
    }

    public static boolean isAxisRemoved(int idx, int[] removedAxes) {
        return idx == removedAxes[0] || idx == removedAxes[1];
    }

    public static Integer[][] reduceMatrix(Integer[][] matrix, int[] removedAxes) {
        int altsCount = matrix.length;
        int modifiedAltsCount = altsCount - 2;
        Integer[][] reducedMatrix = new Integer[modifiedAltsCount][modifiedAltsCount];
        for (int rowA = 0, rowB = 0; rowA < altsCount; rowA++) {
            if (isAxisRemoved(rowA, removedAxes)) {
                continue;
            }
            for (int colA = 0, colB = 0; colA < altsCount; colA++) {
                if (isAxisRemoved(colA, removedAxes)) {
                    continue;
                }
                reducedMatrix[rowB][colB] = matrix[rowA][colA];
                colB++;
            }
            rowB++;
        }
        return reducedMatrix;
    }

    public static int[] computeBaseIndices(int altsCount) {
        int[] result = new int[altsCount];
        for (int i = 0; i < altsCount; i++) {
            result[i] = i;
        }
        return result;
    }

    public static int[] computeModifiedIndices(int[] altsIndices, int[] removedAxes) {
        int newAltsCount = altsIndices.length - 2;
        int[] result = new int[newAltsCount];
        for (int i = 0, j = 0; i < altsIndices.length; i++) {
            if (isAxisRemoved(i, removedAxes)) {
                continue;
            }
            result[j] = altsIndices[i];
            j++;
        }
        return result;
    }

    public static String[] computeKemeniMedian(List<Integer[][]> pairCompMatrices) {
        int altsCount = pairCompMatrices.get(0).length;
        int expertsCount = pairCompMatrices.size();
        int[] altsIndices = computeBaseIndices(altsCount);
        String[] ranking = new String[altsCount];
        int iterationNumber = 0;
        do {
            Integer[][] maxMatrix = computeMaxMatrix(pairCompMatrices);
            out.printf("Матрица максимальных элементов Amax (итерация %d): %n%s%n%n", iterationNumber + 1,
                    convertMatrixToString(maxMatrix));
            Integer[][] lossMatrix = computeLossMatrix(pairCompMatrices, maxMatrix);
            out.printf("Матрица потерь P (итерация %d): %n%s%n%n", iterationNumber + 1,
                    convertMatrixToString(lossMatrix));
            Integer[][] modifiedLossMatrix = computeModifiedLossMatrix(lossMatrix);
            out.printf("Матрица Q (итерация %d): %n%s%n%n", iterationNumber + 1,
                    convertMatrixToString(modifiedLossMatrix));
            int[] removedAxes = findMatrixMinElementIndicies(modifiedLossMatrix);
            out.printf("Удаляемые решения k и l (итерация %d): %d, %d%n%n", iterationNumber + 1,
                    altsIndices[removedAxes[0]] + 1,
                    altsIndices[removedAxes[1]] + 1);
            ranking[iterationNumber] = String.format("X%d", altsIndices[removedAxes[1]] + 1);
            ranking[ranking.length - 1 - iterationNumber] = String.format("X%d", altsIndices[removedAxes[0]] + 1);
            altsIndices = computeModifiedIndices(altsIndices, removedAxes);
            if (altsIndices.length == 0) {
                break;
            } else if (altsIndices.length == 1) {
                ranking[iterationNumber + 1] = String.format("X%d", altsIndices[0] + 1);
                break;
            }
            for (int i = 0; i < expertsCount; i++) {
                Integer[][] pairCompMatrix = pairCompMatrices.get(i);
                Integer[][] reducedMatrix = reduceMatrix(pairCompMatrix, removedAxes);
                pairCompMatrices.set(i, reducedMatrix);
                out.printf("Матрица парных сравнений для эксперта %d (итерация %d): %n%s%n%n", i + 1,
                        iterationNumber + 1, convertMatrixToString(reducedMatrix, altsIndices));
            }
            iterationNumber++;
        } while (true);
        return ranking;
    }

    public static String convertMatrixToString(Integer[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(String.format("%4s", matrix[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String convertMatrixToString(Integer[][] matrix, int[] indices) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = 0; i < indices.length; i++) {
            sb.append(String.format("%4s", indices[i] + 1));
        }
        sb.append("\n");
        for (int i = 0; i < matrix.length; i++) {
            sb.append(indices[i] + 1 + " ");
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(String.format("%4s", matrix[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String ranking1 = "x2, x4, x1, x3, x5, x8, x7, x6";
        String ranking2 = "x1, x5, x4~x2, x6, x7~x8, x3";
        String ranking3 = "x3, x4, x8~x7, x6, x5, x2, x1";
        String ranking4 = "x7, x8, x3~x4, x6~x2, x1, x5";
        String ranking5 = "x4, x8, x3~x7, x6~x1, x2, x5";
        List<Integer[][]> pairCompMatrices = new ArrayList<Integer[][]>();
        Collections.addAll(pairCompMatrices,
                convertRankingToPairCompMatrix(ranking1),
                convertRankingToPairCompMatrix(ranking2),
                convertRankingToPairCompMatrix(ranking3),
                convertRankingToPairCompMatrix(ranking4),
                convertRankingToPairCompMatrix(ranking5)
        );
        String[] finalRanking = GroupChoiceProblem.computeKemeniMedian(pairCompMatrices);
        out.println("Итоговое групповое ранжирование R для 1-го варианта: " + Arrays.toString(finalRanking));
    }
}