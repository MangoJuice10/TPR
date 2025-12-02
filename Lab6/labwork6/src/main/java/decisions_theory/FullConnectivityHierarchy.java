package decisions_theory;

import java.util.Collections;
import java.util.List;

import java.util.ArrayList;

import static java.lang.System.out;

interface EigenvectorStrategy {
    public double[] execute(double[][] compMatrix);
}

class MethodOneEigenvectorStrategy implements EigenvectorStrategy {
    private double[] getRowSums(double[][] matrix) {
        double[] rowSums = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                rowSums[i] += matrix[i][j];
            }
        }
        return rowSums;
    }

    private double getMatrixSum(double[][] matrix) {
        double sum = 0.0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    private boolean verifyEigenvectorConsistency(double[] eigenvector) {
        double weightsSum = 0;
        for (int i = 0; i < eigenvector.length; i++) {
            weightsSum += eigenvector[i];
        }
        return Math.abs(weightsSum - 1) < 1e-6;
    }

    public double[] execute(double[][] matrix) {
        double[] eigenvector = new double[matrix.length];
        double[] rowSums = getRowSums(matrix);
        double matrixSum = getMatrixSum(matrix);
        for (int i = 0; i < rowSums.length; i++) {
            eigenvector[i] = rowSums[i] / matrixSum;
        }
        if (verifyEigenvectorConsistency(eigenvector)) {
            return eigenvector;
        }
        return null;
    }
}

abstract class PairCompMatrix {
    protected final double[][] matrix;
    EigenvectorStrategy eigenvectorStrategy;

    public PairCompMatrix(double[][] compMatrix, EigenvectorStrategy eigenvectorStrategy) {
        this.matrix = compMatrix;
        this.eigenvectorStrategy = eigenvectorStrategy;
    }

    public double[] computeEigenvector() {
        return eigenvectorStrategy.execute(matrix);
    }

    public double[] computeEigenvectorQt() {
        double[] eigenvector = computeEigenvector();
        double[] eigenvectorQt = new double[eigenvector.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                eigenvectorQt[i] += matrix[i][j] * eigenvector[j];
            }
        }
        return eigenvectorQt;
    }

    public double[] computeEigenvectorDoubleQt() {
        double[] eigenvector = computeEigenvector();
        double[] eigenvectorQt = computeEigenvectorQt();
        double[] eigenvectorDoubleQt = new double[eigenvector.length];
        for (int i = 0; i < eigenvector.length; i++) {
            eigenvectorDoubleQt[i] = eigenvectorQt[i] / eigenvector[i];
        }
        return eigenvectorDoubleQt;
    }

    public double computeLambdaMax() {
        double result = 0;
        double[] eigenvectorDoubleQt = computeEigenvectorDoubleQt();
        for (int i = 0; i < eigenvectorDoubleQt.length; i++) {
            result += eigenvectorDoubleQt[i];
        }
        result /= eigenvectorDoubleQt.length;
        return result;
    }

    public double computeConsistencyIndex() {
        return (computeLambdaMax() - matrix.length) / (matrix.length - 1);
    }

    public boolean verifyConsistency() {
        return Math.abs(computeLambdaMax() - matrix.length) < 0.5;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            sb.append("|");
            for (double pref : row) {
                sb.append(String.format("%8.2f", pref));
            }
            sb.append(String.format("%5s%n", "|"));
        }
        return sb.toString();
    }

    public String eigenvectorToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (double weight : computeEigenvector()) {
            sb.append(String.format("%8.2f", weight));
        }
        sb.append(String.format("%5s", "]"));
        return sb.toString();
    }
}

class CriteriaPairCompMatrix extends PairCompMatrix {
    public CriteriaPairCompMatrix(double[][] matrix, EigenvectorStrategy eigenvector) {
        super(matrix, eigenvector);
    }
}

class AltsPairCompMatrix extends PairCompMatrix {
    private int criterionIdx;

    public AltsPairCompMatrix(double[][] matrix, int criterionIdx, EigenvectorStrategy eigenvector) {
        super(matrix, eigenvector);
        this.criterionIdx = criterionIdx;
    }

    public int getCriterionIdx() {
        return criterionIdx;
    }
}

public class FullConnectivityHierarchy {
    public static CriteriaPairCompMatrix initCriteriaPairCompMatrix(
            double[][] criteriaMatrixData, EigenvectorStrategy eigenvectorStrategy) {
        return new CriteriaPairCompMatrix(criteriaMatrixData, eigenvectorStrategy);
    }

