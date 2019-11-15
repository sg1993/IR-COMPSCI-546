package inferencenetwork;

// normalized sum node
public class SumBeliefNode extends BeliefNode {

    public SumBeliefNode() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Double score(int docId) {
        double score = 0.0;

        for (QueryNode child : children) {
            score += Math.exp(child.score(docId));
        }
        score /= children.size();
        return Math.log(score);
    }

}
