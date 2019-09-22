package reader;

public class Document {
    
    private String[] termVector;

    public Document(String text) {
        termVector = text.split("\\s+");
    }
    
    public String[] getTermVector() {
        return termVector;
    }
    
}
