package apps;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import cluster.Cluster;
import cluster.CosineSimilarity;
import cluster.DocumentVector;
import cluster.DocumentVectorFactory;
import cluster.Linkage;
import cluster.Similarity;
import index.InvertedFileIndex;

public class ClusteringApp {

    public ClusteringApp() {
        // TODO Auto-generated constructor stub
    }

    // returns spaces needed to pad so that columns in cluster.out files align
    private static String prettyPrintSpaces(int lengthNeeded, String whatIHave) {
        String spaces = "";
        int len = whatIHave.length();
        while (len++ <= lengthNeeded)
            spaces += " ";
        return spaces;
    }

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Pass (only) the path to the index (compressed or uncompressed) "
                    + "location on disk as argument.\n");
        }

        String indexPath = args[0];
        System.out.println(indexPath);
        InvertedFileIndex index = new InvertedFileIndex(indexPath);
        DocumentVectorFactory documentVectorFactory = index.getDocumentVectorsFromJSON();

        int numDocs = index.getNumDocs();

        // similarity method to be used
        Similarity similarity = new CosineSimilarity(index);

        try {
            PrintWriter clusterMetadataWriter = new PrintWriter("cluster.out");
            // iterate from threshold values from 0.05 to 0.95 increments of 0.05
            for (int t = 5; t <= 95; t += 5) {

                double threshold = (double) t / 100.0;

                // new set of clusters for this threshold
                ArrayList<Cluster> clusters = new ArrayList<Cluster>();

                // cluster sequence number
                int cId = 1;

                ArrayList<String> docIDs = index.getBackingDocumentIDs();

                System.out.println("threshold: " + threshold);

                for (int doc = 0; doc < numDocs; doc++) {

                    DocumentVector documentVector = documentVectorFactory.getDocumentVector(doc);

                    double bestScore = Double.MIN_VALUE;
                    Cluster bestCluster = null;

                    // compute distance of this doc with each cluster
                    for (Cluster cluster : clusters) {
                        double score = cluster.score(documentVector);

                        if (score > bestScore) {
                            bestCluster = cluster;
                            bestScore = score;
                        }
                    }

                    if (bestCluster != null && bestScore > threshold) {
                        bestCluster.addDocument(documentVector);
                    } else {

                        // a new cluster
                        Cluster cluster = new Cluster(cId++, Linkage.MEAN, similarity);

                        // add this document to the new cluster
                        cluster.addDocument(documentVector);

                        // add the cluster into the list of clusters
                        clusters.add(cluster);
                    }
                }

                // dump the cluster info into a file
                try {
                    PrintWriter printWriter = new PrintWriter("cluster-" + threshold + ".out");
                    clusters.stream().forEach((cluster) -> {
                        clusterMetadataWriter.write(threshold
                                + prettyPrintSpaces(10, threshold + "")
                                + cluster.getClusterId()
                                + prettyPrintSpaces(10, cluster.getClusterId().toString())
                                + cluster.getDocumentsInCluster().size() + "\n");
                        for (DocumentVector document : cluster.getDocumentsInCluster()) {
                            printWriter.write(cluster.getClusterId()
                                    + prettyPrintSpaces(10, cluster.getClusterId().toString())
                                    + docIDs.get(document.getDocId()).split("#")[1] + "\n");
                        }
                    });
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            clusterMetadataWriter.close();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
