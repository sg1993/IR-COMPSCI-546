package compression;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class VByteEncoder extends Compressor {

    @Override
    public byte[] encodeIntegerList(ArrayList<Integer> list) {
        // Use ByteArrayOutputStream instead of ByteBuffer
        // since ByteBuffer requires early allocation
        // and we don't know the size post-encoding yet
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        for (Integer i : list) {
            while (i >= 128) {
                bos.write((byte) (i & 0x7F));
                i >>>= 7;
            }
            bos.write((byte) (i | 0x80));
        }
        return bos.toByteArray();
    }

    @Override
    public ArrayList<Integer> decodeIntegerList(byte[] data) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int len = data.length;
        for (int i = 0; i < len; i++) {
            int position = 0;
            int decode = (int) (data[i] & 0x7F);

            while ((data[i] & 0x80) == 0) {
                i += 1;
                position += 1;
                int unsignedByte = (int) (data[i] & 0x7F);
                decode |= (unsignedByte << (7 * position));
            }

            result.add(decode);
        }
        return result;
    }
}
