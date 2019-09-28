package index;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import encoder.VByteEncoder;
import reader.Document;

public class InvertedFileIndex {

    // list of inverted-lists for every term in the vocab
    private HashMap<String, InvertedList> invListLookup;

    // the inverted index file on disk
    private RandomAccessFile binaryFile = null;

    // the file that contains the lookup table for term and its offset in RAF
    private PrintWriter termToOffsetLookupFile;
    
    // filename where the index will be written
    private String indexFileNameString = null;

    // This is map that will be loaded from the lookup file
    // when you want to reconstruct the index from disk.
    private HashMap<String, Integer> termToOffsetMap;

    public InvertedFileIndex(String filename) {
        invListLookup = new HashMap<String, InvertedList>();
        indexFileNameString = filename;
    }

    public void createIndexFromDocumentStore(ArrayList<Document> docs) {
        for (Document doc : docs) {
            String[] termVector = doc.getTermVector();
            int termPosition = 1;
            InvertedList list = null;
            for (String term : termVector) {
                // check if invertedList for this term exists
                if (invListLookup.containsKey(term)) {
                    // there is a list already in our index for this term
                    list = invListLookup.get(term); // index of the list
                } else {
                    list = new InvertedList(term);
                }
                list.addPositionToPosting(doc.getDocumentUniqueId(), termPosition);
                invListLookup.put(term, list);
                termPosition++;
            }
        }
    }

    private long writeUncompressed(ArrayList<Integer> list) {
        long bytesWritten = 0;
        for (Integer integer : list) {
            try {
                binaryFile.writeInt(integer);
                bytesWritten += 4;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return bytesWritten;
    }

    private long writeCompressed(ArrayList<Integer> list) {
        byte[] toWrite = VByteEncoder.encodeIntegerList(list);
        try {
            binaryFile.write(toWrite);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Wrote " + toWrite.length + " to file");
        return toWrite.length;
    }

    private void writeLookupEntry(String key, long length) {
        termToOffsetLookupFile.println(key + " " + length);
    }

    public void writeSelfToDisk(boolean compress) {
        // open RAF and lookup-table writer
        try {
            binaryFile = new RandomAccessFile(indexFileNameString, "rw");
            termToOffsetLookupFile = new PrintWriter(indexFileNameString + ".ttol"); // the offset files have .ttol extension
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        int totalBytesWritten = 0;
        
        try {
            // move raf to the beginning of the file every time before writing an index
            binaryFile.seek(0);
            binaryFile.setLength(0); // truncate any existing content

            if (compress)
                binaryFile.write('C');
            else
                binaryFile.write('U');

            for (Entry<String, InvertedList> list : invListLookup.entrySet()) {
                if (compress)
                    totalBytesWritten += writeCompressed(list.getValue().getList(compress));
                else
                    totalBytesWritten += writeUncompressed(list.getValue().getList(compress));
                
                // write this term's offset in the index into the lookup table
                writeLookupEntry(list.getKey(), totalBytesWritten);
            }
            System.out.println("Total bytes written to disk: " + totalBytesWritten);
            
            // close all files on disk
            binaryFile.close();
            termToOffsetLookupFile.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printSelf() {
        for (Entry<String, InvertedList> entry : invListLookup.entrySet()) {
            // System.out.println("List");
            entry.getValue().printSelf();
        }
    }

    public void createIndexFromDisk() {
     // open RAF and lookup-table reader
        try {
            binaryFile = new RandomAccessFile(indexFileNameString, "r");
            termToOffsetLookupFile = new PrintWriter(indexFileNameString + ".ttol"); // the offset files have .ttol extension
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
