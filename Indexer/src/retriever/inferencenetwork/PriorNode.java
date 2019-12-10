package retriever.inferencenetwork;

import index.Index;

public class PriorNode extends QueryNode {

    // A prior node needs access to the Index
    // so that it can ask the prior values for any document
    Index index;
    
    // the file which has the prior values in it
    String priorLookupFile = null;

    public PriorNode(Index index, String file) {
        this.index = index;
        this.priorLookupFile = file;
    }

    @Override
    public int nextCandidateDocument() {
        // a prior-node has no documents that it "wants" to score
        // it just scores whatever document you throw at it!
        return Integer.MAX_VALUE;
    }

    @Override
    public Double score(int docId) {
        // ask the index to lookup the prior for this doc from the lookup-file
        return index.getPriorForDocument(docId, priorLookupFile);
    }

    @Override
    public void skipTo(int docId) {
        // do nothing; a prior node has no documents to skipTo
    }

}
