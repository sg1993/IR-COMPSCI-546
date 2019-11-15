package index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class InvertedList {

    // An inverted list is specific to a word in the vocab
    // It is list of Postings for all the documents in which this term occurs.

    // We probably don't need to store the term itself here, but anyways..
    String term;

    // how many docs soes this term appear in?
    private int numDocs;

    // count of occurrence across all docs
    private int collectionFrequency;

    // Hashmap for quick lookup whether a doc-posting is already present
    // The key is the unique identifier for doc. String because it accommodates most
    // docIds. If passing integers, convert to String first.
    // the value is the index in which this docId is added to list
    LinkedHashMap<Integer, Posting> postings;

    // pointer to move around and skipping ahead
    private int postingsIndex;

    private ArrayList<Integer> docSet = null;

    public InvertedList(String s) {
        term = s;
        postings = new LinkedHashMap<Integer, Posting>();
        numDocs = 0;
        collectionFrequency = 0;
        startIteration();
    }

    public String getTerm() {
        return term;
    }

    public void addPositionToPosting(int docId, int position) {
        Posting posting = null;
        if (postings.containsKey(docId)) {
            posting = postings.get(docId);
        } else {
            posting = new Posting(docId);
            numDocs++;
        }
        posting.addPosition(position);
        postings.put(docId, posting);
        collectionFrequency++;
    }

    public ArrayList<Integer> getList(boolean compress) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        int previousDocId = 0;
        for (Entry<Integer, Posting> posting : postings.entrySet()) {
            // if compression is enabled, we'll get the delta-encoded list from the
            // "Posting" class. Note that only the positions will be delta-encoded at this
            // stage, since Posting doesn'know about other docIds..
            ArrayList<Integer> posList = posting.getValue().getPosting(compress);

            if (compress) {
                // delta-encode docId which is the first element in the flattened posting array
                // returned by Posting class
                int deltaDocId = posList.get(0) - previousDocId;
                previousDocId = posList.get(0);
                posList.set(0, deltaDocId);
            }

            result.addAll(posList);
        }
        return result;
    }

    public int getDocumentFrequency() {
        return numDocs;
    }

    public int getCollectionFrequency() {
        return collectionFrequency;
    }

    public HashMap<Integer, Posting> getPostings() {
        return postings;
    }

    /**
     * reset the list pointer to the first element
     */
    public void startIteration() {
        postingsIndex = 0;
    }

    /**
     * are there any more?
     * 
     * @return true if there are remaining elements in the list
     */
    public boolean hasMore() {
        return (postingsIndex >= 0 && postingsIndex < postings.size());
    }

    /**
     * skip to or past the specified document id
     * 
     * @param docid the id to skip to
     * 
     */
    public void skipTo(int docid) {
        while (postingsIndex < postings.size() && getCurrentPosting().getDocId() < docid) {
            postingsIndex++;
        }
    }

    /**
     * @return the current posting in the list or null if the list is empty or
     *         consumed
     */
    public Posting getCurrentPosting() {
        Posting retval = null;
        try {
            if (docSet == null) {
                docSet = new ArrayList<Integer>();
                for (Integer docId : postings.keySet()) {
                    docSet.add(docId);
                }
            }

            retval = postings.get(docSet.get(postingsIndex));
        } catch (IndexOutOfBoundsException ex) {
            // ex.printStackTrace();
        }

        return retval;
    }

    public static boolean compareTwoInvertedLists(InvertedList l1, InvertedList l2) {
        for (Entry<Integer, Posting> entry : l1.getPostings().entrySet()) {
            Integer docId = entry.getKey();
            if (!Posting.compareTwoPostings(entry.getValue(), l2.getPostings().get(docId))) {
                return false;
            }
        }
        return true;
    }

    public void printSelf() {
        System.out.println(term);
        for (Entry<Integer, Posting> pos : postings.entrySet()) {
            pos.getValue().printSelf();
        }
    }
}
