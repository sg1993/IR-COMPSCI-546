package main;

import index.InvertedFileIndex;
import reader.SceneReader;

public class Runner {

    public static void main(String[] args) {
        SceneReader sceneReader = new SceneReader("C:/Users/georg/motherlode/UMass/cs546/shakespeare-scenes.json");
        sceneReader.read();
        System.out.println("There are " + sceneReader.getDocumentListSize() + " documents");

        // Begin indexing if the command-line parameter says so
        if (true) {
            // create an index
            InvertedFileIndex index = new InvertedFileIndex();
            index.createIndexFromDocumentStore(sceneReader.getDocuments());
            index.printSelf();
        }
    }
}
