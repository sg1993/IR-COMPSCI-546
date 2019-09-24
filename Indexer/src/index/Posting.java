package index;

import java.util.ArrayList;

public class Posting {

    // this is a class that stores freq/pos of a term for a document

    private String docId;
    int termFrequency; // frequency of the term in this doc
    private ArrayList<Integer> positions;

    public void Posting(String id) {
        docId = id;
        termFrequency = 0;
        positions = new ArrayList<Integer>();
    }

    public void addPosition(int pos) {
        termFrequency++;
        positions.add(pos);
    }
    // we don't really support removing a position so "add" is pretty much it

    public String getDocId() {
        return docId;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    public void deltaEncodeSelf() {

    }

}
