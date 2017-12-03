import java.awt.Color;
import java.util.LinkedList;


public class LBlock extends Block {

    public LBlock(Grid g) {
        super(g);
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 0, Color.CYAN));
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 1, Color.CYAN));
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 2, Color.CYAN));
        pieces.add(new Piece(Grid.WIDTH / 2, 2, Color.CYAN));

        initial = new LinkedList<>();
        for (Piece p: pieces) {
            initial.add(new Piece(p));
        }
    }
}
