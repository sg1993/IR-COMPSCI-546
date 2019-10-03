package reader;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONObject;

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

    public void writeDocumentStatistics(PrintWriter pw) {
        // compute various statistics in JSON format into the file
        JSONObject object = new JSONObject();

        // number of documents
        object.put("num_docs", documentList.size());

        JSONObject docIdMap = new JSONObject();

        for (Document document : documentList) {
            docIdMap.put(Integer.toString(document.getDocumentUniqueId()), document.getBackingId());
        }
        object.put("docid_backingid_map", docIdMap);
        pw.write(object.toString(4));
    }

    // Each type of Document-readers should implement their own read()
    abstract void read();
}
