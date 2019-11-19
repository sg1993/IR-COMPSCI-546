package retriever.inferencenetwork;

public class AndBeliefNode extends BeliefNode {

    @Override
    public Double score(int docId) {
        double score = 1.0;

        for (QueryNode child : children) {
            score *= Math.exp(child.score(docId));
        }
        // return scores in log-space
        return Math.log(score);
    }

}
