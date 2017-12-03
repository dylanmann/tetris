import java.awt.Color;
import java.util.LinkedList;



/**
 * @author Dylan
 *
 * class to generate a new block that is a "shadow" of a given block and shows where 
 * the block would go if not rotated or moved left or right.
 */
public class BlockShadow extends Block {
    
    
    public BlockShadow(Block b, Grid grid) {
        super(b);
        pieces = new LinkedList<>();
        for (Piece p: b.pieces) {
            pieces.add(new Piece(p.xPos, p.yPos, new Color(220, 220, 220)));
        }
        
        this.setPosition(b.getOrigin());
        
        while (!grid.checkCollisions(this)) {
            drop();
        }
        undoMove();
    }

    
    public void updateShadow(Block b) {

    }
}
