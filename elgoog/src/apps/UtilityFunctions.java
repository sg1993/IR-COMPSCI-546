package apps;

import java.util.ArrayList;

import index.InvertedFileIndex;
import retriever.evaluation.Evaluator;
import retriever.inferencenetwork.TermProximityNode;

public final class UtilityFunctions {

    public static ArrayList<TermProximityNode> getTermProximityNodesFromQuery(String query,
            Evaluator evaluator, InvertedFileIndex index) {

        ArrayList<TermProximityNode> list = new ArrayList<TermProximityNode>();
        String[] terms = query.split("\\s+");

        for (String term : terms) {
            TermProximityNode node = new TermProximityNode(evaluator,
                    index.getInvertedListForTerm(term));
            list.add(node);
        }

        return list;
    }

    // returns spaces needed to pad so that columns in .trecrun files align
    public static String prettyPrintSpaces(int lengthNeeded, String whatIHave) {
        String spaces = "";
        int len = whatIHave.length();
        while (len++ <= lengthNeeded)
            spaces += " ";
        return spaces;
    }

}
