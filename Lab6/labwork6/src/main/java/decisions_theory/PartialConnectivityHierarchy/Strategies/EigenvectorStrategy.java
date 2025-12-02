package decisions_theory.PartialConnectivityHierarchy.Strategies;

import decisions_theory.PartialConnectivityHierarchy.PairCompMatrices.PairCompMatrix;

public interface EigenvectorStrategy {
    public double[] execute(PairCompMatrix pairCompMatrix);
}