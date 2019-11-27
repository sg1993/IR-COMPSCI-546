package cluster;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

public class DocumentVectorFactory {

    private HashMap<Integer, DocumentVector> docVectors;

    public DocumentVectorFactory() {
        docVectors = new HashMap<Integer, DocumentVector>();
    }

    public void addTermToDocumentVector(int docId, String term) {

        if (docVectors.containsKey(docId)) {
            docVectors.get(docId).addTerm(term);
        } else {
            // a new document; create a new doc vector
            DocumentVector newVector = new DocumentVector(docId);
            newVector.addTerm(term);
            docVectors.put(docId, newVector);
        }
    }

    public void addTermToDocumentVector(int docId, String term, int termFrequency) {
        if (docVectors.containsKey(docId)) {
            docVectors.get(docId).addTerm(term, termFrequency);
        } else {
            // a new document; create a new doc vector
            DocumentVector newVector = new DocumentVector(docId);
            newVector.addTerm(term, termFrequency);
            docVectors.put(docId, newVector);
        }
    }

    public JSONObject JSONifySelf() {
        JSONObject resultJsonObject = new JSONObject();
        for (Entry<Integer, DocumentVector> entry : docVectors.entrySet()) {
            resultJsonObject.put(entry.getKey().toString(), entry.getValue().JSONifySelf());
        }
        return resultJsonObject;
    }

    @Override
    public String toString() {
        String result = "";
        for (Entry<Integer, DocumentVector> entry : docVectors.entrySet()) {
            result += "doc: " + entry.getKey() + ", " + entry.getValue();
        }
        return result;
    }

    public Set<Integer> getDocIds() {
        return docVectors.keySet();
    }

    public DocumentVector getDocumentVector(Integer docId) {
        return docVectors.getOrDefault(docId, null);
    }

}
