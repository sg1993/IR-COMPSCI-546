package main;

import reader.SceneReader;

public class Indexer {
    public static void main(String[] args) {
        SceneReader sceneReader = new SceneReader("C:/Users/georg/motherlode/UMass/cs546/shakespeare-scenes.json");
        sceneReader.read();
        System.out.println("There are " + sceneReader.getDocumentListSize() + " documents");
    }
}
