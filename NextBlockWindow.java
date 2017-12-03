import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class NextBlockWindow extends JPanel {
    private Block next;
    public GameCourt court;
    
    public NextBlockWindow() {
        setBackground(Color.BLACK);
        
        JLabel nbLabel = new JLabel("Next Block");
        nbLabel.setHorizontalAlignment(JLabel.CENTER);
        nbLabel.setForeground(new Color(173, 255, 235));
        nbLabel.setBackground(new Color(140, 0, 0));
        nbLabel.setOpaque(true);
        nbLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        nbLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 40));

        add(nbLabel);
    }

    public void updateNextBlock() {
        next = court.getNextBlock();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int blockWidth = Piece.SIZE * (next.getRight() - next.getLeft()) / 2;
        int blockHeight = Piece.SIZE * next.getBottom() / 2;

        g.translate(-next.getLeft() * Piece.SIZE + this.getWidth() / 2 - blockWidth,
                2 * this.getHeight() / 4 - blockHeight);

        next.draw(g);

        g.translate(-(-next.getLeft() * Piece.SIZE + this.getWidth() / 2 - blockWidth),
                -(2 * this.getHeight() / 4 - blockHeight));
    }
}