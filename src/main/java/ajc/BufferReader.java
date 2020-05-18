package ajc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class BufferReader {
    private boolean le;
    public static  ByteBuffer view;
    private int offset;
    
    BufferReader(ByteBuffer view, int offset, boolean littleEndian) {
        this.le = littleEndian;
        this.view = view;
        this.offset = offset;
        if (littleEndian) this.view.order(ByteOrder.LITTLE_ENDIAN);
    }

    int getUint8() {
        return this.view.get(this.offset++);
    }

    int getUint16() {
        return this.view.getShort((this.offset += 2) - 2);
    }

    int getInt32() {
        return this.view.getInt((this.offset += 4) - 4);
    }

    int getUint32() {
        //return this.view.getLong((this.offset += 4) - 4);
    	return this.view.getInt((this.offset += 4) - 4);
    }
    
    double getFloat64() {
    	return this.view.getDouble((this.offset += 8) - 8);
    }

    String getStringUTF8() {
        String s = "";
        byte[] b = new byte[1];

        while ((b[0] = this.view.get(this.offset++)) != 0) {
            s += new String(b, StandardCharsets.UTF_8);
        }

        return s;
    }
}