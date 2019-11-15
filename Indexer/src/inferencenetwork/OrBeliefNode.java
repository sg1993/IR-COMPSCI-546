package inferencenetwork;

public class OrBeliefNode extends BeliefNode {

    public OrBeliefNode() {
        super();
    }

    public double score(int docId) {
        double score = 0.0;
        for (QueryNode child : children) {
            score *= (1.0 - Math.exp(child.score(docId)));
        }
        // return scores in log-space
        return Math.log(1.0 - score);
    }
}
