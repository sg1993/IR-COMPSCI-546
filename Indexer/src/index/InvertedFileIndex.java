package index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import encoder.VByteEncoder;
import reader.Document;

/* This is the InvertedFile-Index class.
 * This class can be used to:
 * 1) create an in-memory index from a document store and write that to disk
 * 2) create an in-memory index from a file on disk.
 */

public class InvertedFileIndex {

    // the inverted index file on disk
    private RandomAccessFile binaryFile = null;

    // filename where the index will be written or will be read from
    private String indexFileNameString = null;

    // list of inverted-lists for every term in the vocab
    private HashMap<String, InvertedList> invListLookup;

    // This map will be loaded from the lookup file
    // when you want to reconstruct the index from disk.
    // note that the offset here is where the list for a term ENDS, NOT BEGINS
    private LinkedHashMap<String, Integer> termToOffsetMap;

    // Map of term to Document-frequency
    // will be constructed from the index file on disk
    private HashMap<String, Integer> termtoDFMap;

    // Map of term to Collection-frequency
    // will be constructed from the index file on disk
    private HashMap<String, Integer> termtoCFMap;

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

    private void writeLookupEntry(PrintWriter writer, String key, long length, int df, int cf) {
        writer.println(key + " " + length + " " + df + " " + cf);
    }

    public void writeSelfToDisk(boolean compress) {

        // open RAF and lookup-table writer
        try {
            binaryFile = new RandomAccessFile(indexFileNameString, "rw");
            // the offset files have .ttol extension
            PrintWriter termToOffsetLookupFile = new PrintWriter(indexFileNameString + ".ttol");

            int totalBytesWritten = 0;

            // move raf to the beginning of the file every time before writing an index
            binaryFile.seek(0);
            binaryFile.setLength(0); // truncate any existing content

            if (compress)
                binaryFile.write('C');
            else
                binaryFile.write('U');

            for (Entry<String, InvertedList> list : invListLookup.entrySet()) {
                InvertedList temp = list.getValue();
                if (compress)
                    totalBytesWritten += writeCompressed(temp.getList(compress));
                else
                    totalBytesWritten += writeUncompressed(temp.getList(compress));

                // write this term's offset in the index into the lookup table
                writeLookupEntry(termToOffsetLookupFile, list.getKey(), totalBytesWritten,
                        temp.getDocumentFrequency(), temp.getCollectionFrequency());
            }

            // System.out.println("Total bytes written to disk: " + totalBytesWritten);

            // close all files on disk
            binaryFile.close();
            termToOffsetLookupFile.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private InvertedList constructInvertedList(boolean compressed, byte[] buffer, String term) {

        ArrayList<Integer> list = null;
        InvertedList invertedList = new InvertedList(term);

        if (compressed) {
            list = VByteEncoder.decodeIntegerList(buffer);
        } else {
            //
        }

        int index, len = list.size();
        for (index = 0; index < len;) {
            int docId = list.get(index++);
            int tf = list.get(index++);
            for (int i = 0; i < tf; i++) {
                int position = list.get(index++);
                invertedList.addPositionToPosting(docId, position);
            }
        }

        return invertedList;
    }

    public void loadLookupTable() {
        if (termToOffsetMap == null) {
            try {
                BufferedReader termToOffsetLookupFile = new BufferedReader(
                        new FileReader(indexFileNameString + ".ttol"));

                // contruct the term-offset lookup table first
                // first add the entries from the file into a List.
                String line;
                List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
                termtoDFMap = new HashMap<String, Integer>();
                termtoCFMap = new HashMap<String, Integer>();

                while ((line = termToOffsetLookupFile.readLine()) != null) {
                    String[] terms = line.split("\\s+");
                    int offset = Integer.valueOf(terms[1]);
                    int df = Integer.valueOf(terms[1]); // document frequency
                    int cf = Integer.valueOf(terms[2]); // collection-frequency
                    list.add(new SimpleEntry<String, Integer>(terms[0], offset));
                    termtoDFMap.put(terms[0], df);
                    termtoCFMap.put(terms[0], cf);
                }

                // at this point, the List has entries in sorted order of offset-values
                // we now create a LinkedHashMap out of this list.
                // LinkedHashMap is used so that we fix the order
                // of keys unlike HashMap which has arbitrary order
                termToOffsetMap = new LinkedHashMap<String, Integer>();
                for (Entry<String, Integer> entry : list) {
                    termToOffsetMap.put(entry.getKey(), entry.getValue());
                }
            } catch (NumberFormatException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println(termToOffsetMap);
    }

    public void createCompleteIndexFromDisk() {
        // open RAF and lookup-table reader
        try {
            loadLookupTable();
            // at this point, we have map of term to offset/df/cf
            // we can start creating the in-memory index from the the index file
            binaryFile = new RandomAccessFile(indexFileNameString, "r");
            binaryFile.seek(0);
            int previousTermOffset = 0;

            // read the first byte to find out if this is uncompressed or compressed index
            boolean compressed = (binaryFile.readByte() == 'C');

            for (Entry<String, Integer> entry : termToOffsetMap.entrySet()) {
                String term = entry.getKey();
                int endOffset = entry.getValue();

                // read "endOffset - previousTermOffset" bytes
                int bytesToRead = endOffset - previousTermOffset;
                byte[] buffer = new byte[bytesToRead];
                binaryFile.read(buffer, 0, bytesToRead);

                InvertedList l = constructInvertedList(compressed, buffer, term);
                l.printSelf();
                invListLookup.put(term, l);

                // update previous term's offset with the current one
                previousTermOffset = endOffset;
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    
    private HashMap<String, InvertedList> getInvertedLists(){
        return invListLookup;
    }

    public static boolean compareTwoIndexes(InvertedFileIndex index1, InvertedFileIndex index2) {
        // this is just a quick check
        int size1 = index1.getInvertedLists().size();
        int size2 = index2.getInvertedLists().size();
        
        if(size1==size2) {
            System.out.println("Both indexes have same number of inverted lists, i.e. " + size1);
        }
        return false;
    }
}
