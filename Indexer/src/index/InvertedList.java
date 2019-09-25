package index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class InvertedList {

    // An inverted list is specific to a word in the vocab
    // It is list of Postings for all the documents in which this term occurs.

    // We probably don't need to store the term itself here, but anyways..
    String term;

    private int numDocs;

    // Hashmap for quick lookup whether a doc-posting is already present
    // The key is the unique identifier for doc. String because it accommodates most
    // docIds. If passing integers, convert to String first.
    // the value is the index in which this docId is added to list
    HashMap<Integer, Posting> postings;

    public InvertedList(String s) {
        term = s;
        postings = new HashMap<Integer, Posting>();
        numDocs = 0;
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
    }

    /*
     * public ArrayList<Bytes> getByteBuffer(boolean compress) { for (Entry<String,
     * Posting> posting : postings.entrySet()) { if (compress) { ArrayList<Integer>
     * toWrite = posting.getByteBuffer(compress); } } }
     */

    public void printSelf() {
        System.out.println(term);
        for (Entry<Integer, Posting> pos : postings.entrySet()) {
            pos.getValue().printSelf();
        }
    }

}
