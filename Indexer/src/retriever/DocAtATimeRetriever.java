package retriever;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import index.Index;
import index.InvertedFileIndex;
import index.InvertedList;
import index.Posting;
import reader.Document;

public class DocAtATimeRetriever extends Retriever {

    // Doc at a time retriever needs to know number of documents
    // so just to be on the safe side, keep the document-store itself.
    private ArrayList<Document> documents;

    public DocAtATimeRetriever(Index i, ArrayList<Document> docs) {
        super(i);
        documents = docs;
    }

    @Override
    public List<Entry<Integer, Double>> retrieveQuery(String[] query, int k) {

        PriorityQueue<Map.Entry<Integer, Double>> priorityQueue = new PriorityQueue<Map.Entry<Integer, Double>>(
                new Comparator<Map.Entry<Integer, Double>>() {

                    @Override
                    public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
                        if (o1.getValue() < o2.getValue()) {
                            return -1;
                        } else if (o1.getValue() > o2.getValue()) {
                            return 1;
                        }
                        return 0;
                    }

                });

        // list of InvertedLists for each query term
        ArrayList<InvertedList> invertedLists = new ArrayList<InvertedList>();

        // fetch inverted list of all the query terms
        for (String q : query) {
            // Doc-At-A-Time retrieval needs an inverted index
            // so we cast the index object appropriately
            InvertedList list = ((InvertedFileIndex) index).getInvertedListForTerm(q);
            if (list == null) {
                System.out.println("Didn't find an inverted index associated with this term!");
                continue;
            }
            // list.printSelf();
            invertedLists.add(list);
        }

        int numDocs = documents.size();

        // iterate through each document
        for (int i = 0; i < numDocs; i++) {
            int docId = documents.get(i).getDocumentUniqueId();
            Double curDocScore = 0.0;

            // check each term's I-List and accumulate the score for this doc
            for (InvertedList iList : invertedLists) {
                HashMap<Integer, Posting> postings = iList.getPostings();
                if (postings.containsKey(docId)) {
                    // we have the term in this doc
                    // ad the raw-count to this doc's score
                    curDocScore += postings.get(docId).getTermFrequency();
                }
            }
            if (curDocScore > 0)
                System.out.println("Score of " + docId + ": " + curDocScore);

            if (curDocScore != 0)
                priorityQueue.add(
                        new AbstractMap.SimpleEntry<Integer, Double>(docId, (-1) * curDocScore));
        }

        List<Entry<Integer, Double>> result = new ArrayList<Map.Entry<Integer, Double>>();

        int i = 0;
        while (!priorityQueue.isEmpty() && i < k) {

            Entry<Integer, Double> entry = priorityQueue.poll();
            entry.setValue(-1 * entry.getValue());
            System.out.println(entry.toString());
            result.add(entry);
            i++;
        }

        return result;
    }

}
