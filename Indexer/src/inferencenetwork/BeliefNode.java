package inferencenetwork;

import java.util.ArrayList;

public abstract class BeliefNode extends QueryNode {

    // Belief nodes can have any other QueryNode as their child
    protected ArrayList<QueryNode> children;

    public void setChildren(ArrayList<QueryNode> list) {
        children = list;
    }

    @Override
    public void skipTo(int docId) {
        // skipTo() for a belief node would be to ask all its children to skipTo()
        for (QueryNode node : children) {
            node.skipTo(docId);
        }

    }

    @Override
    public int nextCandidateDocument() {
        int candidate = Integer.MAX_VALUE;

        // find the minimum of all next-document candidates
        // from all children
        for (QueryNode node : children) {
            candidate = Math.min(candidate, node.nextCandidateDocument());
        }

        return candidate;
    }
}
