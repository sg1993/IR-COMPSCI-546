package index;

import java.util.ArrayList;
import java.util.HashMap;
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
    HashMap<Integer, Posting> postings;

    public InvertedList(String s) {
        term = s;
        postings = new HashMap<Integer, Posting>();
        numDocs = 0;
        collectionFrequency = 0;
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
        for (Entry<Integer, Posting> posting : postings.entrySet()) {
            result.addAll(posting.getValue().getPosting(compress));
        }
        return result;
    }

    public int getDocumentFrequency() {
        return numDocs;
    }

    public int getCollectionFrequency() {
        return collectionFrequency;
    }

    private HashMap<Integer, Posting> getPostings() {
        return postings;
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
