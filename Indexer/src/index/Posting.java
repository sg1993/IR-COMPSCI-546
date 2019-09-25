package index;

import java.util.ArrayList;

public class Posting {

    // this is a class that stores freq & pos of a term for a document

    private int docId; // integer id of the Document super-class
    int termFrequency; // frequency of the term in this doc
    private ArrayList<Integer> positions;

    public Posting(int id) {
        docId = id;
        termFrequency = 0;
        positions = new ArrayList<Integer>();
    }

    public void addPosition(int pos) {
        termFrequency++;
        positions.add(pos);
    }
    // we don't really support removing a position so "add" is pretty much it

    public int getDocId() {
        return docId;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    public ArrayList<Integer> deltaEncodeSelf() {
        if (termFrequency > 0) {
            ArrayList<Integer> encoding = new ArrayList<Integer>();
            encoding.add(positions.get(0));
            for (int i = 1; i < termFrequency; i++) {
                encoding.add(positions.get(i) - positions.get(i - 1));
            }
            return encoding;
        }
        return null;
    }

    /*
     * public ArrayList<Byte> getByteBuffer(boolean compress) { ArrayList<Byte>
     * byteArray = new ArrayList<Byte>(); byteArray.add(e) for() {
     * 
     * } return byteArray; }
     */

    public void printSelf() {
        System.out.println("Posting " + docId + ": " + positions);
    }
}
