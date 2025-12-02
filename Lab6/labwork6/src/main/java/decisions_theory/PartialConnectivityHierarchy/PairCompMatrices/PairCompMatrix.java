package decisions_theory.PartialConnectivityHierarchy.PairCompMatrices;

import java.util.Arrays;
import java.util.List;

import decisions_theory.PartialConnectivityHierarchy.Hierarchy.HierarchyMatrix;
import decisions_theory.PartialConnectivityHierarchy.Strategies.EigenvectorStrategy;

public abstract class PairCompMatrix {
    protected final double[][] data;
    protected final HierarchyMatrix hierarchyMatrix;
    protected EigenvectorStrategy eigenvectorStrategy;

    public PairCompMatrix(double[][] pairCompMatrix, HierarchyMatrix hierarchyMatrix,
            EigenvectorStrategy eigenvectorStrategy) {
        this.data = pairCompMatrix;
        this.hierarchyMatrix = hierarchyMatrix;
        this.eigenvectorStrategy = eigenvectorStrategy;
    }

    public double[][] getData() {
        return data;
    }

    public HierarchyMatrix getHierarchyMatrix() {
        return hierarchyMatrix;
    }

    public abstract int getHierarchyLayerElementsCount();

    public abstract List<Integer> findHierarchyElementsIndices();

    public double[] computeEigenvector() {
        return eigenvectorStrategy.execute(this);
    }

    public double[] computeEigenvectorQt() {
        double[] eigenvector = computeEigenvector();
        double[] eigenvectorQt = new double[eigenvector.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                eigenvectorQt[i] += data[i][j] * eigenvector[j];
            }
        }
        return eigenvectorQt;
    }

    public double[] computeEigenvectorDoubleQt() {
        double[] eigenvector = computeEigenvector();
        double[] eigenvectorQt = computeEigenvectorQt();
        double[] eigenvectorDoubleQt = new double[eigenvector.length];
        for (int i = 0; i < eigenvector.length; i++) {
            if (Math.abs(eigenvector[i]) < 1e-6) {
                eigenvectorDoubleQt[i] = 0;
            } else {
                eigenvectorDoubleQt[i] = eigenvectorQt[i] / eigenvector[i];
            }
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
        // DEBUG OUTPUT
        System.out.println("EigenvectorDoubleQt: " + Arrays.toString(eigenvectorDoubleQt));
        System.out.println("Lambda max: " + result);
        return result;
    }

    public double computeConsistencyIndex() {
        return (computeLambdaMax() - data.length) / (data.length - 1);
    }

    public boolean verifyConsistency() {
        return Math.abs(computeLambdaMax() - data.length) < 0.5;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : data) {
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