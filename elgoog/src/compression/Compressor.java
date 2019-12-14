package compression;

import java.util.ArrayList;

public abstract class Compressor {

    /*
     * returns byte[] after compressing all the integers in list
     */
    public abstract byte[] encodeIntegerList(ArrayList<Integer> list);

    /*
     * returns ArrayList of integers after de-compressing them from the byte[]
     */
    public abstract ArrayList<Integer> decodeIntegerList(byte[] data);
}
