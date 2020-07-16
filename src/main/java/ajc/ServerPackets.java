package ajc;

import java.nio.ByteBuffer;

import ajc.Game.camera;

public class ServerPackets {
    long syncUpdStamp;
    ByteBuffer data;
    
    ServerPackets() {}

    public void handlePacket(ByteBuffer data) {
    	this.data = data;
        this.syncUpdStamp = System.currentTimeMillis();
        BufferReader reader = new BufferReader(data, 0, true);
        int packetId = reader.getUint8();
        //System.out.println(packetId);
        switch (packetId) {
            case 16:
                this.updateNodes(reader);
                break;
                
            case 32:
            	Game.spawned = true;
            	break;
                
            case 64:
            	this.setBorder(reader);
            	break;
        }
    }

    void updateNodes(BufferReader reader) {
        int count, flags;
        int id, killer, killed;
        int x, y, s;
        boolean updColor, updSkin, updName;
        RGB color;
        String name, skin;
        Cell newCell;

        // consume records
        count = reader.getUint16();
        for (int i = 0; i < count; i++) {
            killer = reader.getUint32();
            killed = reader.getUint32();
            Cell n = null;
            Cell o = null;
            for (Cell cell : Game.cells) {
                if (cell.id == killer) n = cell;
                else if (cell.id == killed) o = cell;
            }
            if (n == null || o == null) continue;
            Game.cells.get(Game.indexOfCell(o.id)).destroy(killer);
        }
        
        // update records
        while (true) {
            id = reader.getUint32();//System.out.println(id);
            if (id == 0) break;

            x = reader.getInt32();
            y = reader.getInt32();
            s = reader.getUint16();

            flags = reader.getUint8();
            updColor = (flags & 2) != 0 ? true : false;
            updSkin = (flags & 4) != 0 ? true : false;
            updName = (flags & 8) != 0 ? true : false;
            color = updColor ? new RGB(reader.getUint8() & 255, reader.getUint8() & 255, reader.getUint8() & 255) : null;
            skin = updSkin ? reader.getStringUTF8() : null;
            name = updName ? reader.getStringUTF8() : null;
            
            boolean isNewCell = true;
            for (Cell cell : Game.cells) {
                if (cell.id == id) {
                    isNewCell = false;
                    cell.update(syncUpdStamp);
                    cell.updated = syncUpdStamp;
                    cell.ox = cell.x;
                    cell.oy = cell.y;
                    cell.os = cell.s;
                    cell.nx = x;
                    cell.ny = y;
                    cell.ns = s;
                    if (color != null) cell.setColor(color);
                    if (name != null) cell.setName(name);
                    if (skin != null) cell.setSkin(skin);
                    break;
                }
            }
            if (isNewCell) {
                newCell = new Cell(id, x, y, s, name, color, skin, flags);
                Game.cells.add(newCell);
            }
        }
        
        // dissappear records
        count = reader.getUint16();
        for (int i = 0; i < count; i++) {
            killed = reader.getUint32();
            for (Cell cell : Game.cells) {
                if (
                    cell.id == killed &&
                    !cell.destroyed
                ) {
                    Game.cells.get(Game.indexOfCell(killed)).destroy();
                    break;
                }
            }
        }
    }
    
    void setBorder(BufferReader reader) {
    	Game.border.left = (int)reader.getFloat64();
    	Game.border.top = (int)reader.getFloat64();
    	Game.border.right = (int)reader.getFloat64();
    	Game.border.bottom = (int)reader.getFloat64();
    	Game.border.width =  Game.border.right - Game.border.left;
    	Game.border.height = Game.border.bottom - Game.border.top;
    	Game.border.centerX = (Game.border.left + Game.border.right) / 2;
    	Game.border.centerY = (Game.border.top + Game.border.bottom) / 2;
    	
    	if (!Game.mapCenterSet) {
    		Game.mapCenterSet = true;
    		Game.camera.x = Game.camera.target.x = Game.border.centerX;
    		Game.camera.y = Game.camera.target.y = Game.border.centerY;
    		Game.camera.scale = camera.target.scale = 1;
    	}
    }
}