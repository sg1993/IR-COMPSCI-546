package retriever;

import java.util.ArrayList;
import java.util.HashMap;

import index.Index;

public class BM25Evaluator extends Evaluator {

    Index index = null;

    private int N = 0; // total number of documents in the index/collection
    private double avdl = 0; // average document length in collection

    private double k1 = 1.2, b = 0.75, k2 = 100.0;

    HashMap<String, Integer> termFrequencyInQuery = null;

    // pre-computed documents lengths for all docs in the collection
    private ArrayList<Integer> docLengths = null;

    // map of "bm25#"<query-term>#"<docid> : just a weird sequence that has less
    // chance of clashing
    private HashMap<String, Boolean> seenQueryTerm = null;

    public BM25Evaluator(Index i, String[] query, ArrayList<Integer> lengths) {
        index = i;
        N = index.getNumDocs();
        avdl = (double) index.getNumWordsInCollection() / (double) N;

        // pre-compute frequency of terms in the query
        termFrequencyInQuery = new HashMap<String, Integer>();
        for (String s : query) {
            int count = 1;
            if (termFrequencyInQuery.containsKey(s)) {
                count = termFrequencyInQuery.get(s) + 1;
            }
            termFrequencyInQuery.put(s, count);
        }
        docLengths = lengths;
        seenQueryTerm = new HashMap<String, Boolean>();
    }

    @Override
    public double getDocScoreForQueryTerm(String queryTerm, int termFrequency, int docId) {

        // check if the term was already evaluated for this doc
        if (seenQueryTerm.containsKey("bm25#" + queryTerm + "#" + docId)) {
            // we have already seen this term for this docId
            // return a score of 0
            return 0.0;
        }

        // mark the term as "seen" for this document
        seenQueryTerm.put("bm25#" + queryTerm + "#" + docId, true);

        // document-frequency of this query-term
        // (how many documents does it appear atleast once)
        int n = index.getDocumentFrequencyForTerm(queryTerm);

        double logNumerator = (double) N - (double) n + 0.5;
        double logDenominator = (double) n + 0.5;

        int dl = docLengths.get(docId);
        double K = k1 * ((1 - b) + (b * dl / avdl));
        double tfComponent = (k1 + 1) * termFrequency / (K + termFrequency);
        int queryTermFrequency = termFrequencyInQuery.containsKey(queryTerm)
                ? termFrequencyInQuery.get(queryTerm)
                : 0;
        double queryTermComponent = (k2 + 1) * queryTermFrequency / (k2 + queryTermFrequency);

        double score = Math.log(logNumerator / logDenominator) * tfComponent * queryTermComponent;
        return score;
    }

    @Override
    public boolean assignsBackgroundProbability() {
        // The BM-25 evaluator doesn't assign background probability scores
        // to documents which don't have the query term.
        return false;
    }

}
