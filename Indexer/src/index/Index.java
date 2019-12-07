package index;

import java.util.ArrayList;

public abstract class Index {

    public abstract int getDocumentFrequencyForTerm(String term);

    public abstract int getNumDocs();

    public abstract int getNumWordsInCollection();

    public abstract ArrayList<String> getVocabListFromIndex();

    public abstract int getCollectionFrequencyForTerm(String term);

    public abstract int getNumWordsInDocument(int docId);

    public abstract ArrayList<String> getBackingDocumentIDs();

    public abstract Double getPriorForDocument(int docId);

}
