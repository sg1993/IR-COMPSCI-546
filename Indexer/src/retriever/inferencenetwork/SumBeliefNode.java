package retriever.inferencenetwork;

// normalized sum node
public class SumBeliefNode extends BeliefNode {

    @Override
    public Double score(int docId) {
        double score = 0.0;

        for (QueryNode child : children) {
            score += Math.exp(child.score(docId));
        }
        score /= children.size();

        // return the score in log-space
        return Math.log(score);
    }

}
