package ajc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class BufferWriter {
    private int offset = 0;
    private int[] ex;
    private boolean le;
    private int[] b = new int[1];

    BufferWriter(boolean littleEndian) {
        this.le = littleEndian;
    }

    void setPacketId(int pId) {
        this.b[0] = pId;
    }

    void setUint8(int a) {
        if (a >= 0 && a < 256) {
            this.extend(1);
            
            this.ex[this.offset] = a;
            this.b = this.ex;
        }
    }

    void setInt32(int a) {
        byte[] bytes = new byte[4];
        if (this.le) ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).putInt((int) a);
        else         ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN   ).putInt((int) a);

        this.extend(4);

        for (byte p : bytes) this.ex[this.offset++] = (int) p;
        this.b = this.ex;
    }

    void setUint32(int a) {
        byte[] bytes = new byte[4];
        if (this.le) ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).putInt((int) a);
        else         ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN   ).putInt((int) a);

        this.extend(4);

        for (byte p : bytes) this.ex[this.offset++] = (int) p;
        this.b = this.ex;
    }

    void setStringUTF8(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

        this.extend(1 + bytes.length);

        for (byte p : bytes) this.ex[this.offset++] = (int) p;
        this.ex[this.offset] = 0;
        this.b = this.ex;
        /* byte[] bytes = new byte[s.toCharArray().length];
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        int o = 0;

        for (char c : s.toCharArray()) buffer.putChar(o, c); */
    }

    void push(int ... args) {
        for (int p : args) this.setUint8(p);
    }

    void extend(int exSize) {
        this.ex = new int[this.b.length + exSize];
        this.offset = 0;
        for (int p : this.b) this.ex[this.offset++] = p;
    }

    ByteBuffer build() {
        ByteBuffer buffer = ByteBuffer.allocate(this.b.length);
        int o = 0;

        for (int p : this.b) buffer.put(o++, (byte) p);
        return buffer;
    }
}