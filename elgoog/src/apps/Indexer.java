package apps;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import index.InvertedFileIndex;
import reader.SceneReader;

/*
 * This app can build index from the document-store and write that to disk,
 * and can recreate index from file-on-disk. It can also 
 * run validation-tests on indexes such as:
 * a) in-memory index vs index created from file on disk
 * b) compressed vs uncompressed indexes
 */

public class Indexer {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Need atleast path to  document-store as argument.");
        }

        String docStorePath = args[0];

        boolean createIndex = false, compressIndex = false, indexValidation = false,
                comprValidation = false, completeInMemoryIndex = false;
        String indexInPath = null, indexOutPath = null, indexValidationPath = null;

        // parse the arguments using Apache-CLI
        Options options = new Options();
        options.addOption("i", true, "create index from document-store and write it to disk. "
                + "Requires as argument the path on disk to create the index at.");
        options.addOption("c", false, "compress index before writing to disk.");
        options.addOption("d", true, "create fully in-memory index from file on disk. "
                + "This is mostly for validation purposes - usually indexes are too big too be housed in memory.");
        options.addOption("v", true,
                "validate index created from document store against the same index created from disk. "
                        + "Requires as argument the path on disk to create the index at.");
        options.addOption("t", "validate-compr", true,
                "create 2 indexes from document store, with and without compression and "
                        + "then compare if they are the same. "
                        + "Requires as argument the path on disk to create the index at.");

        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(" ", options);

        CommandLineParser parser = new DefaultParser();
        try {

            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("c")) {
                compressIndex = true;
            }

            if (cmd.hasOption("v")) {
                indexValidation = true;
                indexValidationPath = cmd.getOptionValue("v");
            } else if (cmd.hasOption("t")) {
                comprValidation = true;
                indexValidationPath = cmd.getOptionValue("t");
            } else if (cmd.hasOption("i")) {
                createIndex = true;
                indexOutPath = cmd.getOptionValue("i");
            } else if (cmd.hasOption("d")) {
                completeInMemoryIndex = true;
                indexInPath = cmd.getOptionValue("d");
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SceneReader sceneReader = new SceneReader(docStorePath);
        sceneReader.read();
        System.out.println("There are " + sceneReader.getDocumentListSize() + " documents");

        if (indexValidation) {
            // create an index
            InvertedFileIndex index1 = new InvertedFileIndex(indexValidationPath);
            index1.createIndexFromDocumentStore(sceneReader.getDocuments());
            // index.printSelf();
            index1.writeSelfToDisk(compressIndex);
            // index1.printSelf();

            // construct index from disk from the file written by the previous step
            InvertedFileIndex index2 = new InvertedFileIndex(indexValidationPath);
            index2.createCompleteIndexFromDisk();

            // compare index1 vs index2
            boolean same = InvertedFileIndex.compareTwoInvertedIndexes(index1, index2);
            if (!same) {
                System.out.println("Validation failed!");
            } else {
                System.out.println("Validation success!");
            }

        } else if (comprValidation) {
            // create an index without compression
            InvertedFileIndex index1 = new InvertedFileIndex(indexValidationPath + ".uncompressed");
            index1.createIndexFromDocumentStore(sceneReader.getDocuments());
            index1.writeSelfToDisk(false);

            // create an index with compression
            InvertedFileIndex index2 = new InvertedFileIndex(indexValidationPath + ".compressed");
            index2.createIndexFromDocumentStore(sceneReader.getDocuments());
            index2.writeSelfToDisk(true);

            // compare index1 vs index2 after reading from disk
            InvertedFileIndex index3 = new InvertedFileIndex(indexValidationPath + ".uncompressed");
            index3.createCompleteIndexFromDisk();

            InvertedFileIndex index4 = new InvertedFileIndex(indexValidationPath + ".compressed");
            index4.createCompleteIndexFromDisk();

            boolean same = InvertedFileIndex.compareTwoInvertedIndexes(index3, index4);
            if (!same) {
                System.out.println("Validation failed!");
            } else {
                System.out.println("Validation success!");
            }

        } else if (createIndex) {
            // create an index
            InvertedFileIndex index = new InvertedFileIndex(indexOutPath);
            index.createIndexFromDocumentStore(sceneReader.getDocuments());
            // index.printSelf();
            index.writeSelfToDisk(compressIndex);
            index.writeDocumentVectorsToJSON();

        } else if (completeInMemoryIndex) {
            // construct in-memory index from file on disk
            InvertedFileIndex index = new InvertedFileIndex(indexInPath);
            index.createCompleteIndexFromDisk();

        }
    }
}
