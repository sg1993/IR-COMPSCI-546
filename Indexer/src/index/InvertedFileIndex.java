package index;

import java.util.ArrayList;
import java.util.HashMap;

import reader.Document;

public class InvertedFileIndex {

    // list of inverted-lists for every term in the vocab
    private ArrayList<InvertedList> invertedLists;

    // fast lookup table for term's invertedList
    private HashMap<String, Integer> invListLookup;

    public InvertedFileIndex() {
        invertedLists = new ArrayList<InvertedList>();
        invListLookup = new HashMap<String, Integer>();
    }

    public void createIndexFromDocumentStore(ArrayList<Document> docs) {
        for (Document doc : docs) {
            String[] termVector = doc.getTermVector();
            int termPosition = 1;
            for (String term : termVector) {
                // check if invertedList for this term exists
                if (invListLookup.containsKey(term)) {
                    // there is a list already in our index for this term
                    int listIndex = invListLookup.get(term); // index of the list
                    InvertedList list = invertedLists.get(listIndex);
                    int docIndex = list.isPostingInList(doc.getDocumentUniqueId());
                    if(docIndex != -1) {
                        // there is a document-posting already for this term
                        // ask the inverted list to add this position to the the posting of this term
                        list.addPositionToPosting(docIndex, termPosition);
                    }
                }
            }
        }
    }

}
