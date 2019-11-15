package inferencenetwork;

public abstract class FilterQueryNode extends QueryNode {

    protected ProximityNode filter = null;
    protected QueryNode queryNode = null;

    public FilterQueryNode(ProximityNode proximityNode, QueryNode querynode) {
        this.filter = proximityNode;
        this.queryNode = querynode;
    }

    @Override
    public void skipTo(int docId) {
        filter.skipTo(docId);
        queryNode.skipTo(docId);
    }
}
