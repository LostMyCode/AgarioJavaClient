package ajc;

import java.awt.Color;
import java.awt.Graphics2D;

public class Cell {
    public double x, y, nx, ny, ox, oy;
    public long id;
    public float s, ns, os;
    private Color color, sColor;
    public String name = "";
    public long updated, born, dead, diedBy;
    public boolean destroyed, jagged, ejected;
    public int nameSize, drawNameSize;

    Cell(long id, int x, int y, float s, String name, RGB rgb, String skin, int flags) {
        this.id = id;
        this.x = this.nx = this.ox = x;
        this.y = this.ny = this.oy = y;
        this.s = this.ns = this.os = s;
        //this.setColor(rgb);
        this.setName(name);
        this.setSkin(skin);
        this.jagged = (flags & 0x01 | flags & 0x10) != 0 ? true : false;
        this.ejected = (flags & 0x20) != 0 ? true : false;
        this.born = Game.syncUpdStamp;
        this.diedBy = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkin(String name) {}

    public void setColor(RGB rgb) {
        this.color = new Color(rgb.r, rgb.g, rgb.b);
        //this.sColor = new Color(Math.round(rgb.r * .9), Math.round(rgb.g * .9), Math.round(rgb.b * .9));
    }

    public void render(Graphics2D ctx, float scale) {
        /* if (Game.player.size() > 0) {
            Color color = new Color(this.r, this.g, this.b);
            
            // if (scale == 1)

            ctx.setColor(color);

            int size = (int) ((this.sizeRender * 2f * scale) * Game.zoom);
            float avgX = 0;
            float avgY = 0;
        } */
    }

    public void update(long relativeTime) {
        float dt = (relativeTime - this.updated);
        dt = Math.max(Math.min(dt, 1), 0);
        int indexOfCell = Game.indexOfCell(this.diedBy);
        //System.out.println(this.diedBy);
        if (this.destroyed && System.currentTimeMillis() > this.dead + 200) {
            //cells.list.remove(this);
        	//Game.log("dead");
        	
        	//if (Game.indexOfCell(this.id) != -1) Game.cells.remove(Game.indexOfCell(this.id));
        	//if (Game.indexOfCell(this.id) != -1) Game.myCells.remove(Game.indexOfCell(this.id));
        } else if (this.diedBy != 0 && indexOfCell != -1) {
            this.nx = Game.cells.get(indexOfCell).x;
            this.ny = Game.cells.get(indexOfCell).y;
        }
        this.x = this.ox + (this.nx - this.ox) * dt;
        this.y = this.oy + (this.ny - this.oy) * dt;
        this.s = this.os + (this.ns - this.os) * dt;
        this.nameSize = (int) Math.floor(Math.max(0.3 * this.ns, 24) / 3 * 4.2);
        this.drawNameSize = (int) Math.floor(Math.max(0.3 * this.s, 24) / 3 * 4.2);
    }
    
    public void remove() {
    	if (Game.indexOfCell(this.id) != -1) Game.cells.remove(Game.indexOfCell(this.id));
    	if (Game.indexOfCell(this.id) != -1) Game.myCells.remove(Game.indexOfCell(this.id));
    }

    public void destroy(long killerId) {
    	//Game.log("dead destroy");
        //if (Game.indexOfCell(this.id) != -1) Game.cells.remove(Game.indexOfCell(this.id));
        //if (Game.indexOfCell(this.id) != -1) Game.myCells.remove(Game.indexOfCell(this.id));
        this.destroyed = true;
        this.dead = Game.syncUpdStamp;

        if (this.diedBy == 0) {
            this.diedBy = killerId;
            this.updated = Game.syncUpdStamp;
        }
    }

    public void destroy() {
        //Game.cells.remove(Game.cells.indexOf(this));
        //Game.myCells.remove(Game.myCells.indexOf(this));
        this.destroyed = true;
        this.dead = Game.syncUpdStamp;
    }
    
    public void draw(Graphics2D ctx) {
    	drawShape(ctx);
    }
    
    public void drawShape(Graphics2D ctx) {
    	//System.out.println(x + " " + y + " " + Game.border.right + " " + Game.border.width);
    	//if (this.ejected) ctx.setColor(Color.blue);
    	if (this.jagged) ctx.setColor(Color.green);
    	ctx.setColor(color);
    	ctx.drawArc((int)x - (int)s, (int)y - (int)s, (int)s * 2, (int)s * 2, 0, 360);
    	ctx.fillArc((int)x - (int)s, (int)y - (int)s, (int)s * 2, (int)s * 2, 0, 360);
    }
}