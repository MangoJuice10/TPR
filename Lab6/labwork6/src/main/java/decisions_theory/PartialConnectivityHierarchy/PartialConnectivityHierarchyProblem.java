package decisions_theory.PartialConnectivityHierarchy;
import java.util.Collections;
import java.util.List;

import decisions_theory.PartialConnectivityHierarchy.Hierarchy.HierarchyMatrix;
import decisions_theory.PartialConnectivityHierarchy.PairCompMatrices.AltsPairCompMatrix;
import decisions_theory.PartialConnectivityHierarchy.PairCompMatrices.CriteriaPairCompMatrix;
import decisions_theory.PartialConnectivityHierarchy.Strategies.EigenvectorStrategy;
import decisions_theory.PartialConnectivityHierarchy.Strategies.MethodOneEigenvectorStrategy;

import java.util.ArrayList;

import static java.lang.System.out;

public class PartialConnectivityHierarchyProblem {
    public static CriteriaPairCompMatrix initCriteriaPairCompMatrix(
            double[][] criteriaMatrixData, HierarchyMatrix hierarchyMatrix, int criteriaCount, int altsCount, EigenvectorStrategy eigenvectorStrategy) {
        return new CriteriaPairCompMatrix(criteriaMatrixData, hierarchyMatrix, eigenvectorStrategy);
    }

    public static List<AltsPairCompMatrix> initAltsPairCompMatrices(
            List<double[][]> altsPairCompMatricesData, HierarchyMatrix hierarchyMatrix, int criteriaCount, int altsCount, EigenvectorStrategy eigenvectorStrategy) {
        List<AltsPairCompMatrix> altsPairCompMatrices = new ArrayList<AltsPairCompMatrix>();
        for (int idx = 0; idx < altsPairCompMatricesData.size(); idx++) {
            double[][] altsMatrixData = altsPairCompMatricesData.get(idx);
            AltsPairCompMatrix altsMatrix = new AltsPairCompMatrix(altsMatrixData, hierarchyMatrix, 1 + idx,
                    eigenvectorStrategy);
            altsPairCompMatrices.add(altsMatrix);
        }
        return altsPairCompMatrices;
    }

    public static double[] computeAdditiveAggregations(CriteriaPairCompMatrix criteriaMatrix,
            List<AltsPairCompMatrix> altsPairCompMatrices) {
        int altsCount = altsPairCompMatrices.size();
        int criteriaCount = criteriaMatrix.getData().length;

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
        out.printf("Индекс согласованности матрицы парных сравнений критериев A: %.2f%n%n",
                criteriaMatrix.computeConsistencyIndex());
    }

    public static void printCriteriaPairCompMatrixEigenvector(CriteriaPairCompMatrix criteriaMatrix) {
        out.println("Собственный вектор матрицы парных сравнений критериев w:\n"
                + criteriaMatrix.eigenvectorToString() + "\n");
    }

    public static void printAltMatrixWithProps(AltsPairCompMatrix altsMatrix) {
        out.printf("Матрица парных сравнений решений A%d:%n%s%n", altsMatrix.getCriterionIdx(),
                altsMatrix);
        printAltsPairCompMatrixEigenvector(altsMatrix);
        out.printf("%nLambda max матрицы парных сравнений решений A%d: %.2f%n%n", altsMatrix.getCriterionIdx(),
                altsMatrix.computeLambdaMax());
        out.printf("Индекс согласованности матрицы парных сравнений решений A%d: %.2f%n", altsMatrix.getCriterionIdx(),
                altsMatrix.computeConsistencyIndex());
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

        double[][] H = new double[][] {
                // 1 2 3 4 5 6 7 8 9 10 11
                { 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 }, // 1 Цель
                { 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, // 2 Критерий 1
                { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0 }, // 3 Критерий 2
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // 4 Критерий 3
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, // 5 Критерий 4
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 6 Решение 1
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 7 Решение 2
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 8 Решение 3
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 9 Решение 4
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 10 Решение 5
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } // 11 Решение 6
        };

        double[][] A = new double[][] {
                { 1, 3, 2, 5 },
                { 1.0 / 3, 1, 1.0 / 2, 3 },
                { 1.0 / 2, 2, 1, 4 },
                { 1.0 / 5, 1.0 / 3, 1.0 / 4, 1 }
        };

        final int criteriaCount = A.length;
        final int altsCount = H.length - criteriaCount - 1;

        double[][] A1 = {
                { 1, 2 },
                { 1.0 / 2, 1 }
        };

        double[][] A2 = {
                { 1, 3 },
                { 1.0 / 3, 1 }
        };

        double[][] A3 = {
                { 1 }
        };

        double[][] A4 = {
                { 1 }
        };

        HierarchyMatrix hierarchyMatrix = new HierarchyMatrix(H, criteriaCount, altsCount);

        List<double[][]> altsMatrixData = new ArrayList<>();
        Collections.addAll(altsMatrixData, A1, A2, A3, A4);

        EigenvectorStrategy methodOne = new MethodOneEigenvectorStrategy();

        CriteriaPairCompMatrix criteriaMatrix = initCriteriaPairCompMatrix(A, hierarchyMatrix, criteriaCount,
                altsCount, methodOne);
        printCriteriaPairCompMatrixWithProps(criteriaMatrix);

        if (criteriaMatrix.verifyConsistency()) {
            List<AltsPairCompMatrix> altsPairCompMatrices = initAltsPairCompMatrices(altsMatrixData, hierarchyMatrix,
                    criteriaCount, altsCount, methodOne);
            for (AltsPairCompMatrix altsPairCompMatrix : altsPairCompMatrices) {
                if (!altsPairCompMatrix.verifyConsistency()) {
                    out.printf("Матрица парных сравнений альтернатив A%d не является согласованной!",
                            altsPairCompMatrix.getCriterionIdx());
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