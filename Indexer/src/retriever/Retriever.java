package retriever;

import java.util.List;
import java.util.Map;

import index.Index;

public abstract class Retriever {

    // The index based on which the query-retrieval is to be done
    // This index should either have a complete in-memory index or have the ability
    // to construct index from a file on disk.
    protected Index index = null;

    public Retriever(Index i) {
        index = i;
    }

    // The retriveQuery() method takes a String[] of query-terms
    // and uses the Index to retrieve.
    // The method returns a List of top k documents that score well according to
    // some/any scoring method.
    public abstract List<Map.Entry<Integer, Double>> retrieveQuery(String[] query, int k,
            Evaluator evaluator);

    // This method computes Dice's coefficient for 2 terms.
    abstract double computeDiceCoefficient(String a, String b);
}
