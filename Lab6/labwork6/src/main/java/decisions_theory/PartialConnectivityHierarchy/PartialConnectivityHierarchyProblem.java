package decisions_theory.PartialConnectivityHierarchy;

import java.util.Collections;
import java.util.List;

import decisions_theory.PartialConnectivityHierarchy.Hierarchy.HierarchyMatrix;
import decisions_theory.PartialConnectivityHierarchy.PairCompMatrices.AltsPairCompMatrix;
import decisions_theory.PartialConnectivityHierarchy.PairCompMatrices.CriteriaPairCompMatrix;
import decisions_theory.PartialConnectivityHierarchy.Strategies.EigenvectorStrategy;
import decisions_theory.PartialConnectivityHierarchy.Strategies.MethodOneEigenvectorStrategy;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.System.out;

public class PartialConnectivityHierarchyProblem {
    public static CriteriaPairCompMatrix initCriteriaPairCompMatrix(
            double[][] criteriaMatrixData, HierarchyMatrix hierarchyMatrix, int criteriaCount, int altsCount,
            EigenvectorStrategy eigenvectorStrategy) {
        return new CriteriaPairCompMatrix(criteriaMatrixData, hierarchyMatrix, eigenvectorStrategy);
    }

    public static List<AltsPairCompMatrix> initAltsPairCompMatrices(
            List<double[][]> altsPairCompMatricesData, HierarchyMatrix hierarchyMatrix, int criteriaCount,
            int altsCount, EigenvectorStrategy eigenvectorStrategy) {
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
        int criteriaCount = criteriaMatrix.getData().length;
        int altsCount = altsPairCompMatrices.get(0).getHierarchyLayerElementsCount();

        double[] criteriaMatrixEigenvector = criteriaMatrix.computeEigenvector();

        double[] additiveAggregations = new double[altsCount];
        for (int j = 0; j < altsCount; j++) {
            double additiveAggregation = 0.0;
            for (int i = 0; i < criteriaCount; i++) {
                additiveAggregation += criteriaMatrixEigenvector[i]
                        * altsPairCompMatrices.get(i).computeModifiedEigenvector()[j];
                additiveAggregations[j] = additiveAggregation;
            }
        }
        return additiveAggregations;
    }

    public static List<Integer> findEfficientAltsIndices(double[] additiveAggregations) {
        List<Integer> efficientAltsIndices = new ArrayList<Integer>();

        int efficientAltIdx = 0;
        for (int i = 0; i < additiveAggregations.length; i++) {
            if (additiveAggregations[i] > additiveAggregations[efficientAltIdx]) {
                efficientAltIdx = i;
            }
        }

        for (int i = 0; i < additiveAggregations.length; i++) {
            if (additiveAggregations[i] == additiveAggregations[efficientAltIdx]) {
                efficientAltsIndices.add(i);
            }
        }

        return efficientAltsIndices;
    }

    public static void printAlt(String altName) {
        out.printf("Альтернатива \"%s\"%n", altName);
    }

    public static void printAlts(String[] altNames, List<Integer> altIndices) {
        for (int altIdx : altIndices) {
            printAlt(altNames[altIdx]);
        }
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
            "Альтернатива 1",
            "Альтернатива 2",
            "Альтернатива 3",
            "Альтернатива 4",
        };

        double[][] H = new double[][] {
                //0  1  2  3  4  5  6  7
                { 0, 1, 1, 1, 0, 0, 0, 0 }, // 0 Цель → все свойства
                { 0, 0, 0, 0, 1, 1, 0, 0 }, // 1 Свойство 1 → Решение 1, Решение 2
                { 0, 0, 0, 0, 1, 1, 1, 0 }, // 2 Свойство 2 → Решение 1, Решение 2, Решение 3
                { 0, 0, 0, 0, 0, 1, 1, 1 }, // 3 Свойство 3 → Решение 2, Решение 3, Решение 4
                { 0, 0, 0, 0, 0, 0, 0, 0 }, // Решение 1
                { 0, 0, 0, 0, 0, 0, 0, 0 }, // Решение 2
                { 0, 0, 0, 0, 0, 0, 0, 0 }, // Решение 3
                { 0, 0, 0, 0, 0, 0, 0, 0 }, // Решение 4
        };

        double[][] A = new double[][] {
                { 1, 3, 2},
                { 1.0 / 3, 1, 1.0 / 2},
                { 1.0 / 2, 2, 1},
        };

        int criteriaCount = A.length;
        int altsCount = H.length - criteriaCount - 1;

        double[][] A1 = new double[][] { // Свойство 1 → A1, A2
                { 1, 3 },
                { 1.0 / 3, 1 }
        };

        double[][] A2 = new double[][] { // Свойство 2 → A1, A2, A3
                { 1, 2, 4 },
                { 1.0 / 2, 1, 3 },
                { 1.0 / 4, 1.0 / 3, 1 }
        };

        double[][] A3 = new double[][] { // Свойство 3 → A2, A3, A4
                { 1, 2, 1.0 / 4 },
                { 1.0 / 2, 1, 1.0 / 2 },
                { 4, 2, 1 }
        };

        HierarchyMatrix hierarchyMatrix = new HierarchyMatrix(H, criteriaCount, altsCount);

        List<double[][]> altsMatrixData = new ArrayList<>();
        Collections.addAll(altsMatrixData, A1, A2, A3);

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

            List<Integer> efficientAltsIndices = findEfficientAltsIndices(additiveAggregations);
            out.printf("%nПредпочтительные решения:%n");
            printAlts(altsNames, efficientAltsIndices);
        } else {
            out.println("Матрица парных сравнений критериев не является согласованной!");
            return;
        }
    }
}