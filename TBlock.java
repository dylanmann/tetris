import java.awt.Color;
import java.util.LinkedList;


public class TBlock extends Block {

    public TBlock(Grid g) {
        super(g);
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 0, Color.ORANGE));
        pieces.add(new Piece(Grid.WIDTH / 2, 0, Color.ORANGE));
        pieces.add(new Piece(Grid.WIDTH / 2 + 1, 0, Color.ORANGE));
        pieces.add(new Piece(Grid.WIDTH / 2, 1, Color.ORANGE));  

        initial = new LinkedList<>();
        for (Piece p: pieces) {
            initial.add(new Piece(p));
        }
    }
}
