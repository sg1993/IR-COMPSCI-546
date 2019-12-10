package apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import index.InvertedFileIndex;
import retriever.evaluation.DirichletEvaluator;
import retriever.evaluation.Evaluator;
import retriever.inferencenetwork.AndBeliefNode;
import retriever.inferencenetwork.InferenceNetworkRetriever;
import retriever.inferencenetwork.PriorNode;
import retriever.inferencenetwork.QueryNode;
import retriever.inferencenetwork.TermProximityNode;

import apps.UtilityFunctions;

public class PriorApp {

    public static void main(String[] args) throws Exception {

        if (args.length <= 2) {
            System.out.println("First argument must be the path to the index!");
        }

        String indexPath = args[0];
        System.out.println(indexPath);
        InvertedFileIndex index = new InvertedFileIndex(indexPath);

        boolean runInfNetEvaluation = false;
        boolean writePriorValueToFile = false;
        String priorFileName = null;

        Options options = new Options();
        options.addOption("w", false, "Write both uniformly-distributed prior values"
                + "and random-prior values to a file on disk");
        options.addOption("r", "", true,
                "Run inference-network evaluation using prior probabilities of documents."
                        + "Needs to be passed a file on disk that has the prior value");

        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(" ", options);

        CommandLineParser parser = new DefaultParser();
        try {

            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("r")) {
                runInfNetEvaluation = true;
                priorFileName = cmd.getOptionValue("r");
                System.out.println(priorFileName);
            } else if (cmd.hasOption("w")) {
                writePriorValueToFile = true;
            } else {
                System.out.println("Nothing to do! Exiting..");
                System.exit(0);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (writePriorValueToFile) {

            // write the priors to a file
            try {
                RandomAccessFile binaryFile = new RandomAccessFile("uniform.prior", "rw");
                // write the same uniform probability for all documents
                double uniform = Math.log(1.0 / index.getNumDocs());
                for (int i = 0; i < index.getNumDocs(); i++) {
                    binaryFile.writeDouble(uniform);
                }
                binaryFile.close();

                // now the ransom-distribution ones
                Random random = new Random(1024);
                double[] rand = new double[index.getNumDocs()];
                double randSum = 0.0;

                for (int i = 0; i < index.getNumDocs(); i++) {
                    rand[i] = random.nextDouble();
                    randSum += rand[i];
                }

                // normalize the random-priors so that they sum to 1
                for (int i = 0; i < index.getNumDocs(); i++) {
                    rand[i] /= randSum;

                    // move from probability-space to log-space
                    rand[i] = Math.log(rand[i]);
                }

                binaryFile = new RandomAccessFile("random.prior", "rw");
                for (int i = 0; i < index.getNumDocs(); i++) {
                    binaryFile.writeDouble(rand[i]);
                }

                binaryFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (runInfNetEvaluation) {

            // pre-computed doc length for speed
            ArrayList<Integer> docLength = new ArrayList<Integer>(Arrays.asList(1586, 911, 3484,
                    2728,
                    116, 3205, 505, 67, 4194, 1726, 2401, 2156, 937, 1179, 3323, 1877, 2335, 921,
                    1722,
                    1493, 590, 3289, 904, 2830, 1895, 1095, 1214, 1140, 2078, 752, 1246, 1102, 289,
                    959,
                    1766, 632, 2683, 550, 2633, 1261, 1510, 221, 397, 1918, 1824, 940, 2960, 919,
                    1146,
                    1347, 111, 1084, 469, 2126, 921, 1925, 731, 1825, 696, 985, 995, 3254, 3601,
                    322,
                    1294, 1568, 173, 531, 126, 153, 1163, 624, 460, 824, 1928, 817, 306, 2469, 123,
                    838,
                    1464, 1977, 2668, 777, 978, 790, 1994, 970, 1271, 605, 1849, 116, 819, 1042,
                    471,
                    297, 134, 228, 51, 2185, 1337, 2298, 3121, 2425, 1225, 1382, 465, 699, 1767,
                    1101,
                    229, 1014, 907, 138, 2037, 987, 1128, 488, 4903, 165, 260, 220, 3237, 143, 371,
                    1444, 2222, 1140, 796, 1570, 1035, 5097, 1650, 3387, 857, 1920, 409, 254, 614,
                    567,
                    1781, 314, 1707, 2598, 3505, 867, 1922, 2488, 871, 986, 1066, 4832, 2240, 1443,
                    1879, 1183, 734, 971, 355, 1227, 868, 633, 1616, 389, 1496, 1354, 898, 984, 402,
                    266, 764, 513, 718, 1170, 1032, 1732, 1283, 820, 409, 1616, 468, 453, 399, 511,
                    511,
                    864, 523, 194, 1682, 1418, 863, 2151, 2424, 3610, 764, 2154, 1714, 1045, 1726,
                    880,
                    696, 1220, 1685, 1880, 669, 2174, 1832, 595, 2593, 481, 797, 215, 1133, 120,
                    365,
                    902, 1059, 425, 820, 704, 2936, 323, 916, 345, 482, 3032, 2506, 1524, 2153,
                    1742,
                    1437, 1489, 3342, 1234, 7988, 2510, 663, 474, 1599, 1803, 1562, 510, 132, 1241,
                    973,
                    982, 1910, 2378, 1341, 259, 647, 326, 291, 888, 844, 584, 1094, 483, 225, 744,
                    883,
                    847, 429, 2034, 871, 967, 992, 1308, 388, 1700, 827, 1784, 725, 322, 1737, 1225,
                    1507, 335, 2175, 1113, 432, 516, 292, 1227, 745, 258, 2775, 4274, 1120, 273,
                    2640,
                    533, 2677, 763, 1203, 1623, 1068, 940, 1457, 1818, 1789, 94, 546, 1287, 509,
                    653,
                    292, 759, 588, 708, 1344, 383, 1274, 498, 245, 1363, 282, 435, 1300, 779, 2056,
                    661,
                    280, 550, 204, 474, 105, 305, 717, 2409, 337, 996, 686, 260, 804, 100, 172, 862,
                    316, 2337, 1455, 2316, 2984, 1323, 1253, 523, 537, 435, 247, 2037, 1412, 510,
                    679,
                    976, 1847, 595, 81, 1416, 640, 2766, 1387, 2830, 1132, 131, 431, 2539, 2346,
                    323,
                    435, 421, 2633, 1074, 62, 960, 298, 739, 1257, 959, 1030, 1968, 1400, 1716,
                    1101,
                    679, 874, 1582, 3849, 2674, 239, 617, 3232, 455, 2271, 985, 1064, 1459, 762,
                    541,
                    2744, 682, 2850, 820, 242, 1185, 358, 523, 1207, 378, 2657, 419, 367, 1648,
                    1059,
                    1710, 1425, 772, 428, 3459, 593, 1052, 316, 3463, 2672, 1560, 248, 2875, 417,
                    1129,
                    1433, 167, 2729, 474, 773, 237, 1624, 214, 977, 997, 724, 856, 483, 249, 349,
                    2517,
                    895, 644, 139, 3051, 1667, 328, 379, 1495, 809, 1777, 560, 480, 1395, 1413, 313,
                    756, 748, 976, 1714, 1486, 893, 144, 254, 3700, 442, 501, 301, 259, 915, 1742,
                    4328,
                    701, 1521, 476, 770, 2353, 1728, 393, 1579, 2350, 2347, 676, 1847, 1538, 289,
                    138,
                    150, 4699, 1456, 1109, 369, 918, 1476, 568, 1006, 512, 790, 674, 947, 391, 152,
                    879,
                    1228, 446, 465, 97, 1645, 2267, 139, 880, 381, 4097, 1831, 585, 1871, 194, 2083,
                    1244, 282, 502, 1084, 7419, 2059, 1608, 1400, 586, 1621, 943, 741, 666, 452,
                    2133,
                    392, 95, 1127, 1241, 1295, 333, 657, 451, 323, 211, 829, 749, 53, 47, 363, 634,
                    334,
                    1842, 168, 461, 259, 386, 184, 335, 175, 369, 316, 94, 50, 460, 102, 1311, 816,
                    705,
                    3321, 592, 4450, 2678, 1665, 881, 1262, 1090, 2283, 2859, 1242, 1203, 752, 1370,
                    190, 576, 1715, 424, 346, 763, 3091, 784, 641, 1124, 377, 1792, 109, 477, 137,
                    1451,
                    1563, 1205, 1503, 406, 1808, 185, 339, 484, 601, 677, 441, 844, 1106, 2706, 326,
                    707, 780, 3948, 195, 2703, 2048, 1952, 629, 1140, 1574, 1406, 973, 2251, 1643,
                    4168,
                    1329, 1675, 1671, 354, 1690, 853, 770, 2056, 844, 2232, 1245, 1645, 3918, 1845,
                    386,
                    3290, 1509, 2375, 1191, 584, 180, 638, 855, 437, 182, 1692, 167, 3433, 860, 494,
                    1258, 1785, 152, 1498, 513, 1077, 324, 1695, 1027, 2398, 3065, 985, 1737, 2127,
                    1173, 1770, 2522, 685, 967, 117, 1264, 2533, 884, 1637, 992, 326, 378, 309, 205,
                    223, 101, 513, 796, 2557, 1084, 1596, 529, 1230, 307, 1263, 501, 456, 571, 1519,
                    1283, 2734, 525, 1195, 607, 214, 340, 1643, 1100, 783, 3352, 2412, 5067, 2955,
                    103,
                    663, 1571, 1227, 2294, 1404, 658, 1483, 164, 518, 209, 400, 1035, 2380, 2282,
                    321,
                    1994, 553, 771, 394, 1023, 973, 1031, 318, 470, 4572, 2037, 160, 109, 748));

            InferenceNetworkRetriever retriever = new InferenceNetworkRetriever(index);
            DirichletEvaluator evaluator = new DirichletEvaluator(1500/* mu */, index, docLength);

            ArrayList<String> id = index.getBackingDocumentIDs();

            // there is only one query to run
            String query = "the king queen royalty";
            PriorNode priorNode = new PriorNode(index, priorFileName);

            ArrayList<QueryNode> children = new ArrayList<QueryNode>();

            // first add the Prior-node to the list of children
            children.add(priorNode);

            // then add the TermProximityNodes for each term in the query
            children.addAll(
                    UtilityFunctions.getTermProximityNodesFromQuery(query, evaluator, index));

            // "AND" is the combine operator
            AndBeliefNode andBeliefNode = new AndBeliefNode();
            andBeliefNode.setChildren(children);

            Integer rank = 1;
            String runTag = "shibingeorge-infnet-and-ql-dir-mu=1500-" + priorFileName;

            PrintWriter pWriter = new PrintWriter(new File(priorFileName + ".trecrun"));

            // run the query and fetch the top-10 results
            for (Entry<Integer, Double> entry : retriever.retrieveQuery(andBeliefNode, 10)) {
                String sceneId = id.get(entry.getKey()).split("#")[1];
                String toWriteString = ("Q1        "
                        + "  skip  " + sceneId + UtilityFunctions.prettyPrintSpaces(35, sceneId)
                        + rank
                        + UtilityFunctions.prettyPrintSpaces(5, rank.toString()) + entry.getValue()
                        + UtilityFunctions.prettyPrintSpaces(20, entry.getValue().toString())
                        + runTag + "\n");
                pWriter.write(toWriteString);
                rank++;
            }

            pWriter.close();
        }
    }

}
