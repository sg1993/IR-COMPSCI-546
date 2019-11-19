package retriever.inferencenetwork;

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

        if (iList == null) {
            // if no iList exists, the probability-score is 0
            // so the log-space score is log(0) i.e. -ve infinity.
            return Double.NEGATIVE_INFINITY;
        }

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

}
