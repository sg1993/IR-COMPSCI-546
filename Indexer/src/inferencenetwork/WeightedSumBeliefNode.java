package inferencenetwork;

public class WeightedSumBeliefNode extends BeliefNode {

    private double[] weights = null;

    public WeightedSumBeliefNode(double[] weights) {
        this.weights = weights;
    }

    @Override
    public Double score(int docId) {
        double score = 0.0, wsum = 0.0;
        int i = 0;

        for (QueryNode child : children) {
            score += weights[i] * Math.exp(Math.exp(child.score(docId)));
            wsum += weights[i++];
        }
        score /= wsum;
        return Math.log(score);
    }

}
