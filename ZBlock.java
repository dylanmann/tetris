import java.awt.Color;
import java.util.LinkedList;



public class ZBlock extends Block {
    public ZBlock(Grid g) {
        super(g);
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 0, Color.BLUE));
        pieces.add(new Piece(Grid.WIDTH / 2, 1, Color.BLUE));
        pieces.add(new Piece(Grid.WIDTH / 2, 0, Color.BLUE));
        pieces.add(new Piece(Grid.WIDTH / 2 + 1, 1, Color.BLUE));     

        initial = new LinkedList<>();
        for (Piece p: pieces) {
            initial.add(new Piece(p));
        }
    }
}
