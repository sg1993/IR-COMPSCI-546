package apps;

import java.util.HashMap;
import java.util.Map.Entry;

public class RelevanceJudge {

    public static String[] rank = { "richard_iii:4.2                     1 ",
            "midsummer_nights_dream:3.0          2 ", "antony_and_cleopatra:1.0            3 ",
            "romeo_and_juliet:0.3                4 ", "richard_iii:0.3                     5 ",
            "hamlet:2.0                          6 ", "cymbeline:4.3                       7 ",
            "romeo_and_juliet:4.0                8 ", "twelfth_night:3.0                   9 ",
            "cymbeline:3.1                       10", "antony_and_cleopatra:1.0            1 ",
            "richard_iii:4.2                     2 ", "midsummer_nights_dream:3.0          3 ",
            "hamlet:2.0                          4 ", "richard_iii:0.3                     5 ",
            "richard_iii:3.1                     6 ", "romeo_and_juliet:0.3                7 ",
            "cymbeline:3.1                       8 ", "merchant_of_venice:1.4              9 ",
            "macbeth:2.1                         10", "richard_iii:4.2                     1 ",
            "midsummer_nights_dream:3.0          2 ", "richard_iii:0.3                     3 ",
            "romeo_and_juliet:0.3                4 ", "antony_and_cleopatra:1.0            5 ",
            "hamlet:2.0                          6 ", "cymbeline:3.1                       7 ",
            "cymbeline:4.3                       8 ", "romeo_and_juliet:4.0                9 ",
            "richard_iii:3.1                     10" };

    static HashMap<String, Double> map = new HashMap<String, Double>();

    public static void main(String args[]) {

        for (String s : rank) {
            String sceneString = s.split("\\s+")[0];
            Integer sceneRank = Integer.valueOf(s.split("\\s+")[1]);

            double score = 10.0 / (double) sceneRank;
            if (map.containsKey(sceneString)) {
                map.put(sceneString, map.get(sceneString) + score);
            } else {
                map.put(sceneString, score);
            }
        }

        Double maxValDouble = Double.MIN_VALUE, minValDouble = Double.MAX_VALUE;

        for (Entry<String, Double> entry : map.entrySet()) {
            // shift these values into
            if (entry.getValue() > maxValDouble) {
                maxValDouble = entry.getValue();
            }
            if (entry.getValue() < minValDouble) {
                minValDouble = entry.getValue();
            }
            // System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println(maxValDouble + " to " + minValDouble);

        for (Entry<String, Double> entry : map.entrySet()) {
            // shift these values into
            Double ShiftedScore = (entry.getValue() - minValDouble) * (2)
                    / (maxValDouble - minValDouble) + 1;
            System.out.println(entry.getKey() + " " + Math.round(ShiftedScore));
        }
    }
}