    public static List<AltsPairCompMatrix> initAltsPairCompMatrices(
            List<double[][]> altsPairCompMatricesData,
            EigenvectorStrategy eigenvectorStrategy) {
        List<AltsPairCompMatrix> altsPairCompMatrices = new ArrayList<AltsPairCompMatrix>();
        for (int idx = 0; idx < altsPairCompMatricesData.size(); idx++) {
            double[][] altsMatrixData = altsPairCompMatricesData.get(idx);
            AltsPairCompMatrix altsMatrix = new AltsPairCompMatrix(altsMatrixData, idx + 1,
                    eigenvectorStrategy);
            altsPairCompMatrices.add(altsMatrix);
        }
        return altsPairCompMatrices;
    }

    public static double[] computeAdditiveAggregations(CriteriaPairCompMatrix criteriaMatrix,
            List<AltsPairCompMatrix> altsPairCompMatrices) {
        int altsCount = altsPairCompMatrices.size();
        int criteriaCount = criteriaMatrix.matrix.length;

        double[] criteriaMatrixEigenvector = criteriaMatrix.computeEigenvector();

        double[] additiveAggregations = new double[altsCount];
        for (int j = 0; j < altsCount; j++) {
            double additiveAggregation = 0.0;
            for (int i = 0; i < criteriaCount; i++) {
                additiveAggregation += criteriaMatrixEigenvector[i]
                        * altsPairCompMatrices.get(j).computeEigenvector()[i];
                additiveAggregations[j] = additiveAggregation;
            }
        }
        return additiveAggregations;
    }

    public static void printAltWithAdditiveAggregation(String altName, double additiveAggregation) {
        out.printf("Альтернатива \"%s\": Dj = %f%n", altName, additiveAggregation);
    }

    public static void printAltsWithAdditiveAggregations(String[] altsNames, double[] additiveAggregations) {
        for (int i = 0; i < additiveAggregations.length; i++) {
            printAltWithAdditiveAggregation(altsNames[i], additiveAggregations[i]);
        }
    }

    public static void printCriteriaPairCompMatrixWithProps(CriteriaPairCompMatrix criteriaMatrix) {
        out.printf("Матрица парных сравнений критериев A:%n%s%n", criteriaMatrix);
        printCriteriaPairCompMatrixEigenvector(criteriaMatrix);
        out.printf("Lambda max матрицы парных сравнений критериев A: %.2f%n%n", criteriaMatrix.computeLambdaMax());
        out.printf("Индекс согласованности матрицы парных сравнений критериев A: %.2f%n%n", criteriaMatrix.computeConsistencyIndex());
    }

    public static void printCriteriaPairCompMatrixEigenvector(CriteriaPairCompMatrix criteriaMatrix) {
        out.println("Собственный вектор матрицы парных сравнений критериев w:\n"
                + criteriaMatrix.eigenvectorToString() + "\n");
    }

    public static void printAltMatrixWithProps(AltsPairCompMatrix altsMatrix) {
        out.printf("Матрица парных сравнений решений A%d:%n%s%n", altsMatrix.getCriterionIdx(),
                altsMatrix);
        printAltsPairCompMatrixEigenvector(altsMatrix);
        out.printf("%nLambda max матрицы парных сравнений решений A%d: %.2f%n%n", altsMatrix.getCriterionIdx(), altsMatrix.computeLambdaMax());
        out.printf("Индекс согласованности матрицы парных сравнений решений A%d: %.2f%n", altsMatrix.getCriterionIdx(), altsMatrix.computeConsistencyIndex());
    }

    public static void printAltsPairCompMatrixEigenvector(AltsPairCompMatrix altsMatrix) {
        out.println("Собственный вектор матрицы парных сравнений решений w"
                + altsMatrix.getCriterionIdx() + ":\n" + altsMatrix.eigenvectorToString());
    }

    public static void printAltsPairCompMatricesWithProps(List<AltsPairCompMatrix> altsPairCompMatrices) {
        out.println("-".repeat(65));
        for (AltsPairCompMatrix altsMatrix : altsPairCompMatrices) {
            printAltMatrixWithProps(altsMatrix);
            out.println("-".repeat(65));
        }
    }

