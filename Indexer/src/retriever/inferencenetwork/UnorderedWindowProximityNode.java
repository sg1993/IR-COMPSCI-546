package retriever.inferencenetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import index.InvertedList;
import index.Posting;
import retriever.evaluation.Evaluator;

public class UnorderedWindowProximityNode extends WindowProximityNode {

    public UnorderedWindowProximityNode(Evaluator evaluator, int w) {
        super(evaluator, w);
    }

    public void createFakeInvertedList() {

        // create a list of inverted lists of the children nodes (which are
        // TermProxNodes). Use streams coz ... why not?
        childILists = new ArrayList<InvertedList>(
                children.stream()
                        .map(child -> child.getIList())
                        .collect(Collectors.toList()));
        assert (children.size() == childILists.size());

        // first intersect the docs to get a candidate list of
        // documents to comb through searching for windows
        docSet = intersectDocs(childILists);
        // System.out.println("the terms appear together in these documents: " +
        // docSet);

        // now that we have the the set of documents to start with,
        // search for the actual windows in these documents
        findUnorderedWindows(docSet, childILists);

        // at this point, we have the windows counted in each doc already
        // prune those docs from docSet which have zero window-count
        for (Integer docId : windowCounts.keySet()) {
            if (windowCounts.get(docId) == 0) {
                // docId is an Integer not an int so the remove() takes
                // docId as the object to be removed not as the index
                docSet.remove(docId);
            }
        }

        // woah, we have the candidate docs and their window-counts all ready!
        // time to score some docs!
    }

    private int nextUnseenPostion(ArrayList<Integer> pos, HashSet<Integer> seen) {
        for (int i = 0; i < pos.size(); i++) {
            if (!seen.contains(pos.get(i))) {
                return pos.get(i);
            }
        }
        return Integer.MAX_VALUE;
    }

    @SuppressWarnings("serial")
    private void findUnorderedWindows(ArrayList<Integer> docs,
            ArrayList<InvertedList> childLists) {
        /*
         * The algorithm to find unordered windows goes like this:
         * 1) List the current positions
         * of all terms. The least positioned one is the first one.
         * 2) Find the largest window of all the possible pairs with the first one.
         * The largest window should by definition contain all the other terms as well
         * in this window.
         * a) if this window < windowSize, great, its a match.
         * Move all terms to their next position (No double dipping of terms between
         * windows).
         * b) if not a match, the terms remain unconsumed so move only the first term's
         * (the one with the least position) pointer to the next position.
         * Go to 1.
         */

        // if window-size is zero, this node acts as a Boolean-And Node
        // where the the terms can be present in any order in a window
        // of size = document-length.
        // Here, we just assume it to be INT_MAX since its the same damn thing!
        if (windowSize == 0)
            windowSize = Integer.MAX_VALUE;

        // loop through all candidate docs
        for (Integer docId : docs) {

            // initialize count of windows in this doc to zero
            windowCounts.put(docId, 0);

            // get postings for this doc from all children's inverted-lists
            ArrayList<Posting> postings = new ArrayList<Posting>(
                    childLists.stream()
                            .map(list -> list.getPostings().get(docId))
                            .collect(Collectors.toList()));

            // this loop will exit when even one of
            // the child has no more positions left
            int windowCount = 0;
            boolean positionsLeft = true;
            HashSet<Integer> globalPositionSeen = new HashSet<Integer>();
            HashSet<Integer> localPositionSeen = new HashSet<Integer>();
            while (true) {

                localPositionSeen.clear();

                // get the next "unused" positions from all the postings
                int startPos = Integer.MAX_VALUE;
                int endPos = Integer.MIN_VALUE;
                for (int i = 0; i < postings.size(); i++) {
                    // get next unused position from this position-array
                    // the returned position should never have been used in any previous window's
                    // computation neither in the current window's computation
                    int pos = nextUnseenPostion(postings.get(i).getPositions(),
                            new HashSet<Integer>() {
                                {
                                    addAll(globalPositionSeen);
                                    addAll(localPositionSeen);
                                }
                            });
                    if (pos == Integer.MAX_VALUE) {
                        // no more unused positions for this child
                        // no need to continue searching for windows
                        positionsLeft = false;
                        break;
                    }
                    // System.out.println("using " + pos + " for term: " + i);

                    // add this pos to current window's consumed positions
                    localPositionSeen.add(pos);

                    // is this the first-positioned term as of now?
                    if (pos < startPos) {
                        startPos = pos;
                    }

                    // is this the last-positioned term as of now?
                    if (pos > endPos) {
                        endPos = pos;
                    }
                }

                // if even one of the terms had no more unused positions, break
                if (!positionsLeft)
                    break;

                // we have a range [startPos, endPos] which contains all the terms
                // check if this range fits the window-size criteria
                // there must be at most 'windowSize' terms in the window
                // including startPos and endPos.
                // i.e. endPos - startPos + 1 <= windowSize
                if (endPos - startPos + 1 <= windowSize) {
                    // yay! a window
                    windowCount += 1;
                    /*
                     * System.out.println(
                     * "Found a window in " + docId + " from " + startPos + " to " + endPos
                     * + " using: " + localPositionSeen);
                     */

                    // add all position's used in this window's computation to the
                    // global-position set so that future windows don't use them
                    globalPositionSeen.addAll(localPositionSeen);

                    // add only the start-position of this window to the fake inverted list we're
                    // creating for this ProximityNode
                    if (iList == null) {
                        iList = new InvertedList("<WINDOW_HOLDER_TERM>");
                    }
                    iList.addPositionToPosting(docId, startPos);
                } else {

                    /*
                     * System.out.println(
                     * "Found a window in " + docId + " from " + startPos + " to " + endPos
                     * + " but greater than the required windowSize");
                     */
                    // we want only the starting term's position to move forward
                    // the others should remain same as they are unused
                    // To achieve this, add only startPos to the global-position set
                    // the others, by virtue of not being added to the global-position set,
                    // will be picked up for future window computations
                    globalPositionSeen.add(startPos);
                }
            }
            windowCounts.put(docId, windowCount);
            collectionFrequency += windowCount;
        }
    }

}
