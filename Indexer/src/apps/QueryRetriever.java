package apps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import index.InvertedFileIndex;
import index.InvertedList;
import reader.SceneReader;
import retriever.DocAtATimeRetriever;

public class QueryRetriever {

    private static String[] SEVEN_TERM_QUERY_SET = { "effects", "unearned", "burr", "indigne",
            "chequin", "lisp", "rains", "isbel", "cast", "guise", "seemingly", "wire", "twelve",
            "sheet", "judgest", "pulse", "swollen", "garcon", "firebrand", "outvenom", "row",
            "brace", "affirm", "couronne", "pleaseth", "invocation", "nereid", "viz", "madame",
            "translate", "marjoram", "unmerite", "cheer", "antipholu", "leek", "orleans",
            "overspread", "riches", "rigg", "entrap", "cent", "lone", "patroclu", "curate",
            "plummet", "hugh", "fancies", "pigmy", "opinion", "shock", "glance", "despairing",
            "journey", "damnation", "hundredth", "pious", "calves", "bide", "hermione", "yonder",
            "jake", "ergrowth", "trempl", "marseille", "magician", "unweeded", "venice", "value",
            "vitravio", "unpleasant", "conflux", "bray", "diffuse", "remorseful", "counter", "soe",
            "toast", "vulnerable", "churchmen", "stanze", "heirless", "directitude", "falourous",
            "cerimon", "tow", "sustain", "gingerly", "nonino", "bel", "edgeless", "hyen", "po",
            "quench", "griffin", "pursent", "bene", "distinction", "unprovoke", "condemn", "hap",
            "mare", "fiddle", "ember", "topless", "censer", "undescry", "celestial", "descant",
            "counterpoint", "highly", "accused", "overswear", "enamour", "topsy", "ladyship",
            "waterish", "velvet", "senator", "cuore", "u", "outstretch", "horizon", "spoke",
            "phlegmatic", "expire", "soften", "innumerable", "dispose", "antiopa", "feel", "thine",
            "coil", "spiritless", "recollect", "sanguine", "faite", "in", "strive", "lapland",
            "ulcer", "winning", "knead", "solon", "mouse", "sophy", "terre", "ubique", "grain",
            "confines", "tween", "infernal", "bachelor", "contrarious", "lee", "worky", "humble",
            "unlimited", "reconcile", "gull", "gosling", "quatch", "isidore", "accessible", "solem",
            "carnarvonshire", "bedimm", "have", "plantage", "obstruction", "erhang", "gormandise",
            "contributor", "midst", "sharpest", "firm", "comparison", "bertram", "mansionry",
            "confession", "michael", "uncapable", "armory", "allure", "mulmutiu", "alone",
            "putrefy", "usure", "hannibal", "brooch", "effeminate", "carnarvonshire", "attractive",
            "join", "clownish", "attend", "save", "nunnery", "snare", "cleanse", "fun", "aloft",
            "townsmen", "unstanched", "tax", "southern", "overfond", "harcourt", "wings",
            "rejoiceth", "wooing", "heatest", "ciel", "fin", "semblable", "unyoked", "render",
            "allicho", "gibber", "foix", "resignation", "hinderd", "joineth", "ungovern",
            "ostentare", "mining", "qualified", "ajax", "harry", "principal", "spiteful",
            "votarist", "had", "imman", "tend", "rewards", "saucer", "teem", "mark", "pretty",
            "import", "received", "stag", "dismantle", "garrison", "sumpter", "caelo", "dungeon",
            "slipt", "metamorphose", "thing", "apprenticehood", "heighten", "coffers", "icicle",
            "quod", "wholesomest", "florizel", "ha", "fester", "disappear", "ridden", "forenoon",
            "gabriel", "mark", "cesse", "whereon", "pole", "unmuzzled", "ecstasy", "depart",
            "discandy", "diminish", "auvergne", "bessy", "damage", "disputable", "venerable",
            "gaul", "treason", "enwrap", "devil", "venge", "fornication", "eglantine", "menecrate",
            "vocation", "hearty", "parler", "forewarn", "recruit", "longboat", "oblige", "kate",
            "score", "slough", "invasion", "unparted", "bodily", "region", "disdain", "boat",
            "downward", "derision", "loss", "half", "glass", "scantl", "adventure", "curry",
            "basset", "magnificoe", "helping", "dwindle", "rhyme", "variance", "venturous",
            "betroth", "stark", "asunder", "martem", "mecaena", "joinder", "cheers", "require",
            "ancient", "unweighed", "sip", "cilicia", "faithful", "noblesse", "chivalry",
            "livelong", "verona", "untempere", "starling", "bad", "mortise", "scare", "bertram",
            "antioch", "miller", "brewage", "unholy", "pilchard", "prompter", "forbade", "camp",
            "eldest", "familiarly", "augury", "pour", "arose", "ungrateful", "grandsire",
            "needlework", "fourth", "gaunt", "impound", "chanticleer", "carnarvonshire", "bessy",
            "throat", "slightly", "sheepcote", "arms", "waits", "martext", "drawbridge", "tout",
            "float", "tah", "diedst", "hearten", "popp", "carlot", "northerly", "thumb", "unnoble",
            "aquilon", "once", "mangle", "eyest", "lapwing", "bodkin", "turnbull", "affectionate",
            "wicked", "emboss", "handmaid", "pierce", "through", "stripe", "canterbury", "wist",
            "beg", "goddesse", "engineer", "crossing", "hallow", "esteemest", "undraw", "outgoe",
            "colour", "twould", "theirs", "remains", "friendly", "intermix", "repulse", "capital",
            "welshman", "reflection", "salary", "lennox", "degree", "honeycomb", "constringe",
            "unwise", "brink", "wall", "aeacide", "fluellen", "governor", "bepaint", "bartholomew",
            "withdrew", "dibble", "errant", "encircle", "fervor", "pothecary", "ink", "2", "wither",
            "extravagant", "button", "las", "likeness", "affiance", "evans", "cassado", "apology",
            "seigeurie", "mile", "imitari", "scribble", "unfortify", "sanguine", "coelestibu",
            "fives", "unprovided", "dorca", "opposition", "dido", "dispossess", "uneven", "foggy",
            "cain", "displeasure", "spend", "jaunt", "spoil", "sprag", "culpable", "item", "hop",
            "twere", "erga", "dodge", "proscription", "mought", "sailmaker", "worth", "briareu",
            "hearten", "warily", "contraction", "gabriel", "alarbu", "nonino", "controversy",
            "collected", "wherein", "charneco", "trenchant", "jog", "anthropophagi", "unpregnant",
            "disappointed", "carve", "reclaim", "extremities", "clare", "defame", "la", "vincere",
            "alphabetical", "gelding", "crossest", "aeson", "udge", "pond", "lone", "impress",
            "abridge", "pomfret", "skilful", "erpay", "peascod", "vincere", "confused", "cabby",
            "deer", "struck", "sad", "l", "prophetic", "importune", "wanteth", "disproportion",
            "pribble", "comptible", "gamesome", "continue", "lodging", "enforce", "spectacles",
            "ulcerous", "christendom", "sole", "spectatorship", "til", "his", "tuition", "furthest",
            "manned", "mightful", "cheval", "ominous", "bestow", "hampton", "item", "hearer",
            "distasteful", "stalls", "pug", "jer", "shrowd", "sun", "flier", "metaphor", "disgrace",
            "remonstrance", "terrace", "foretell", "choir", "captivate", "orb", "rood", "cashier",
            "pody", "mummy", "sorrow", "offence", "dotage", "inwards", "learn", "nicanor", "disme",
            "clutch", "tabourer", "rush", "method", "soothe", "zone", "faith", "rancour", "brunt",
            "progne", "sacrilegious", "slain", "dullard", "beak", "hebrew", "staff", "wheresome",
            "bewray", "rome", "yielding", "everyone", "construction", "thee", "in", "8d",
            "commixture", "diversity", "cannibal", "needless", "fumiter", "armoury", "ensemble",
            "knead", "foresay", "enticeth", "pug", "receipt", "coronet", "thicket", "powers",
            "grandfather", "incision", "woof", "zany", "stare", "calmie", "jangle", "disinherit",
            "ballow", "induce", "misthought", "malice", "urswick", "mare", "regarding", "endurest",
            "heft", "hob", "stygian", "premise", "afflict", "fume", "sphinx", "enchase", "unlucky",
            "widower", "misbeliever", "vial", "certainly", "cassandra", "verger", "garbage",
            "conduit", "outrage", "incur", "shouldest", "loss", "george", "vomit", "untroubled",
            "extremity", "away", "unseen", "beshrew", "oberon", "unlike", "scratch", "abstemious",
            "departure", "clapp", "tickle", "brittany", "climbeth", "hurtle", "lee", "opulent",
            "forthwith", "imputation", "debt", "obedience", "erewhile", "certain", "undercrest",
            "miscall", "league", "alas", "clownish", "shipman", "polydote", "lightning",
            "mischievous", "proteu", "pedler", "bowed", "statist", "pold", "porringer", "enfree",
            "marry", "green", "ruff", "fel", "offendress", "epicure", "abhorr", "zenelophon", "ale",
            "respect", "elm", "elbe", "savour" };

