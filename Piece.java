import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dylan
 *
 * class of base objects
 * 
 * pieces make up the court and also the blocks that fall.  they are also responsible
 * for drawing essentially the entire dynamic portion of gameplay
 *
 */
public class Piece {
    // graphical size of a piece
    public final static int SIZE = 31;

    private List<int[]> lastPos; // list of former positions of piece
    public int xPos;             // xposition
    public int yPos;             // y position
    public final Color COLOR;    // color of piece

    // constructor given position and color
    public Piece(int xInit, int yInit, Color c) {
        xPos = xInit;
        yPos = yInit;
        lastPos = new LinkedList<>(); 
        lastPos.add(new int[] {xPos, yPos});
        COLOR = c;
    }

    // creates new piece that is identical to given one
    public Piece(Piece p) {
        xPos = p.xPos;
        yPos = p.yPos;
        lastPos = new LinkedList<>(); 
        lastPos.add(new int[] {xPos, yPos});
        COLOR = p.COLOR;
    }

    // records the last position for use in undoMove
    private void recordPos() {
        lastPos.add(new int[] {xPos, yPos});
    }

    // moves block by x and y value
    public void translate(int x, int y) {
        recordPos();
        xPos += x;
        yPos += y;
    }

    // moves block in 4 cardinal directions
    public void moveUp() {
        recordPos();
        yPos--;

    }

    public void moveDown() {
        recordPos();
        yPos++;
    }

    public void moveLeft() {
        recordPos();
        xPos--;

    }

    public void moveRight() {
        recordPos();
        xPos++;
    }

    // undoes last move of piece
    public void undo() {
        if (lastPos.size() == 0)
            return;
        
        int[] pos = lastPos.remove(lastPos.size() - 1);
        xPos = pos[0];
        yPos = pos[1];
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Piece other = (Piece) obj;
        if (xPos != other.xPos)
            return false;
        if (yPos != other.yPos)
            return false;
        return true;
    }

    // draws piece
    public void draw(Graphics g) {
        g.translate(xPos * SIZE, yPos * SIZE);
        
        g.setColor(new Color(180, 180, 180));
        g.fillRect(0, 0, SIZE - 1, SIZE - 1);

        g.setColor(new Color(80, 80, 80));
        g.fillRect(3, 3, SIZE - 4, SIZE - 4);
        
        g.setColor(new Color(30, 30, 30));
        g.drawLine(SIZE, 0, 0, SIZE);

        g.setColor(COLOR);
        g.fillRect(3, 3, SIZE - 7, SIZE - 7);
        
        g.translate(-xPos * SIZE, -yPos * SIZE);
    }
}
