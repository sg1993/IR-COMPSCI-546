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
    @SuppressWarnings("unused")
    private ArrayList<Document> documents;

    // number of docs; the retriver will iterate over 0 to numDocs
    private int numDocs = 0;

    public DocAtATimeRetriever(Index i, ArrayList<Document> docs) {
        super(i);
        documents = docs;
    }

    public DocAtATimeRetriever(Index i, int n) {
        super(i);
        numDocs = n;
    }

    @Override
    public List<Entry<Integer, Double>> retrieveQuery(String[] query, int k, Evaluator evaluator) {

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

        // check if this evaluator accepts even those documents
        // that don't have a query term to assign them a
        // background-probability score
        boolean evaluatorAssignsBackgroundProbability = evaluator.assignsBackgroundProbability();

        // iterate through each document
        for (int i = 0; i < numDocs; i++) {
            int docId = i;
            Double curDocScore = 0.0;
            boolean docScored = false;

            // check each term's I-List and accumulate the score for this doc
            for (InvertedList iList : invertedLists) {
                HashMap<Integer, Posting> postings = iList.getPostings();
                if (postings.containsKey(docId)) {
                    // We have the term in this doc.
                    // Ask the evaluator to score this doc w.r.t this query term
                    docScored = true;
                    curDocScore += evaluator.getDocScoreForQueryTerm(iList.getTerm(),
                            postings.get(docId).getTermFrequency(), docId);
                } else if (evaluatorAssignsBackgroundProbability) {
                    // the evaluator accepts even those docs which don't have the query term.
                    docScored = true;
                    // the term-frequency however should be sent as 0 since
                    // the doc doesn't have the term in it.
                    curDocScore += evaluator.getDocScoreForQueryTerm(iList.getTerm(), 0, docId);
                }
            }

            // add to the Priority-Queue if the doc was scored atleast once!
            if (docScored)
                priorityQueue.add(
                        new AbstractMap.SimpleEntry<Integer, Double>(docId, (-1) * curDocScore));
        }

        List<Entry<Integer, Double>> result = new ArrayList<Map.Entry<Integer, Double>>();

        int i = 0;
        while (!priorityQueue.isEmpty() && i < k) {
            Entry<Integer, Double> entry = priorityQueue.poll();
            entry.setValue(-1 * entry.getValue());
            result.add(entry);
            i++;
        }

        return result;
    }

    @Override
    public double computeDiceCoefficient(String term1, String term2) {

        // conjunctive doc-at-a-time retrieval to compute Dice's coefficient
        // strict ordering - term1 must appear just before term2 to qualify
        // for dice's coefficient score

        // InvertedLists for the two query terms
        InvertedList l1 = ((InvertedFileIndex) index).getInvertedListForTerm(term1);
        InvertedList l2 = ((InvertedFileIndex) index).getInvertedListForTerm(term2);

        // how many times does termA appear in the entire collection
        double countA = l1.getCollectionFrequency();

        // how many times does termB appear in the entire collection
        double countB = l2.getCollectionFrequency();

        // compute how many times does termA just precedes termB
        // (consecutive occurrence in a document)

        double countAB = 0;
        for (Entry<Integer, Posting> entry : l1.getPostings().entrySet()) {
            int docId = entry.getKey();
            Posting p = entry.getValue();

            HashMap<Integer, Posting> entry2 = l2.getPostings();
            if (entry2.containsKey(docId)) {
                // both term1 and term2 are in the same doc
                Posting q = entry2.get(docId);

                // System.out.println("docid: " + docId);
                countAB += getCountOfConsecutivePositions(p.getPositions(), q.getPositions());
            }
        }

        // System.out.println(
        // term1 + ": " + countA + ", " + term2 + ": " + countB + ", countAB: " +
        // countAB);

        return (countAB) / (countA + countB);
    }

    private int getCountOfConsecutivePositions(ArrayList<Integer> positionsA,
            ArrayList<Integer> positionsB) {

        int count = 0;
        /*
         * test: positionsA = new ArrayList<Integer>(); positionsB = new
         * ArrayList<Integer>(); positionsA.add(1); positionsA.add(5);
         * positionsA.add(7); positionsA.add(8); positionsA.add(9); positionsA.add(10);
         * positionsB.add(2); positionsB.add(3); positionsB.add(4); positionsB.add(5);
         * positionsB.add(11); positionsB.add(12);
         */
        // two pointers to iterate over the position-arrays respectively
        int pointerA = 0, pointerB = 0;
        int len1 = positionsA.size(), len2 = positionsB.size();

        while (pointerA < len1 && pointerB < len2) {
            int a = positionsA.get(pointerA), b = positionsB.get(pointerB);
            if ((a + 1) == b) {
                // consecutive positions - term A just before term B
                count++;
                pointerA++;
                pointerB++;
                // System.out.println(a + " and " + b);
            } else if (a < b) {
                // increment the pointer to the next position
                pointerA++;
            } else {
                pointerB++;
            }
        }

        // System.out.println("count is " + count);
        return count;
    }
}
