package retriever;

public class RawCountEvaluator extends Evaluator {

    @Override
    public double getDocScoreForQueryTerm(String queryTerm, int termFrequency, int docId) {
        // raw-count evaluation just returns the raw-term frequency
        // of the term in the document, as the "score" of the document
        return termFrequency;
    }

}
