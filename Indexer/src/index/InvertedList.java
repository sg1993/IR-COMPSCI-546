package index;

import java.util.ArrayList;
import java.util.HashMap;

public class InvertedList {

    // An inverted list is specific to a word in the vocab
    // It is list of Postings for all the documents in which this term occurs.

    // We probably don't need to store the term itself here, but anyways..
    String term;

    // frequency of the term in this doc
    int termFrequency;

    // Hashmap for quick lookup whether a doc-posting is already present
    // The key is the unique identifier for doc. String because it accommodates most
    // docIds. If passing integers, convert to String first
    HashMap<String, Integer> postingLookup;

    // List of Postings
    ArrayList<Posting> postings;

    public void InvertedList(String s) {
        term = s;
        postings = new ArrayList<Posting>();
    }

    public int isPostingInList(String docId) {
        if (postingLookup.containsKey(docId)) {
            return postingLookup.get(docId);
        }
        return -1;
    }
    
    public void writeSelfToDisk() {
        
    }

}
