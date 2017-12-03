import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Dylan
 *
 *
 * moveable state of game
 * contains 4 pieces and can be moved in any direction by the player until saved onto the grid
 * 
 * gameGrid: grid of current game
 * pieces: list of current pieces
 * initial: list of initial state of pieces
 */
public class Block {
    
    protected Grid gameGrid;            // Grid object for static pieces
    protected List<Piece> pieces;       // list of pieces in block
    protected List<Piece> initial;      // list of initial position of block
    
    
    public Block(Grid g) {
        pieces = new LinkedList<>();
        gameGrid = g;
        initial = new LinkedList<>();
    }
    
    public Block(Block b) {
        pieces = new LinkedList<>();
        initial = b.initial;
        resetPosition();
        gameGrid = b.gameGrid;      

    }

    // returns the location center of the block
    public int[] getOrigin() {
        return new int[] {pieces.get(1).xPos, pieces.get(1).yPos};
    }

    // resets the position of the block to its initial position
    public void resetPosition() {
        for (Piece p: initial) {
            pieces.add(new Piece(p));
        }
    }
    
    public enum Direction {
        CW, CCW
    }
    
    // rotates a block using a rotation matrix
    protected void rotatePieces(Direction d) {
        int[] origin = getOrigin();
        for (Piece p: pieces) {

            int x = 0;
            int y = 0;

            if (d == Direction.CW) {
                y = (p.xPos - origin[0]) + origin[1];
                x = -(p.yPos - origin[1]) + origin[0];
            } else {
                y = -(p.xPos - origin[0]) + origin[1];
                x = (p.yPos - origin[1]) + origin[0];
            }
            p.xPos = x;
            p.yPos = y;
        }
    }

    // rotates with wall kicks.  moves it in all directions hoping for a success
    public void rotate(Direction d) {
        rotatePieces(d);
        if (gameGrid.checkCollisions(this)) {
            moveLeft();

            if (gameGrid.checkCollisions(this)) {
                undoMove();
                moveRight();

                if (gameGrid.checkCollisions(this)) {
                    undoMove();
                    drop();

                    if (gameGrid.checkCollisions(this)) {
                        undoMove();
                        moveUp();

                        if (gameGrid.checkCollisions(this)) {
                            undoMove();
                        }
                    }
                }
            }
        }
    }

    // moves block in 4 cardinal directions
    protected void moveLeft() {
        for (Piece p: pieces) {
            p.moveLeft();
        }
    }

    protected void moveRight() {
        for (Piece p: pieces) {
            p.moveRight();
        }
    }

    protected void moveUp() {
        for (Piece p: pieces) {
            p.moveUp();
        }
    }

    public void drop() {
        for (Piece p: pieces) {
            p.moveDown();
        }
    }

    // drops block to as far as it can go
    public int hardDrop(Grid g) {
        int score = 0;
        while (!g.dropCheckCollisions(this)) {
            score += 2;
            drop();
        }
        return score;
    }
    
    // sets position of block, used for swap block mostly
    public void setPosition(int[] pos) {        
        if (pos == null || pos.length != 2)
            return;
        
        int x = pos[0] - getOrigin()[0];
        int y = pos[1] - getOrigin()[1];
        
        if (y == -1)
            y = 0;

        for (Piece p: pieces) {
            p.translate(x, y);
        }
    }

    // 3 methods to get the outer dimensions of the block
    public int getBottom() {
        int max = 0;

        for (Piece p: pieces) {
            max = Math.max(max, p.yPos);
        }

        return max;
    }

    public int getRight() {
        int max = 0;

        for (Piece p: pieces) {
            max = Math.max(max, p.xPos);
        }

        return max;
    }

    public int getLeft() {
        int min = GameCourt.COURT_WIDTH;

        for (Piece p: pieces) {
            min = Math.min(min, p.xPos);
        }

        return min;
    }

    // undoes last move of the block
    public void undoMove() {
        for (Piece p: pieces) {
            p.undo();
        }
    }

    // returns the list of pieces contained in the block
    public List<Piece> getPieces() {
        return pieces;
    }

    public void draw(Graphics g) {
        for (Piece p: pieces) {
            p.draw(g);
        }
    }
}
