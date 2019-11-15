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
        System.out.println("checking filter-reject on " + docId);
        if (!filter.canScoreDoc(docId)) {
            // the proximity-node (i.e. the filter) doesn't have this
            // doc in its docSet, so the filter-reject applies!
            // score this doc
            System.out.println("no can do amigo");
            return queryNode.score(docId);
        }

        return null;
    }

}
