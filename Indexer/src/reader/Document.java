package reader;

public class Document {

    private String[] termVector;
    
    private String uniqueId;

    public Document(String uId, String text) {
        uniqueId = uId;
        termVector = text.split("\\s+");
    }
    
    public String getDocumentUniqueId() {
        return uniqueId;
    }

    public String[] getTermVector() {
        return termVector;
    }

}
