package index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import reader.Document;

public class InvertedFileIndex {

    // list of inverted-lists for every term in the vocab
    private HashMap<String, InvertedList> invListLookup;

    public InvertedFileIndex() {
        invListLookup = new HashMap<String, InvertedList>();
    }

    public void createIndexFromDocumentStore(ArrayList<Document> docs) {
        for (Document doc : docs) {
            String[] termVector = doc.getTermVector();
            int termPosition = 1;
            InvertedList list = null;
            for (String term : termVector) {
                // check if invertedList for this term exists
                if (invListLookup.containsKey(term)) {
                    // there is a list already in our index for this term
                    list = invListLookup.get(term); // index of the list
                } else {
                    list = new InvertedList(term);
                }
                list.addPositionToPosting(doc.getDocumentUniqueId(), termPosition);
                invListLookup.put(term, list);
                termPosition++;
            }
        }
    }

    public void writeSelfToDisk(String filename, boolean compress) {
        for (Entry<String, InvertedList> list : invListLookup.entrySet()) {
            // list.getByteBuffer(compress);
        }
    }

    public void printSelf() {
        for (Entry<String, InvertedList> entry : invListLookup.entrySet()) {
            // System.out.println("List");
            entry.getValue().printSelf();
        }
    }

}
