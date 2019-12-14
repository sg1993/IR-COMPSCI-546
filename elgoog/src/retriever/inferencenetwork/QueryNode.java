package retriever.inferencenetwork;

public abstract class QueryNode {

    public abstract int nextCandidateDocument();

    // score a document.
    // "Double" and not "double" because we want
    // this function to return "null" if a filter (if present)
    // doesn't apply to this document.
    public abstract Double score(int docId);

    public abstract void skipTo(int docId);

}
