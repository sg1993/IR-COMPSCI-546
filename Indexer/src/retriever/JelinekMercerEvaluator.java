package retriever;

import java.util.ArrayList;

import index.Index;

/*
 * This is a query-likelihood model which uses
 * the Jelinek-Mercer Smoothing to assign
 * foreground and background probabilities to query-terms.
 * Query-likelihood models treat each term in the query independently. 
 * If a term occurs more than once in a query, 
 * it contributes more than once to the score.
 */
public class JelinekMercerEvaluator extends Evaluator {

    Index index = null;

    // pre-computed documents lengths for all docs in the collection
    private ArrayList<Integer> docLengths = null;

    private double lambda = 0.3;

    public JelinekMercerEvaluator(Index i, ArrayList<Integer> lengths) {
        index = i;
        docLengths = lengths;
    }

    @Override
    public double getDocScoreForQueryTerm(String queryTerm, int termFrequency, int docId) {
        double foregroundProbability = (1 - lambda) * (termFrequency)
                / (double) (docLengths.get(docId));
        double backgroundProbability = lambda * (index.getCollectionFrequencyForTerm(queryTerm))
                / (index.getNumWordsInCollection());

        return Math.log(foregroundProbability + backgroundProbability);
    }

}
