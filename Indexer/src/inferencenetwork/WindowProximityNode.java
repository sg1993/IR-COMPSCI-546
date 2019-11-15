package inferencenetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import index.InvertedList;
import retriever.Evaluator;

public abstract class WindowProximityNode extends ProximityNode {

    // window-size of this node
    int windowSize = 0;

    // maintain a list of docs that have all the children nodes
    // of this proximity node i.e. intersection of documents in
    // the inverted-lists of children
    protected ArrayList<Integer> docSet = null;

    // Window nodes can only have TermProximityNodes as their children
    protected ArrayList<TermProximityNode> children;

    // holds the inverted lists of children
    protected ArrayList<InvertedList> childILists = null;

    // holds count of window occurrences for all docs
    HashMap<Integer, Integer> windowCounts = new HashMap<Integer, Integer>();

    // collection frequency of this window i.e. across all documents
    int collectionFrequency = 0;

    // docIndex : index of the next candidate doc to be scored
    protected int docIndex = Integer.MAX_VALUE;

    public WindowProximityNode(Evaluator evaluator, int windowSize) {
        super(evaluator);
        this.windowSize = windowSize;
        // TODO Auto-generated constructor stub
    }

    protected ArrayList<Integer> intersectDocs(ArrayList<InvertedList> iLists) {
        ArrayList<Integer> results = new ArrayList<Integer>();
        HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
        int numChildren = children.size();
        for (InvertedList iList : iLists) {
            for (Integer docId : iList.getPostings().keySet()) {
                counts.put(docId, counts.getOrDefault(docId, 0) + 1);
            }
        }

        // the docs which have all the terms will have their count = numChildren
        // i.e. they appear atleast once in all childrens' inverted-list
        for (Integer docId : counts.keySet()) {
            if (counts.get(docId) == numChildren) {
                results.add(docId);
            }
        }

        // sort in ascending order of docId and then return
        Collections.sort(results);
        return results;
    }

    @Override
    public int nextCandidateDocument() {
        // return the next doc from docSet since docSet
        // already has the pre-computed list of documents
        // that contain atleast one occurrence of the window
        if (docIndex < docSet.size()) {
            return docSet.get(docIndex);
        }

        // else, we have no more candidates.
        // return MAX_VALUE. the caller will know what to do
        return Integer.MAX_VALUE;
    }

    @Override
    public void skipTo(int docId) {
        // skip till the docIndex is on a doc with id >= docId
        while (docIndex < docSet.size() && docSet.get(docIndex) < docId) {
            docIndex++;
        }
    }

    @Override
    public Double score(int docId) {
        if (docSet.contains(docId)) {
            return evaluator.getDocScoreForQueryWindow(windowCounts.get(docId), docId,
                    collectionFrequency);
        } else {
            if (evaluator.assignsBackgroundProbability()) {
                return evaluator.getDocScoreForQueryWindow(0, docId, collectionFrequency);
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }
    }

    @Override
    protected boolean canScoreDoc(int docId) {

        if (docSet.contains(docId)) {
            return true;
        }

        return false;
    }

}
