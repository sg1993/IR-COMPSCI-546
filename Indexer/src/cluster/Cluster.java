package cluster;

import java.util.ArrayList;

public class Cluster {

    int clusterId;

    Linkage linkage;

    Similarity similarityMethod;

    ArrayList<DocumentVector> documents;

    private DocumentVector centroidDocumentVector;

    public Cluster(int cId, Linkage l, Similarity s) {
        clusterId = cId;
        linkage = l;
        similarityMethod = s;
        documents = new ArrayList<DocumentVector>();
        centroidDocumentVector = new DocumentVector(Integer.MIN_VALUE);
    }

    public void addDocument(DocumentVector documentVector) {

        int numDocsInCluster = documents.size();

        // update the centroid vector for this cluster
        for (String term : documentVector.getTerms()) {
            // get the averaged-value for this term before adding the incoming vector
            int count = centroidDocumentVector.getTermCount(term);
            count *= numDocsInCluster;

            // add the incoming vector's count for this term for the new centroid
            count += documentVector.getTermCount(term);
            count /= (numDocsInCluster + 1);

            // update the centroid with the new count
            centroidDocumentVector.addTerm(term, count);
        }

        // now add the incoming-document vector into the cluster
        documents.add(documentVector);
    }

    public double score(DocumentVector documentVector) {
        switch (linkage) {
        case SINGLE:
            return getSingleLinkageScore(documentVector);
        case COMPLETE:
            return getCompleteLinkageScore(documentVector);
        case AVERAGE:
            return getAverageLinkageScore(documentVector);
        case MEAN:
            return getMeanLinkageScore(documentVector);
        default:
            return 0.0;
        }
    }

    private double getMeanLinkageScore(DocumentVector documentVector) {

        // return the similarity-score of the incoming document
        // with the centroid representation of the cluster
        return similarityMethod.score(documentVector, centroidDocumentVector);
    }

    private double getAverageLinkageScore(DocumentVector documentVector) {

        // average linkage returns the average of all pairwise similarity-scores
        // incoming document and the documents in this cluster
        double averageScore = 0.0;
        int numDocuments = 0;

        for (DocumentVector document : documents) {
            averageScore += similarityMethod.score(document, documentVector);
            numDocuments += 1;
        }

        return averageScore / numDocuments;
    }

    private double getCompleteLinkageScore(DocumentVector documentVector) {

        // Complete linkage only considers the maximum distance or
        // the minimum similarity-score between the
        // incoming document and the documents in this cluster
        double minScore = Double.MAX_VALUE;

        for (DocumentVector document : documents) {
            minScore = Math.min(similarityMethod.score(document, documentVector), minScore);
        }

        return minScore;
    }

    private double getSingleLinkageScore(DocumentVector documentVector) {

        // single linkage only considers the minimum distance or
        // the maximum similarity-score between the
        // incoming document and the documents in this cluster
        double maxScore = Double.MIN_VALUE;

        for (DocumentVector document : documents) {
            maxScore = Math.max(similarityMethod.score(document, documentVector), maxScore);
        }

        return maxScore;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public ArrayList<DocumentVector> getDocumentsInCluster() {
        return documents;
    }
}
