package retriever.inferencenetwork;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import index.Index;
import retriever.Retriever;

public class InferenceNetworkRetriever extends Retriever {

    public InferenceNetworkRetriever(Index i) {
        super(i);
    }

    @Override
    public List<Entry<Integer, Double>> retrieveQuery(QueryNode queryNode, int k) {
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

        int nextDoc = Integer.MAX_VALUE;

        // loop till the the network has more documents to score
        while ((nextDoc = queryNode.nextCandidateDocument()) != Integer.MAX_VALUE) {

            queryNode.skipTo(nextDoc);

            Double score = queryNode.score(nextDoc);
            if (score != null) {
                priorityQueue.add(
                        new AbstractMap.SimpleEntry<Integer, Double>(nextDoc, (-1) * score));
            }

            queryNode.skipTo(nextDoc + 1);
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
}
