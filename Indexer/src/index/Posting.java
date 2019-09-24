package index;

import java.util.ArrayList;

public class Posting {

    // this is a class that stores freq/pos of a term for a document

    private int docId;
    private int termFrequency;
    private ArrayList<Integer> positions;
    
    public void Posting(int id) {
        docId = id;
        positions = new ArrayList<Integer>();
    }

    public void addPosition(int pos) {
        termFrequency++;
        positions.add(pos);
    }
    // we don't really support removing a position so "add" is pretty much it
    
    public int getTermFrequency() {
        return termFrequency;
    }

    public void deltaEncodeSelf() {

    }

}
