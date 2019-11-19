package retriever.inferencenetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import index.InvertedList;
import index.Posting;
import retriever.Evaluator;

public class OrderedWindowProximityNode extends WindowProximityNode {

    public OrderedWindowProximityNode(Evaluator evaluator, int w) {
        super(evaluator, w);
    }

    public void createFakeInvertedList() {

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
        findOrderedWindows(docSet, childILists);

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

    private ArrayList<ArrayList<Integer>> findCandidatePositions(
            ArrayList<ArrayList<Integer>> positionsA,
            ArrayList<Integer> positionsB, int window) {

        // System.out.println("received: " + positionsA);
        // System.out.println("received: " + positionsB);
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

        int len1 = positionsA.size(), len2 = positionsB.size();

        // O(n^2): go through all possible pairs
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                // A has to come before B
                ArrayList<Integer> A = new ArrayList<Integer>(positionsA.get(i));
                // get the last position in A as that is what compare against position of B
                int posA = A.get(A.size() - 1);
                int posB = positionsB.get(j);
                if (posA >= posB)
                    continue;
                // there must 'window-1' or less terms between posB and posA
                // i.e. posB-posA-1 <= window-1
                // or posB-posA <= window
                if (posB - posA <= window) {
                    A.add(posB);
                    result.add(A);
                } else {
                    // posB has already exceeded the window limit.
                    // move to next posA
                    break;
                }
            }
        }
        // System.out.println("result: " + result);
        return result;
    }

    private void findOrderedWindows(ArrayList<Integer> docs,
            ArrayList<InvertedList> childILists) {

        /*
         * we will borrow the 2-pointer algorithm used to calculate Dice's coefficient
         * score and extend it to work on multiple terms.
         * The algo looks something like this:
         * 1) Start with any 2 terms and find all the possible candidate-positions (i.e.
         * within the window size). We only care about the position of end of window.
         * 2) Use the candidate-positions in the first array as the next term and pick
         * another term from the window-query.
         * 3) Repeat until no more terms are left.
         * 4) Now we have all possible windows but there is double-dipping since we
         * looked at ALL possible pairs in (1).
         * 5) The final step is to prune those windows which have already "seen"
         * positions and thus avoiding double-dipping across windows.
         */

        // loop through all candidate docs
        for (Integer docId : docs) {

            windowCounts.put(docId, 0);

            int windowCount = 0;

            // get postings for this doc from all children's inverted-lists
            ArrayList<Posting> postings = new ArrayList<Posting>(
                    childILists.stream()
                            .map(list -> list.getPostings().get(docId))
                            .collect(Collectors.toList()));

            // start with 2 terms
            ArrayList<ArrayList<Integer>> result = null;
            for (int i = 0; i < postings.size(); i++) {
                if (i == 0) {
                    // since we are just starting, create an arraylist of arraylist where
                    // each position of the first term is an arraylist in itself
                    result = new ArrayList<ArrayList<Integer>>(
                            postings.get(0).getPositions().stream()
                                    .map(position -> (new ArrayList<Integer>(
                                            Arrays.asList(position))))
                                    .collect(Collectors.toList()));
                    // System.out.println("result: " + result);
                } else {
                    // process list1 and the current posting's positions
                    // and update list1 to the returned result
                    result = findCandidatePositions(result, postings.get(i).getPositions(),
                            windowSize);
                    if (result.size() == 0) {
                        // if nothing was returned, we are done
                        // no windows exist!
                        break;
                    }
                }
            }
            // results has valid windows but with double-dipping
            // choose only those windows from result which don't double dip
            HashSet<Integer> seen = new HashSet<Integer>();
            for (ArrayList<Integer> window : result) {
                boolean validWindow = true;
                for (int position : window) {
                    if (seen.contains(position)) {
                        // if even one position in this window was seen already,
                        // we can't use this window
                        // move to the next window
                        validWindow = false;
                        break;
                    }
                }
                if (validWindow) {
                    // yay! we have a valid window
                    windowCount += 1;
                    // add all positions in window to the "seen" list
                    seen.addAll(window);
                    // System.out.println("valid window: " + window);
                    // add only the start-position of this window to the fake inverted list we're
                    // creating for this ProximityNode
                    if (iList == null) {
                        iList = new InvertedList("<WINDOW_HOLDER_TERM>");
                    }
                    iList.addPositionToPosting(docId, window.get(0));
                }
            }
            windowCounts.put(docId, windowCount);
            collectionFrequency += windowCount;
        }
    }

}
