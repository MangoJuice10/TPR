package decisions_theory.PartialConnectivityHierarchy.PairCompMatrices;

import java.util.ArrayList;
import java.util.List;

import decisions_theory.PartialConnectivityHierarchy.Hierarchy.HierarchyMatrix;
import decisions_theory.PartialConnectivityHierarchy.Strategies.EigenvectorStrategy;

public class CriteriaPairCompMatrix extends PairCompMatrix {
    public CriteriaPairCompMatrix(double[][] criteriaPairCompMatrix, HierarchyMatrix hierarchyMatrix, EigenvectorStrategy eigenvectorStrategy) {
        super(criteriaPairCompMatrix, hierarchyMatrix, eigenvectorStrategy);
    }

    public int getHierarchyLayerElementsCount() {
        return hierarchyMatrix.getCriteriaCount();
    }

    public List<Integer> findHierarchyElementsIndices() {
        List<Integer> altsIndices = new ArrayList<Integer>();
        for (int i = 0; i < data.length; i++) {
            altsIndices.add(1 + i);
        }
        return altsIndices;
    }
}