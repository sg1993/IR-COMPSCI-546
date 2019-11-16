package inferencenetwork;

public class NotBeliefNode extends BeliefNode {

    public Double score(int docId) {

        // return score of the lone child in log-space
        return Math.log(1.0 - Math.exp(children.get(0).score(docId)));
    }

}
