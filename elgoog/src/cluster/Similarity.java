package cluster;

public abstract class Similarity {

    public abstract Double score(DocumentVector d1, DocumentVector d2);

}
