package reader;

import java.util.ArrayList;

public abstract class Reader {

    String filename = null;
    private ArrayList<Document> documentList = null;

    public Reader(String fname) {
        this.filename = fname;
        documentList = new ArrayList<Document>();
    }

    public void putIntoDocumentList(Document doc) {
        documentList.add(doc);
    }

    public ArrayList<Document> getDocuments() {
        return documentList;
    }

    public int getDocumentListSize() {
        return documentList.size();
    }

    // Each type of Document-readers should implement their own read()
    abstract void read();
}
