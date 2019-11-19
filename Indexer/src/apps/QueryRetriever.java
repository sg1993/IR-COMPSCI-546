package apps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import index.InvertedFileIndex;
import index.InvertedList;
import retriever.DocAtATimeRetriever;
import retriever.Retriever;
import retriever.evaluation.RawCountEvaluator;

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

    // 1400 query terms or 700 pairs of words where each pair is a word and its
    // best-pair w.r.t Dice's Coefficient
    private static String[] FOURTEEN_TERM_QUERY_SET = { "effects", "corneliu", "unearned", "luck",
            "burr", "vile", "indigne", "serviteur", "chequin", "were", "lisp", "affecting", "rains",
            "downright", "isbel", "since", "cast", "away", "guise", "when", "seemingly", "obedient",
            "wire", "and", "twelve", "score", "sheet", "bleach", "judgest", "false", "pulse",
            "twice", "swollen", "parcel", "garcon", "a", "firebrand", "brother", "outvenom", "all",
            "row", "pluck", "brace", "re", "affirm", "it", "couronne", "le", "pleaseth", "burgundy",
            "invocation", "cardinal", "nereid", "so", "viz", "these", "madame", "comme",
            "translate", "ourself", "marjoram", "king", "unmerite", "proud", "cheer", "appall",
            "antipholu", "of", "leek", "today", "orleans", "alencon", "overspread", "with",
            "riches", "fineless", "rigg", "st", "entrap", "thee", "cent", "ecu", "lone", "woman",
            "patroclu", "thersite", "curate", "alexander", "plummet", "sound", "hugh", "evans",
            "fancies", "prick", "pigmy", "arms", "opinion", "crush", "shock", "them", "glance",
            "full", "despairing", "shouldst", "journey", "bated", "damnation", "add", "hundredth",
            "psalm", "pious", "chanson", "calves", "guts", "bide", "until", "hermione", "mamilliu",
            "yonder", "tower", "jake", "with", "ergrowth", "of", "trempl", "of", "marseille",
            "road", "magician", "obscure", "unweeded", "garden", "venice", "venetia", "value",
            "differ", "vitravio", "signior", "unpleasant", "st", "conflux", "of", "bray", "trumpet",
            "diffuse", "attire", "remorseful", "pardon", "counter", "caster", "soe", "er", "toast",
            "cheese", "vulnerable", "crest", "churchmen", "pray", "stanze", "a", "heirless", "it",
            "directitude", "first", "falourous", "gentleman", "cerimon", "philemon", "tow", "me",
            "sustain", "moe", "gingerly", "lucetta", "nonino", "these", "bel", "s", "edgeless",
            "sword", "hyen", "and", "po", "it", "quench", "fire", "griffin", "and", "pursent",
            "three", "bene", "intelligo", "distinction", "provide", "unprovoke", "it", "condemn",
            "seconds", "hap", "betide", "mare", "together", "fiddle", "em", "ember", "up",
            "topless", "deputation", "censer", "in", "undescry", "perdita", "celestial", "harmony",
            "descant", "there", "counterpoint", "costly", "highly", "heapt", "accused", "freely",
            "overswear", "and", "enamour", "d", "topsy", "turvy", "ladyship", "silvia", "waterish",
            "diet", "velvet", "hose", "senator", "c", "cuore", "ben", "u", "don", "outstretch", "d",
            "horizon", "we", "spoke", "aloud", "phlegmatic", "hear", "expire", "moreover", "soften",
            "steel", "innumerable", "substance", "dispose", "tout", "antiopa", "titania", "feel",
            "retiring", "thine", "own", "coil", "must", "spiritless", "so", "recollect", "terms",
            "sanguine", "star", "faite", "vous", "in", "the", "strive", "mightily", "lapland",
            "sorcerer", "ulcer", "hamlet", "winning", "match", "knead", "clod", "solon", "s",
            "mouse", "trap", "sophy", "sir", "terre", "orleans", "ubique", "then", "grain", "tort",
            "confines", "sly", "tween", "snow", "infernal", "ate", "bachelor", "sit", "contrarious",
            "quest", "lee", "d", "worky", "day", "humble", "bee", "unlimited", "seneca",
            "reconcile", "dumb", "gull", "catcher", "gosling", "to", "quatch", "buttock", "isidore",
            "isidore", "accessible", "is", "solem", "out", "carnarvonshire", "although", "bedimm",
            "d", "have", "been", "plantage", "to", "obstruction", "malvolio", "erhang", "firmament",
            "gormandise", "as", "contributor", "and", "midst", "john", "sharpest", "blow", "firm",
            "fixture", "comparison", "apart", "bertram", "lafeu", "mansionry", "that", "confession",
            "justify", "michael", "cassio", "uncapable", "of", "armory", "with", "allure", "beauty",
            "mulmutiu", "made", "alone", "uphold", "putrefy", "core", "usure", "senate", "hannibal",
            "drive", "brooch", "table", "effeminate", "changeable", "carnarvonshire", "although",
            "attractive", "eyes", "join", "stool", "clownish", "fool", "attend", "on", "save",
            "yourself", "nunnery", "go", "snare", "uncaught", "cleanse", "them", "fun", "soul",
            "aloft", "lessen", "townsmen", "yet", "unstanched", "thirst", "tax", "signior",
            "southern", "cloud", "overfond", "of", "harcourt", "look", "wings", "misdoubteth",
            "rejoiceth", "my", "wooing", "here", "heatest", "my", "ciel", "cousin", "fin",
            "couronne", "semblable", "coherence", "unyoked", "humour", "render", "vengeance",
            "allicho", "and", "gibber", "in", "foix", "beaumont", "resignation", "of", "hinderd",
            "by", "joineth", "rouen", "ungovern", "d", "ostentare", "to", "mining", "all",
            "qualified", "too", "ajax", "ajax", "harry", "percy", "principal", "glancing",
            "spiteful", "execrate", "votarist", "roots", "had", "been", "imman", "and", "tend",
            "foh", "rewards", "hast", "saucer", "sweet", "teem", "loins", "mark", "antony",
            "pretty", "dimple", "import", "bigot", "received", "belief", "stag", "bassianu",
            "dismantle", "was", "garrison", "york", "sumpter", "to", "caelo", "the", "dungeon",
            "denmark", "slipt", "like", "metamorphose", "with", "thing", "about", "apprenticehood",
            "to", "heighten", "d", "coffers", "ransack", "icicle", "hang", "quod", "sufficit",
            "wholesomest", "spirt", "florizel", "perdita", "ha", "ha", "fester", "gainst",
            "disappear", "pericle", "ridden", "with", "forenoon", "in", "gabriel", "s", "mark",
            "antony", "cesse", "lafeu", "whereon", "hyperion", "pole", "clipt", "unmuzzled",
            "thought", "ecstasy", "duncan", "depart", "unkiss", "discandy", "melt", "diminish",
            "one", "auvergne", "laughest", "bessy", "to", "damage", "add", "disputable", "for",
            "venerable", "burthen", "gaul", "france", "treason", "lurk", "enwrap", "me", "devil",
            "himself", "venge", "thy", "fornication", "adultery", "eglantine", "whom", "menecrate",
            "know", "vocation", "hal", "hearty", "commendation", "parler", "comment", "forewarn",
            "wind", "recruit", "c", "longboat", "s", "oblige", "faith", "kate", "katharine",
            "score", "hogshead", "slough", "doth", "invasion", "shall", "unparted", "to", "bodily",
            "health", "region", "kite", "disdain", "destruction", "boat", "sail", "downward",
            "look", "derision", "medicine", "loss", "assume", "half", "an", "glass", "pomander",
            "scantl", "of", "adventure", "retire", "curry", "with", "basset", "crossing",
            "magnificoe", "antonio", "helping", "baptista", "dwindle", "peak", "rhyme", "planet",
            "variance", "antony", "venturous", "fairy", "betroth", "himself", "stark", "naked",
            "asunder", "ah", "martem", "that", "mecaena", "gallu", "joinder", "of", "cheers",
            "each", "require", "convenience", "ancient", "malice", "unweighed", "behavior", "sip",
            "or", "cilicia", "and", "faithful", "tributary", "noblesse", "would", "chivalry",
            "cressida", "livelong", "day", "verona", "brag", "untempere", "effect", "starling",
            "shall", "bad", "verse", "mortise", "what", "scare", "bell", "bertram", "lafeu",
            "antioch", "thaliard", "miller", "by", "brewage", "exit", "unholy", "braggart",
            "pilchard", "are", "prompter", "where", "forbade", "her", "camp", "near", "eldest",
            "son", "familiarly", "sometimes", "augury", "deceive", "pour", "le", "arose",
            "antipholu", "ungrateful", "shape", "grandsire", "grandsire", "needlework", "pewter",
            "fourth", "citizen", "gaunt", "steed", "impound", "as", "chanticleer", "cry",
            "carnarvonshire", "although", "bessy", "to", "throat", "setting", "slightly", "baste",
            "sheepcote", "now", "arms", "against", "waits", "upon", "martext", "truly",
            "drawbridge", "there", "tout", "est", "float", "upon", "tah", "tah", "diedst", "a",
            "hearten", "those", "popp", "d", "carlot", "once", "northerly", "osric", "thumb",
            "wreck", "unnoble", "swerve", "aquilon", "come", "once", "again", "mangle", "myrmidon",
            "eyest", "him", "lapwing", "runs", "bodkin", "biron", "turnbull", "street",
            "affectionate", "servant", "wicked", "hannibal", "emboss", "carbuncle", "handmaid",
            "speak", "pierce", "through", "through", "gloucestershire", "stripe", "begone",
            "canterbury", "denny", "wist", "look", "beg", "enfranchise", "goddesse", "seeing",
            "engineer", "hoist", "crossing", "give", "hallow", "d", "esteemest", "thou", "undraw",
            "the", "outgoe", "the", "colour", "beard", "twould", "braid", "theirs", "theirs",
            "remains", "unpaid", "friendly", "communication", "intermix", "d", "repulse",
            "whatever", "capital", "crime", "welshman", "taken", "reflection", "shipwreck",
            "salary", "not", "lennox", "ross", "degree", "priority", "honeycomb", "each",
            "constringe", "in", "unwise", "patrician", "brink", "martiu", "wall", "joan", "aeacide",
            "was", "fluellen", "fluellen", "governor", "gun", "bepaint", "my", "bartholomew",
            "boar", "withdrew", "me", "dibble", "in", "errant", "malmsey", "encircle", "him",
            "fervor", "sanctify", "pothecary", "and", "ink", "writing", "2", "then", "wither",
            "pear", "extravagant", "spirit", "button", "thank", "las", "what", "likeness",
            "burgundy", "affiance", "seem", "evans", "fery", "cassado", "to", "apology", "benvolio",
            "seigeurie", "indigne", "mile", "asunder", "imitari", "is", "scribble", "form",
            "unfortify", "a", "sanguine", "star", "coelestibu", "irae", "fives", "stark",
            "unprovided", "stanley", "dorca", "mopsa", "opposition", "bloody", "dido", "gonzalo",
            "dispossess", "her", "uneven", "ways", "foggy", "raw", "cain", "coloured",
            "displeasure", "tripp", "spend", "luciliu", "jaunt", "up", "spoil", "whilst", "sprag",
            "memory", "culpable", "queen", "item", "anchovy", "hop", "forty", "twere", "pity",
            "erga", "te", "dodge", "and", "proscription", "cicero", "mought", "not", "sailmaker",
            "in", "worth", "forty", "briareu", "many", "hearten", "those", "warily", "fall",
            "contraction", "pluck", "gabriel", "s", "alarbu", "limb", "nonino", "these",
            "controversy", "bleeding", "collected", "choice", "wherein", "crafty", "charneco",
            "third", "trenchant", "sword", "jog", "while", "anthropophagi", "and", "unpregnant",
            "of", "disappointed", "unanel", "carve", "bone", "reclaim", "d", "extremities", "speak",
            "clare", "lucio", "defame", "by", "la", "pucelle", "vincere", "posse", "alphabetical",
            "position", "gelding", "out", "crossest", "me", "aeson", "lorenzo", "udge", "me",
            "pond", "fish", "lone", "woman", "impress", "thersite", "abridge", "from", "pomfret",
            "pomfret", "skilful", "conserve", "erpay", "all", "peascod", "instead", "vincere",
            "posse", "confused", "to", "cabby", "run", "deer", "mazed", "struck", "twelve", "sad",
            "story", "l", "envoy", "prophetic", "greeting", "importune", "personal", "wanteth",
            "wings", "disproportion", "thought", "pribble", "and", "comptible", "even", "gamesome",
            "passing", "continue", "truce", "lodging", "likes", "enforce", "attention",
            "spectacles", "edmund", "ulcerous", "pitiful", "christendom", "shortly", "sole",
            "victress", "spectatorship", "and", "til", "fal", "his", "own", "tuition", "of",
            "furthest", "inch", "manned", "horse", "mightful", "gods", "cheval", "volant",
            "ominous", "ending", "bestow", "equally", "hampton", "to", "item", "anchovy", "hearer",
            "weeping", "distasteful", "looks", "stalls", "flung", "pug", "tooth", "jer", "doctor",
            "shrowd", "the", "sun", "begin", "flier", "mark", "metaphor", "stink", "disgrace",
            "bewail", "remonstrance", "of", "terrace", "king", "foretell", "some", "choir", "fell",
            "captivate", "talbot", "orb", "continent", "rood", "day", "cashier", "worship", "pody",
            "in", "mummy", "maw", "sorrow", "silvia", "offence", "philomel", "dotage", "terms",
            "inwards", "to", "learn", "happiest", "nicanor", "roman", "disme", "hath", "clutch",
            "d", "tabourer", "he", "rush", "candle", "method", "in", "soothe", "when", "zone",
            "make", "faith", "sir", "rancour", "o", "brunt", "of", "progne", "i", "sacrilegious",
            "thief", "slain", "falls", "dullard", "in", "beak", "even", "hebrew", "will", "staff",
            "sixpenny", "wheresome", "er", "bewray", "whose", "rome", "titu", "yielding", "rescue",
            "everyone", "will", "construction", "caiu", "thee", "well", "in", "the", "8d", "item",
            "commixture", "shown", "diversity", "of", "cannibal", "given", "needless", "jealosy",
            "fumiter", "and", "armoury", "luciu", "ensemble", "de", "knead", "clod", "foresay",
            "it", "enticeth", "thee", "pug", "tooth", "receipt", "thereof", "coronet", "collar",
            "thicket", "please", "powers", "above", "grandfather", "roger", "incision", "forget",
            "woof", "to", "zany", "olivia", "stare", "alonso", "calmie", "custure", "jangle", "out",
            "disinherit", "thine", "ballow", "be", "induce", "by", "misthought", "for", "malice",
            "towards", "urswick", "derby", "mare", "together", "regarding", "that", "endurest",
            "betwixt", "heft", "i", "hob", "nob", "stygian", "bank", "premise", "flame", "afflict",
            "merely", "fume", "epicurean", "sphinx", "as", "enchase", "with", "unlucky", "manage",
            "widower", "shortly", "misbeliever", "cut", "vial", "pour", "certainly", "aunchient",
            "cassandra", "raving", "verger", "with", "garbage", "imogen", "conduit", "girl",
            "outrage", "withdraw", "incur", "a", "shouldest", "strike", "loss", "assume", "george",
            "stanley", "vomit", "empty", "untroubled", "soul", "extremity", "pursue", "away",
            "exeunt", "unseen", "unvisited", "beshrew", "me", "oberon", "fairy", "unlike", "each",
            "scratch", "titania", "abstemious", "or", "departure", "nerissa", "clapp", "d",
            "tickle", "commodity", "brittany", "received", "climbeth", "tamora", "hurtle", "from",
            "lee", "d", "opulent", "throne", "forthwith", "dismiss", "imputation", "laid", "debt",
            "wither", "obedience", "upward", "erewhile", "silviu", "certain", "dukedom",
            "undercrest", "your", "miscall", "retire", "league", "inviolable", "alas", "poor",
            "clownish", "fool", "shipman", "s", "polydote", "return", "lightning", "flash",
            "mischievous", "foul", "proteu", "lucetta", "pedler", "s", "bowed", "as", "statist",
            "though", "pold", "i", "porringer", "fell", "enfree", "antenor", "marry", "sir",
            "green", "sickness", "ruff", "on", "fel", "from", "offendress", "against", "epicure",
            "pompey", "abhorr", "dst", "zenelophon", "and", "ale", "rascal", "respect", "tradition",
            "elm", "answer", "elbe", "where", "savour", "nobly" };

    public static void runQueries(Retriever r, String[] set, int numTermsAtATime) {
        int len = set.length, queryNum = 1;
        for (int i = 0; i < len;) {
            String[] query = Arrays.copyOfRange(set, i, i + numTermsAtATime);
            System.out.println("Query #" + queryNum + ": " + Arrays.toString(query));
            // top 10 results - but don't print them since
            // this is a timing experiment anyways
            RawCountEvaluator evaluator = new RawCountEvaluator();
            System.out.println(r.retrieveQuery(query, 10, evaluator));
            i += numTermsAtATime;
            queryNum++;
        }
    }

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
                "Run query-retrieval. 100 sets of 7-term query and 14-term query. Requires 1 arg: the index path on disk.");

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
            runQueries(retriever, SEVEN_TERM_QUERY_SET, 7);

            // query retrieval using 7 terms from the array at a time
            runQueries(retriever, FOURTEEN_TERM_QUERY_SET, 14);

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

            // find average length of a scene
            int numWords = index.getNumWordsInCollection();
            int numDocs = index.getNumDocs();
            System.out.println("Average length of document: "
                    + (double) (numWords) / (double) (numDocs) + " words");

            // find length of all documents in the document store for other statistics
            ArrayList<Integer> docLength = new ArrayList<Integer>();

            for (int i = 0; i < numDocs; i++) {
                int l = index.getNumWordsInDocument(i);
                docLength.add(l);
                System.out.println("Scene " + i + " has " + l + " words");
            }

            // shortest scene
            int shortestScene = 0, shortestSceneLength = Integer.MAX_VALUE, j = 0;
            for (Integer i : docLength) {
                if (i < shortestSceneLength) {
                    shortestScene = j;
                    shortestSceneLength = i;
                }
                j++;
            }
            System.out.println("Shortest Scene is: " + shortestScene + " with "
                    + shortestSceneLength + " words.");

            // longest play
            ArrayList<String> sceneIDArrayList = index.getBackingDocumentIDs();

            // find play-length for all plays
            HashMap<String, Integer> playLength = new HashMap<String, Integer>();
            for (int i = 0; i < numDocs; i++) {
                String[] ids = sceneIDArrayList.get(i).split("#"); // we know playId and sceneId is
                                                                   // split by #
                String playId = ids[0];
                String sceneId = ids[1];
                int c = playLength.get(playId) != null ? playLength.get(playId) : 0;
                playLength.put(playId, c + docLength.get(i));
            }

            System.out.println(playLength);
            String longestPlay = "", shortestPlay = "";
            int longestPlayLength = 0, shortestPlayLength = Integer.MAX_VALUE;
            for (Entry<String, Integer> entry : playLength.entrySet()) {
                if (entry.getValue() > longestPlayLength) {
                    longestPlay = entry.getKey();
                    longestPlayLength = entry.getValue();
                }
                if (entry.getValue() < shortestPlayLength) {
                    shortestPlay = entry.getKey();
                    shortestPlayLength = entry.getValue();
                }
            }

            System.out.println(
                    "Shortest play: " + shortestPlay + " with " + shortestPlayLength + " words.");
            System.out.println(
                    "Longest play: " + longestPlay + " with " + longestPlayLength + " words.");
        }
    }
}
