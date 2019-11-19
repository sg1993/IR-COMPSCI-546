package retriever.inferencenetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import index.InvertedList;
import index.Posting;
import retriever.evaluation.Evaluator;

public abstract class WindowProximityNode extends ProximityNode {

    // window-size of this node
    int windowSize = 0;

    // maintain a list of docs that have all the children nodes
    // of this proximity node i.e. intersection of documents in
    // the inverted-lists of children
    protected ArrayList<Integer> docSet = null;

    // Window nodes can have any other ProximityNodes as their children
    protected ArrayList<? extends ProximityNode> children;

    // holds the inverted lists of children
    protected ArrayList<InvertedList> childILists = null;

    // holds count of window occurrences for all docs
    HashMap<Integer, Integer> windowCounts = new HashMap<Integer, Integer>();

    // collection frequency of this window i.e. across all documents
    int collectionFrequency = 0;

    public WindowProximityNode(Evaluator evaluator, int windowSize) {
        super(evaluator);
        this.windowSize = windowSize;
        // TODO Auto-generated constructor stub
    }

    public void setChildren(ArrayList<? extends ProximityNode> list) {
        children = list;

        // create the inverted list for window-nodes
        // they are same as traditional inverted lists except that the positions
        // are really just start-positions of windows
        createFakeInvertedList();
    }

    public abstract void createFakeInvertedList();

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
    public Double score(int docId) {

        if (iList == null) {
            // if no iList exists, the probability-score is 0
            // so the log-space score is log(0) i.e. -ve infinity.
            return Double.NEGATIVE_INFINITY;
        }

        // since we would have been asked to skipTo(docId) previously,
        // if the currentPosting is not docId, we don't have docId in our list
        Posting curPosting = iList.getCurrentPosting();
        if (curPosting != null && curPosting.getDocId() == docId) {
            /*
             * System.out.println(
             * docId + ": " + windowCounts.get(docId) + ", total: " + collectionFrequency);
             */
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

}
