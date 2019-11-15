package inferencenetwork;

public class AndBeliefNode extends BeliefNode {

    public AndBeliefNode() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public double score(int docId) {
        double score = 1.0;

        for (QueryNode child : children) {
            score *= Math.exp(child.score(docId));
        }
        // return scores in log-space
        return Math.log(score);
    }

}
