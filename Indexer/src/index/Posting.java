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

    private ArrayList<Integer> deltaEncodeSelf() {
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

    public ArrayList<Integer> getPosting(boolean compress) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        // first add the doc id
        result.add(docId);

        // then add the term frequency in this document
        result.add(termFrequency);

        if (compress) {
            result.addAll(deltaEncodeSelf());
        } else {
            result.addAll(positions);
        }

        return result;
    }

    public void printSelf() {
        System.out.println("Posting " + docId + ": [" + termFrequency + "] = " + positions);
    }

    public static boolean compareTwoPostings(Posting p1, Posting p2) {
        ArrayList<Integer> pos1 = p1.getPositions(), pos2 = p2.getPositions();

        if (p1.getDocId() != p2.getDocId())
            return false;

        if (pos1.size() != pos2.size())
            return false;

        for (int i = 0; i < pos1.size(); i++) {
            if (!pos1.get(i).equals(pos2.get(i)))
                return false;

        }

        return true;
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }
}
