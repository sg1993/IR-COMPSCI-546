package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.stream.Collector;

public class ClusterProcessing {

    static HashMap<Integer, ArrayList<String>> collector = new HashMap<Integer, ArrayList<String>>();

    public static void main(String[] args) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(
                    "C:\\Users\\georg\\motherlode\\UMass\\cs546\\IR-COMPSCI-546\\Indexer\\cluster.out"));
            String line = "";
            String thresholdString = "";
            while ((line = fileReader.readLine()) != null) {
                String[] s = line.split("\\s+");
                if (!s[0].equals(thresholdString)) {
                    prettyPrint(thresholdString, collector);
                    collector.clear();
                }
                ArrayList<String> arrayList = collector.getOrDefault(Integer.valueOf(s[2]), null);
                if (arrayList == null) {
                    arrayList = new ArrayList<String>();
                }
                arrayList.add(s[1]);
                collector.put(Integer.valueOf(s[2]), arrayList);
                thresholdString = s[0];
            }
            prettyPrint(thresholdString, collector);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void prettyPrint(String thresholdString,
            HashMap<Integer, ArrayList<String>> collector2) {
        int totalClusters = 0;
        for (Entry<Integer, ArrayList<String>> entry : collector2.entrySet()) {
            totalClusters += entry.getValue().size();
        }
        System.out.println(thresholdString + " -> size of cluster = " + totalClusters);

        collector2.keySet().stream().sorted().forEach((clusterSize) -> {
            System.out.println(collector2.get(clusterSize) + "\t" + clusterSize);
        });

    }

}
