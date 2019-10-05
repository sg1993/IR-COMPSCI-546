package compression;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class EmptyCompressor extends Compressor {

    /*
     * Returns byte[] from list without any compression
     */
    @Override
    public byte[] encodeIntegerList(ArrayList<Integer> list) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * list.size());
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        for (Integer integer : list) {
            intBuffer.put(integer);
        }

        return byteBuffer.array();
    }

    /*
     * Returns list from byte[] without any decompression
     */
    @Override
    public ArrayList<Integer> decodeIntegerList(byte[] data) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        int i = 0, len = data.length;
        while (i < len) {
            result.add(ByteBuffer.wrap(Arrays.copyOfRange(data, i, i + 4)).getInt());
            i += 4;
        }

        return result;
    }

}
