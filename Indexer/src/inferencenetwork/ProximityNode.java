package inferencenetwork;

import index.InvertedList;
import retriever.Evaluator;

public abstract class ProximityNode extends QueryNode {

    protected Evaluator evaluator;

    // Every Proximity Node deals with an inverted list
    protected InvertedList iList;

    public ProximityNode(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    protected InvertedList getIList() {
        return iList;
    }

    // should only be used for filter-reject/filter-require operations
    // the proximity node (term/window) checks if this doc satisfies
    // the proximity-operator
    protected abstract boolean canScoreDoc(int docId);

}
