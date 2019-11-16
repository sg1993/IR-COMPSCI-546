package inferencenetwork;

import java.util.ArrayList;

public abstract class BeliefNode extends QueryNode {

    // Belief nodes can have any other QueryNode as their child
    protected ArrayList<? extends QueryNode> children;

    public void setChildren(ArrayList<? extends QueryNode> list) throws Exception {

        if (this instanceof NotBeliefNode &&
                children.size() == 1) {
            throw new Exception("A NotBeliefNode can have ony a single child!");
        }

        children = list;
    }

    @Override
    public void skipTo(int docId) {
        // skipTo() for a belief node would be to ask all its children to skipTo()
        for (QueryNode child : children) {
            child.skipTo(docId);
        }

    }

    @Override
    // nextCandidate of a BeliefNode is the min of the
    // nextCandidates of its children.
    public int nextCandidateDocument() {
        int candidate = Integer.MAX_VALUE;

        // find the minimum of all next-document candidates
        // from all children
        for (QueryNode child : children) {
            candidate = Math.min(candidate, child.nextCandidateDocument());
        }

        return candidate;
    }
}
