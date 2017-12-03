import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Dylan
 *
 *
 * class for storing current pieces in an array and drawing them
 * checks for collisions and also checks for cleared lines
 * 
 * contains a method for generating garbage at the start of a game also
 * 
 * based on a 2D array that hold one block per available slot on game grid
 * 
 * 
 * methods 
 */
public class Grid {
    private Piece[][] gameGrid;

    // dimensions of array (piece slots)
    public static final int WIDTH = GameCourt.COURT_WIDTH / Piece.SIZE;
    public static final int HEIGHT = GameCourt.COURT_HEIGHT / Piece.SIZE;


    public Grid() {
        gameGrid = new Piece[WIDTH][HEIGHT];
    }

    // adds a piece to the grid
    public void add(Piece p) {
        gameGrid[p.xPos][p.yPos] = p;
    }

    // checks if a slot contains a piece.
    public boolean contains(int x, int y) {
        return gameGrid[x][y] != null;
    }

    // checks to see if a line can be cleared.  if it can, it does.
    // returns number of lines cleared
    public int clearLineCheck(GameCourt court) {
        int val = 0;
        
        for (int row = 0; row < HEIGHT; row++) {
            int numPieces = 0;

            for (int col = 0; col < WIDTH; col ++) {
                if (gameGrid[col][row] == null) {
                    break;
                } else {
                    numPieces++;
                }
            }
            if (numPieces == WIDTH) {
                clearLine(row, court);
                val++;
            }
        }
        return val;
    }

    // clears current line and also moves down all pieces above that row
    private void clearLine (int y, GameCourt court) {
        for (int x = 0; x < WIDTH; x++) {
            gameGrid[x][y] = null;            
            
            for (int row = y - 1; row > 0; row--) {
                Piece p = gameGrid[x][row];
                if (p != null) {
                    p.moveDown();
                    gameGrid[x][row] = null;
                    gameGrid[x][row + 1] = p;
                }
            }
        }
    }
    
    
    // returns whether block is colliding with any pieces
    public boolean dropCheckCollisions(Block block) {
        for (Piece p: block.getPieces()) {
            try {
                if (gameGrid[p.xPos][p.yPos + 1] != null) {
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException a) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisions(Block block) {
        for (Piece p: block.getPieces()) {
            try {
                if (gameGrid[p.xPos][p.yPos] != null) {
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException a) {
                return true;
            }
        }
        return false;
    }
    
    // returns a random valid color for garbage creation
    private Color randomColor() {
        int choice = (int)Math.ceil(Math.random() * 7);

        switch (choice) {
        case 1:
            return Color.RED;
        case 2:
            return Color.BLUE;
        case 3:
            return Color.GREEN;
        case 4:
            return Color.YELLOW;
        case 5:
            return Color.ORANGE;
        case 6:
            return new Color(140, 0, 255);
        case 7:
            return Color.CYAN;
        default: return randomColor();  // shouldnt be possible, recalculate just in case
        }

    }

    public void generateGarbage(int lines) {
        for (int y = HEIGHT - 1; y > HEIGHT - lines - 1; y--) {
            add(new Piece(1, y, randomColor()));
            int numPieces = (int)Math.floor(Math.random() * (WIDTH / 2) + 2);
            
            for (int ctr = 0; ctr < numPieces; ctr++) {
                int x = (int)Math.floor(Math.random() * WIDTH);
                if (contains(x, y)) 
                    ctr--;
                else
                    add(new Piece(x, y, randomColor()));
            }
        }
    }
    
    public void clear() {
        gameGrid = new Piece[WIDTH][HEIGHT];
    }
    
    public void draw (Graphics g) {
        g.setColor(java.awt.Color.GRAY);
        for (int x = 0; x < GameCourt.COURT_WIDTH; x += Piece.SIZE) {
            g.drawLine(x, 0, x, GameCourt.COURT_HEIGHT);
        }
        
        for (int y = 0; y < GameCourt.COURT_HEIGHT; y += Piece.SIZE) {
            g.drawLine(0, y, GameCourt.COURT_WIDTH, y);
        }
        
        
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (gameGrid[x][y] != null) {
                    gameGrid[x][y].draw(g);
                }
            }
        }
    }
    
    
}
