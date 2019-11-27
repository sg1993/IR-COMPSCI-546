package cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

public class DocumentVector {

    private int docId;

    private HashMap<String, Integer> docVector;

    public DocumentVector(int d) {
        docId = d;
        docVector = new HashMap<String, Integer>();
    }

    public int getDocId() {
        return docId;
    }

    // add a count to an existing term in the vector
    public void addTerm(String term) {
        docVector.put(term, docVector.getOrDefault(term, 0) + 1);
    }

    // add a term with its count in the vector
    public void addTerm(String term, int termFrequency) {
        docVector.put(term, termFrequency);
    }

    public JSONObject JSONifySelf() {
        JSONObject jsonObject = new JSONObject();
        for (Entry<String, Integer> entry : docVector.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return docVector.toString();
    }

    public Set<String> getTerms() {
        return docVector.keySet();
    }

    public int getTermCount(String term) {
        return docVector.getOrDefault(term, 0);
    }

}
