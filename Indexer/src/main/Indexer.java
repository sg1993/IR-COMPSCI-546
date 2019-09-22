package main;

import reader.JsonReader;

public class Indexer {
    public static void main(String[] args) {
        JsonReader docReader = new JsonReader("C:/Users/georg/motherlode/UMass/cs546/shakespeare-scenes.json");
        docReader.getNextDocumentId();
    }
}
