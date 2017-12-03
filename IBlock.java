import java.awt.Color;
import java.util.LinkedList;


public class IBlock extends Block {

    public IBlock(Grid g) {
        super(g);
        pieces.add(new Piece(Grid.WIDTH / 2 - 2, 0, Color.RED));
        pieces.add(new Piece(Grid.WIDTH / 2 - 1, 0, Color.RED));
        pieces.add(new Piece(Grid.WIDTH / 2, 0, Color.RED));
        pieces.add(new Piece(Grid.WIDTH / 2 + 1, 0, Color.RED));

        longBlock = true;
        initial = new LinkedList<>();
        for (Piece p: pieces) {
            initial.add(new Piece(p));
        }
    }

    private boolean hasntMoved(int xOld, int yOld) {
        return pieces.get(0).xPos == xOld &&
                pieces.get(3).yPos == yOld;
    }

    @Override
    public void rotate(Direction d) {
        int xOld = pieces.get(0).xPos;
        int yOld = pieces.get(3).yPos;
        super.rotate(d);

        if (hasntMoved(xOld, yOld)) {
            moveRight();

            xOld = pieces.get(0).xPos;
            yOld = pieces.get(3).yPos;

            super.rotate(d);

            if (hasntMoved(xOld, yOld)) {

                undoMove();
                moveLeft();

                xOld = pieces.get(0).xPos;
                yOld = pieces.get(3).yPos;

                super.rotate(d);

                if (hasntMoved(xOld, yOld)) {
                    drop();

                    xOld = pieces.get(0).xPos;
                    yOld = pieces.get(3).yPos;

                    super.rotate(d);

                    if (hasntMoved(xOld, yOld)) {
                        moveUp();

                        xOld = pieces.get(0).xPos;
                        yOld = pieces.get(3).yPos;

                        super.rotate(d);
                    }
                }
            }
        }
    }
}
