package inferencenetwork;

import java.util.HashMap;

import index.InvertedList;
import index.Posting;
import retriever.Evaluator;

public class TermProximityNode extends ProximityNode {

    public TermProximityNode(Evaluator evaluator, InvertedList iList) {
        super(evaluator);
        // for a term proximity, there's only one InvertedList
        // that needs to be dealt with which is the term's
        this.iList = iList;
    }

    @Override
    // return score in log-space and not probability-space
    public Double score(int docId) {

        HashMap<Integer, Posting> postings = iList.getPostings();
        if (postings.containsKey(docId)) {
            return evaluator.getDocScoreForQueryTerm(iList.getTerm(),
                    postings.get(docId).getTermFrequency(), docId);
        } else {
            if (evaluator.assignsBackgroundProbability()) {
                return evaluator.getDocScoreForQueryTerm(iList.getTerm(), 0, docId);
            } else {
                // if evaluator doesn't assign background probability,
                // then score should be log(0) i.e. negative-infinity
                return Double.NEGATIVE_INFINITY;
            }
        }
    }

    @Override
    // a term-proximity node can only score a document that
    // is present in its inverted list
    protected boolean canScoreDoc(int docId) {

        Posting curPosting = iList.getCurrentPosting();

        if (curPosting != null) {
            return (curPosting.getDocId() == docId);
        }

        return false;
    }

}