    public static void main(String args[]) {

        boolean generateRandomQueries = false, queryRetrieval = false, recordTermStatistics = false;

        int numQueries = 0, numTermsInQuery = 0;
        String indexPath = null;

        // parse the arguments using Apache-CLI
        Options options = new Options();
        Option rOption = new Option("r", true,
                "Generate random query terms, from vocab, without replacement, "
                        + "requires 3 args: index-file location on disk, "
                        + "how many queries to generate, how many terms in each query "
                        + "(in that order!)");

        // Takes 3 args - index-file location on disk, how many queries to generate, how
        // many terms in each query
        rOption.setArgs(3);
        rOption.setValueSeparator(' ');
        options.addOption(rOption);

        options.addOption("q", "run-query", true,
                "Run query-retrieval. 100 sets of 7-term query. Requires 1 arg: the index path on disk.");

        options.addOption("s", "get-stats", true,
                "Get df/tf for 100 sets of 7-term query. Requires 1 arg: the index path on disk.");

        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(" ", options);

        CommandLineParser parser = new DefaultParser();
        try {

            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("r")) {
                generateRandomQueries = true;
                String[] res = cmd.getOptionValues("r");
                System.out.println(res);
                indexPath = res[0];
                numQueries = Integer.valueOf(res[1]);
                numTermsInQuery = Integer.valueOf(res[2]);
            } else if (cmd.hasOption("q")) {
                queryRetrieval = true;
                indexPath = cmd.getOptionValue("q");
            } else if (cmd.hasOption("s")) {
                recordTermStatistics = true;
                indexPath = cmd.getOptionValue("s");
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (generateRandomQueries) {
            // get vocabulary document-store from the index
            InvertedFileIndex index = new InvertedFileIndex(indexPath);
            ArrayList<String> vocabList = index.getVocabListFromIndex();
            System.out.println("There are " + vocabList.size() + " terms in the vocab.");

            for (int i = 0; i < numQueries; i++) {
                Collections.shuffle(vocabList);
                System.out.println(vocabList.subList(0, numTermsInQuery));
            }

        } else if (queryRetrieval) {

            InvertedFileIndex index = new InvertedFileIndex(indexPath);

            // Pass the invertedFileIndex into the retriever
            DocAtATimeRetriever retriever = new DocAtATimeRetriever(index, index.getNumDocs());

            // query retrieval using 7 terms from the array at a time
            int len = SEVEN_TERM_QUERY_SET.length, queryNum = 1;
            for (int i = 0; i < len;) {
                String[] query = Arrays.copyOfRange(SEVEN_TERM_QUERY_SET, i, i + 7);
                System.out.println("Query #" + queryNum + ": " + Arrays.toString(query));

                // top 10 results
                System.out.println(retriever.retrieveQuery(query, 10));
                i += 7;
                queryNum++;
            }
        } else if (recordTermStatistics) {
            InvertedFileIndex index = new InvertedFileIndex(indexPath);
            int len = SEVEN_TERM_QUERY_SET.length;
            for (int i = 0; i < len; i++) {
                String term = SEVEN_TERM_QUERY_SET[i];
                InvertedList iL = index.getInvertedListForTerm(term);
                System.out.println("\"" + term + "\" appears " + iL.getCollectionFrequency()
                        + " times in the corpus.");
                System.out.println("\"" + term + "\" appears in " + iL.getDocumentFrequency()
                        + " documents at-least once.");
            }
        }
    }
}
