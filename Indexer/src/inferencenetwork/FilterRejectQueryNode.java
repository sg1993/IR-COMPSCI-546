package inferencenetwork;

public class FilterRejectQueryNode extends FilterQueryNode {

    public FilterRejectQueryNode(ProximityNode proximityNode, QueryNode querynode) {
        super(proximityNode, querynode);
    }

    @Override
    public int nextCandidateDocument() {
        return queryNode.nextCandidateDocument();
    }

    @Override
    public Double score(int docId) {
        if (!filter.canScoreDoc(docId)) {
            // the proximity-node (i.e. the filter) doesn't have this
            // doc in its docSet, so the filter-reject applies!
            // score this doc
            return queryNode.score(docId);
        }

        return null;
    }

}
