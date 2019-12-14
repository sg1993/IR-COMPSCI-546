package retriever.evaluation;

public abstract class Evaluator {

    // The evaluator should return true if it needs even those
    // documents to be sent to be scored which don't have the query term
    // by assigning a background probability.
    // If not, false
    public abstract boolean assignsBackgroundProbability();

    public abstract double getDocScoreForQueryTerm(String queryTerm, int termFrequency, int docId);

    public double getDocScoreForQueryWindow(int termFrequency, int docId, int collectionFrequency) {
        // TODO Auto-generated method stub
        return 0;
    }
}
