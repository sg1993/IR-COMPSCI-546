package retriever.inferencenetwork;

public class WeightedAndBeliefNode extends BeliefNode {

    private double[] weights = null;

    public WeightedAndBeliefNode(double[] weights) {
        this.weights = weights;
    }

    @Override
    public Double score(int docId) {
        double score = 0.0;
        int i = 0;

        // weighted-and could be done computed by exponentiating
        // child's score (in probability-space) by the weights
        // or multiplying child's score (in log-space) by the weights.
        // we'll go with the latter one here
        for (QueryNode child : children) {
            score += (weights[i] * child.score(docId));
            i++;
        }

        // the score we have is already in log-space; just return it
        return score;
    }

}
