package retriever.inferencenetwork;

public class AndBeliefNode extends BeliefNode {

    @Override
    public Double score(int docId) {
        double score = 0.0;

        for (QueryNode child : children) {
            // add up the score in log-space
            score += child.score(docId);
        }
        // return the score which is already in log-space
        return score;
    }

}