    public static void main(String[] args) {
        String[] criteriaNames = new String[] {
                "Фундаментальность знаний",
                "Соответствие уровню развитие науки",
                "Профессиональная применимость",
                "Симпатия к преподавателю"
        };

        String[] altsNames = new String[] {
                "Теория принятия решений",
                "Теория алгоритмов",
                "Теория вероятностей и математическая статистика",
                "Теория информационных процессов",
                "Технологии обработки информации",
                "Технологии программирования"
        };

        double[][] A = new double[][] {
                { 1, 3, 2, 5 },
                { 1.0 / 3, 1, 1.0 / 2, 3 },
                { 1.0 / 2, 2, 1, 4 },
                { 1.0 / 5, 1.0 / 3, 1.0 / 4, 1 }
        };

        double[][] A1 = {
                { 1, 1.0 / 2, 1.0 / 2, 2, 3, 4 },
                { 2, 1, 1, 3, 4, 5 },
                { 2, 1, 1, 3, 4, 5 },
                { 1.0 / 2, 1.0 / 3, 1.0 / 3, 1, 2, 3 },
                { 1.0 / 3, 1.0 / 4, 1.0 / 4, 1.0 / 2, 1, 2 },
                { 1.0 / 4, 1.0 / 5, 1.0 / 5, 1.0 / 3, 1.0 / 2, 1 }
        };

        double[][] A2 = {
                { 1, 1.0 / 3, 1.0 / 2, 1.0 / 2, 1.0 / 4, 1.0 / 5 },
                { 3, 1, 2, 2, 1, 1.0 / 2 },
                { 2, 1.0 / 2, 1, 1, 1.0 / 2, 1.0 / 3 },
                { 2, 1.0 / 2, 1, 1, 1, 1.0 / 2 },
                { 4, 1, 2, 1, 1, 1.0 / 2 },
                { 5, 2, 3, 2, 2, 1 }
        };

        double[][] A3 = {
                { 1, 1.0 / 2, 1, 1.0 / 3, 1.0 / 3, 1.0 / 4 },
                { 2, 1, 2, 1, 1, 1.0 / 2 },
                { 1, 1.0 / 2, 1, 1.0 / 2, 1, 1.0 / 3 },
                { 3, 1, 2, 1, 2, 1 },
                { 3, 1, 1, 1.0 / 2, 1, 1.0 / 2 },
                { 4, 2, 3, 1, 2, 1 }
        };

        double[][] A4 = {
                { 1, 3, 2, 4, 5, 3 },
                { 1.0 / 3, 1, 1.0 / 2, 2, 3, 1 },
                { 1.0 / 2, 2, 1, 3, 4, 2 },
                { 1.0 / 4, 1.0 / 2, 1.0 / 3, 1, 2, 1 },
                { 1.0 / 5, 1.0 / 3, 1.0 / 4, 1.0 / 2, 1, 1.0 / 2 },
                { 1.0 / 3, 1, 1.0 / 2, 1, 2, 1 }
        };

        List<double[][]> altsMatrixData = new ArrayList<>();
        Collections.addAll(altsMatrixData, A1, A2, A3, A4);

        EigenvectorStrategy methodOne = new MethodOneEigenvectorStrategy();
        CriteriaPairCompMatrix criteriaMatrix = initCriteriaPairCompMatrix(A, methodOne);
        printCriteriaPairCompMatrixWithProps(criteriaMatrix);

        if (criteriaMatrix.verifyConsistency()) {
            List<AltsPairCompMatrix> altsPairCompMatrices = initAltsPairCompMatrices(altsMatrixData, methodOne);
            for (AltsPairCompMatrix altsPairCompMatrix : altsPairCompMatrices) {
                if (!altsPairCompMatrix.verifyConsistency()) {
                    out.printf("Матрица парных сравнений альтернатив A%d не является согласованной!", altsPairCompMatrix.getCriterionIdx());
                    return;
                }
            }
            printAltsPairCompMatricesWithProps(altsPairCompMatrices);

            double[] additiveAggregations = computeAdditiveAggregations(criteriaMatrix, altsPairCompMatrices);
            printAltsWithAdditiveAggregations(altsNames, additiveAggregations);
            
            int efficientAltIdx = 0;
            for (int i = 0; i < additiveAggregations.length; i++) {
                if (additiveAggregations[i] > additiveAggregations[efficientAltIdx]) {
                    efficientAltIdx = i;
                }
            }
            out.printf("%nЦель принятия решений:%n");
            printAltWithAdditiveAggregation(altsNames[efficientAltIdx], additiveAggregations[efficientAltIdx]);
        } else {
            out.println("Матрица парных сравнений критериев не является согласованной!");
            return;
        }
    }
}