package cluster;

import java.util.HashSet;

import index.Index;

public class CosineSimilarity extends Similarity {

    Index index = null;

    public CosineSimilarity(Index index) {
        this.index = index;
    }

    @Override
    public Double score(DocumentVector d1, DocumentVector d2) {
        HashSet<String> termSet = new HashSet<String>() {
            {
                addAll(d1.getTerms());
                addAll(d2.getTerms());
            }
        };

        double cosineScore = 0.0;
        double den1 = 0.0;
        double den2 = 0.0;

        for (String term : termSet) {

            // tf-component is the raw term-frequency of each term
            double tf1 = (double) (d1.getTermCount(term));
            double tf2 = (double) (d2.getTermCount(term));

            // the idf-component is common for both vectors for this term
            double idf = ((double) index.getNumDocs() + 1.0)
                    / ((double) (index.getDocumentFrequencyForTerm(term)) + 0.5);

            double tfIdf1 = tf1 * idf;
            double tfIdf2 = tf2 * idf;

            cosineScore += (tfIdf1 * tfIdf2);

            den1 += tfIdf1 * tfIdf1;
            den2 += tfIdf2 * tfIdf2;

        }

        cosineScore /= (Math.sqrt(den1) * Math.sqrt(den2));

        return cosineScore;

    }
}
