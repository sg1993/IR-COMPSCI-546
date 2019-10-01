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
    // and uses the Index object.
    // The method returns a List of top k documents that score well according to
    // some or any scoring method.
    abstract List<Map.Entry<Integer, Double>> retrieveQuery(String[] query, int k);
}
