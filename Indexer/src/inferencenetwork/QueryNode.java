package inferencenetwork;

public abstract class QueryNode {

    public abstract int nextCandidateDocument();

    public abstract double score(int docId);

    public abstract void skipTo(int docId);

}
