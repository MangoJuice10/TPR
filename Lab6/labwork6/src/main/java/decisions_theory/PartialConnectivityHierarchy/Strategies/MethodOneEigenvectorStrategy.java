package decisions_theory.PartialConnectivityHierarchy.Strategies;

import java.util.Arrays;

import decisions_theory.PartialConnectivityHierarchy.PairCompMatrices.PairCompMatrix;

public class MethodOneEigenvectorStrategy implements EigenvectorStrategy {
    private double[] getRowSums(PairCompMatrix pairCompMatrix) {
        double[][] data = pairCompMatrix.getData();
        double[] rowSums = new double[pairCompMatrix.getData().length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                rowSums[i] += data[i][j];
            }
        }
        return rowSums;
    }

    private double getMatrixSum(PairCompMatrix pairCompMatrix) {
        double[][] data = pairCompMatrix.getData();

        double sum = 0.0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                sum += data[i][j];
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

    public double[] execute(PairCompMatrix pairCompMatrix) {
        double[] eigenvector = new double[pairCompMatrix.getData().length];
        double[] rowSums = getRowSums(pairCompMatrix);
        double matrixSum = getMatrixSum(pairCompMatrix);
        for (int i = 0; i < rowSums.length; i++) {
            eigenvector[i] = rowSums[i] / matrixSum;
        }
        if (verifyEigenvectorConsistency(eigenvector)) {
            return eigenvector;
        }
        return null;
    }
}