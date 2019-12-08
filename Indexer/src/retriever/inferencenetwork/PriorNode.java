package retriever.inferencenetwork;

import index.Index;

public class PriorNode extends QueryNode {

    // A prior node needs access to the Index
    // so that it can ask the prior values for any document
    Index index;

    public PriorNode(Index index) {
        this.index = index;
    }

    @Override
    public int nextCandidateDocument() {
        // a prior-node has no documents that it "wants" to score
        // it just scores whatever document you throw at it!
        return Integer.MAX_VALUE;
    }

    @Override
    public Double score(int docId) {
        return index.getPriorForDocument(docId);
    }

    @Override
    public void skipTo(int docId) {
        // do nothing; a prior node has no documents to skipTo
    }

}
