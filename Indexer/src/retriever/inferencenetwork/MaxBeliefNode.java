package retriever.inferencenetwork;

public class MaxBeliefNode extends BeliefNode {

    @Override
    public Double score(int docId) {
        double score = Double.MIN_VALUE;

        for (QueryNode child : children) {
            score = Math.max(score, Math.exp(child.score(docId)));
        }
        // return scores in log-space
        return Math.log(score);
    }

}
