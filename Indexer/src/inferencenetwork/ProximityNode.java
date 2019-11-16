package inferencenetwork;

import index.InvertedList;
import index.Posting;
import retriever.Evaluator;

public abstract class ProximityNode extends QueryNode {

    protected Evaluator evaluator;

    // Every Proximity Node deals with an inverted list
    protected InvertedList iList = null;

    public ProximityNode(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    protected InvertedList getIList() {
        return iList;
    }

    @Override
    public int nextCandidateDocument() {

        if (iList == null) {
            return Integer.MAX_VALUE;
        }

        // nextCandidate for term node is the next doc in the list
        Posting curPosting = iList.getCurrentPosting();
        if (curPosting != null) {
            return curPosting.getDocId();
        }

        // return max-value if we don't have any more documents to go through
        return Integer.MAX_VALUE;
    }

    @Override
    public void skipTo(int docId) {

        if (iList != null) {
            // just ask the backing inverted-list to skip-to the docId
            iList.skipTo(docId);
        }
    }

    // should only be used for filter-reject/filter-require operations
    // the proximity node (term/window) checks if this doc satisfies
    // the proximity-operator
    protected abstract boolean canScoreDoc(int docId);

}
