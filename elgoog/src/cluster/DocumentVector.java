package cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

public class DocumentVector {

    private int docId;

    private HashMap<String, Double> docVector;

    public DocumentVector(int d) {
        docId = d;
        docVector = new HashMap<String, Double>();
    }

    public int getDocId() {
        return docId;
    }

    // add a count to an existing term in the vector
    public void addTerm(String term) {
        docVector.put(term, docVector.getOrDefault(term, 0.0) + 1.0);
    }

    // add a term with its count in the vector
    public void addTerm(String term, double termFrequency) {
        docVector.put(term, termFrequency);
    }

    public JSONObject JSONifySelf() {
        JSONObject jsonObject = new JSONObject();
        for (Entry<String, Double> entry : docVector.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue().intValue());
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

    public Double getTermCount(String term) {
        return docVector.getOrDefault(term, 0.0);
    }

}
