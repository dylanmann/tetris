import java.awt.Color;
import java.util.LinkedList;


public class JBlock extends Block{

    public JBlock(Grid g) {
        super(g);
        Color purple = new Color(140, 0, 255);
        pieces.add(new Piece(Grid.WIDTH / 2, 0, purple));
        pieces.add(new Piece(Grid.WIDTH / 2, 1, purple));
        pieces.add(new Piece(Grid.WIDTH / 2, 2, purple));
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 2, purple));     
        
        initial = new LinkedList<>();
        for (Piece p: pieces) {
            initial.add(new Piece(p));
        }
    }
}

