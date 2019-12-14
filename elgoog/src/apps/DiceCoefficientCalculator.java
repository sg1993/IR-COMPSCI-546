package apps;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import index.InvertedFileIndex;
import retriever.DocAtATimeRetriever;

public class DiceCoefficientCalculator {

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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Pass (only) the path to the index location on disk as argument.");
        }

        String indexPath = args[0];
        System.out.println(indexPath);
        InvertedFileIndex index = new InvertedFileIndex(indexPath);

        int numDocs = index.getNumDocs();
        System.out.println("There are " + numDocs + " docs in the collection.");

        // Pass the invertedFileIndex and numDocs into the retriever
        DocAtATimeRetriever retriever = new DocAtATimeRetriever(index, numDocs);

        ArrayList<String> vocabList = index.getVocabListFromIndex();

        // compute the best pair for each of the 700 terms in the SEVEN_TERM_QUERY_SET
        // the best pair for query-term is the one that has the best dice's coefficient
        // of all the terms in the collection.
        try {
            // write all the best pairs into a .bestpair file
            PrintWriter bestPairWriter = new PrintWriter(indexPath + ".bestpair");

            long startTime = System.currentTimeMillis();
            for (String queryTerm : SEVEN_TERM_QUERY_SET) {
                String bestPair = "";
                Double bestDicesValue = 0.0;
                for (String vocabTerm : vocabList) {
                    Double dValue = retriever.computeDiceCoefficient(queryTerm, vocabTerm);
                    if (dValue > bestDicesValue) {
                        bestDicesValue = dValue;
                        bestPair = vocabTerm;
                    }
                }
                bestPairWriter.write(
                        queryTerm + " : " + bestPair + "\t\t\t\t\t\t" + bestDicesValue + "\n");
            }
            long endTime = System.currentTimeMillis();
            System.out.println("It took " + (endTime - startTime)
                    + " milliseconds to compute best pair for 700 terms.");
            bestPairWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
