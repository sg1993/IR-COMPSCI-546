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

}
