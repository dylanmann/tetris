import java.awt.Color;
import java.util.LinkedList;


public class SBlock extends Block{

    public SBlock(Grid g) {
        super(g);
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 1, Color.GREEN));
        pieces.add(new Piece(Grid.WIDTH / 2, 0, Color.GREEN));
        pieces.add(new Piece(Grid.WIDTH / 2, 1, Color.GREEN));
        pieces.add(new Piece(Grid.WIDTH / 2 + 1, 0, Color.GREEN));   

        initial = new LinkedList<>();
        for (Piece p: pieces) {
            initial.add(new Piece(p));
        }
    }
}
