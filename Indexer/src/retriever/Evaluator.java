package retriever;

public abstract class Evaluator {

    public abstract double getDocScoreForQueryTerm(String queryTerm, int termFrequency, int docId);
}
