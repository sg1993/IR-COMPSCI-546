package retriever.inferencenetwork;

public class FilterRequireQueryNode extends FilterQueryNode {

    public FilterRequireQueryNode(ProximityNode proximityNode, QueryNode querynode) {
        super(proximityNode, querynode);
    }

    @Override
    public int nextCandidateDocument() {
        return Math.max(filter.nextCandidateDocument(), queryNode.nextCandidateDocument());
    }

    @Override
    public Double score(int docId) {
        // ask the proximity-node (i.e. the filter) if this docId
        // is in its document-set and, if yes, score it using the queryNode
        if (filter.canScoreDoc(docId)) {
            return queryNode.score(docId);
        }

        // if this docId doesn't pass the filter-check, return null
        return null;
    }

}
