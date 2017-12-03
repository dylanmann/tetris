import java.awt.Color;
import java.util.LinkedList;


public class SquareBlock extends Block {
    public SquareBlock(Grid g) {
        super(g);
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 0, Color.YELLOW));
        pieces.add(new Piece(Grid.WIDTH / 2, 0, Color.YELLOW));
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 1, Color.YELLOW));
        pieces.add(new Piece(Grid.WIDTH / 2, 1, Color.YELLOW));     

        initial = new LinkedList<>();
        for (Piece p: pieces) {
            initial.add(new Piece(p));
        }
    }

    @Override
    public void rotate(Direction d) {};
}
