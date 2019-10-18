package retriever;

import java.util.ArrayList;

import index.Index;

/*
 * This is a query-likelihood model which uses
 * the Dirichlet Smoothing to assign
 * foreground and background probabilities to query-terms.
 * Query-likelihood models treat each term in the query independently. 
 * If a term occurs more than once in a query, 
 * it contributes more than once to the score.
 */
public class DirichletEvaluator extends Evaluator {

    Index index = null;

    // pre-computed documents lengths for all docs in the collection
    private ArrayList<Integer> docLengths = null;

    private double mu = 0.3;

    public DirichletEvaluator(Index i, ArrayList<Integer> lengths) {
        index = i;
        docLengths = lengths;
    }

    @Override
    public double getDocScoreForQueryTerm(String queryTerm, int termFrequency, int docId) {
        double denominator = docLengths.get(docId) + mu;
        double foregroundProbability = (double) termFrequency / (double) denominator;
        double backgroundProbability = mu * (index.getCollectionFrequencyForTerm(queryTerm))
                / (double) ((index.getNumWordsInCollection()) * denominator);

        return Math.log((foregroundProbability + backgroundProbability));
    }
}
