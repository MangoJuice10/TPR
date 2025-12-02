package decisions_theory.PartialConnectivityHierarchy.Hierarchy;

public class HierarchyMatrix {
    private final double[][] data;
    private final int criteriaCount;
    private final int altsCount;

    public HierarchyMatrix(double[][] hierarchyMatrix, int criteriaCount, int altsCount) {
        this.data = hierarchyMatrix;
        this.criteriaCount = criteriaCount;
        this.altsCount = altsCount;
    }

    public double[][] getData() {
        return data;
    }

    public int getCriteriaCount() {
        return criteriaCount;
    }

    public int getAltsCount() {
        return altsCount;
    }
}
