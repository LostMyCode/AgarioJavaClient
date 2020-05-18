package ajc;

import java.nio.ByteBuffer;

public class ClientPackets {
	public static ByteBuffer spawn(String name) {
        BufferWriter writer = new BufferWriter(true);
        writer.setPacketId(0);
        writer.setStringUTF8(name);
        return writer.build();
        /* ByteBuffer buffer = ByteBuffer.allocate(2 + name.toCharArray().length);
        buffer.put(0, (byte)0);
        
        byte[] bytes = new byte[2 + name.toCharArray().length]; */
    }

    public static ByteBuffer mouseMove(int x, int y) {
        BufferWriter writer = new BufferWriter(true);
        writer.setPacketId(16);
        writer.setUint32(x);
        writer.setUint32(y);
        writer.setUint32(0);
        return writer.build();
    }

    public static ByteBuffer handShake254() {
        BufferWriter writer = new BufferWriter(true);
        writer.setPacketId(254);
        writer.push(6, 0, 0, 0);
        return writer.build();
    }

    public static ByteBuffer handShake255() {
        BufferWriter writer = new BufferWriter(true);
        writer.setPacketId(255);
        writer.push(1, 0, 0, 0);
        return writer.build();
    }
}