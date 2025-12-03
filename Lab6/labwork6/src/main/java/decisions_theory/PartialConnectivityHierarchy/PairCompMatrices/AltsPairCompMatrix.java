package decisions_theory.PartialConnectivityHierarchy.PairCompMatrices;

import decisions_theory.PartialConnectivityHierarchy.Hierarchy.HierarchyMatrix;
import decisions_theory.PartialConnectivityHierarchy.Strategies.EigenvectorStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AltsPairCompMatrix extends PairCompMatrix {
    private int criterionIdx;

    public AltsPairCompMatrix(double[][] altsPairCompMatrix, HierarchyMatrix hierarchyMatrix, int criterionIdx,
            EigenvectorStrategy eigenvectorStrategy) {
        super(altsPairCompMatrix, hierarchyMatrix, eigenvectorStrategy);
        this.criterionIdx = criterionIdx;
    }

    public double[] computeModifiedEigenvector() {
        double[] eigenvector = computeEigenvector();
        List<Integer> hierarchyElementsIndices = findHierarchyElementsIndices();
        double[] modifiedEigenvector = new double[getHierarchyLayerElementsCount()];
        for (int i = 0; i < eigenvector.length; i++) {
            int layerIndex = hierarchyElementsIndices
                    .stream()
                    .map(index -> (index - 1 - hierarchyMatrix.getCriteriaCount()))
                    .toList()
                    .get(i);
            modifiedEigenvector[layerIndex] = eigenvector[i];
        }
        return modifiedEigenvector;
    }

    public int getCriterionIdx() {
        return criterionIdx;
    }

    public int getHierarchyLayerElementsCount() {
        return hierarchyMatrix.getAltsCount();
    }

    public List<Integer> findHierarchyElementsIndices() {
        double[][] hierarchyData = hierarchyMatrix.getData();
        List<Integer> altsIndices = new ArrayList<Integer>();
        for (int j = 0; j < hierarchyData.length; j++) {
            if (hierarchyData[criterionIdx][j] == 1) {
                altsIndices.add(j);
            }
        }
        return altsIndices;
    }
}