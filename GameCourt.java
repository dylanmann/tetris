/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * @author Dylan
 *
 * class to control the logic of the gameplay.  
 * Takes in key commands for controlling the blocks and acts as a hub for gameplay
 * 
 * fields:
 * 
 * 
 * Grid grid:  the Grid Object that stores the saved pieces and checks for collisions and cleared lines
 * Block nextBlock:  the block that is stored in the nextBlockWindow
 * Block block:  the current block being controlled by the player of the game
 * 
 * int initialLevel: level where game started
 * int level: current level of the game
 * int score:  the player's current score
 * int linesCleared:  the number of lines cleared by the player in this game instance
 * 
 * Timer dropTimer:  timer used to advance the block down for each timestep.  Changes with level
 * Timer saveWaitTimer:  timer used to allow movement of a block before it gets saved.
 * 
 * NextBlockWindow blockWindow:  the NextBlockWindow Object that shows the next block in queue
 * JLabels score_label, level_label, lines_label:  show the current state of the game
 * JPanel game_over:  game over display of words after game is over
 * 
 * boolean shadow:  whether a shadowBlock object should be drawn to show path of block
 * boolean playing:  controls whether controls work and timestep advances
 * 
 * int COURT_WIDTH:  width of court in pixels
 * int COURT_HEIGHT:  height of court in pixels
 * 
 * 
 * ----------------------------------------------------------------------
 * methods:  
 * 
 * viod endGame():  ends the game goes back to menu
 * 
 * void reset():  resets the game to its initial state
 * 
 * void advanceBlock():  pulls nextBlock to become current block, generates new nextBlock
 * updates nextBlockWindow
 * 
 * void swapBlock():  allows you to swap the current block for the next block
 * 
 * Block newBlock():  generates a new subtype of Block randomly.
 * 
 * void savePieces():  saves the current block and adds them to the grid
 * 
 * void dropTick():  all the actions that need to happen in a timestep
 * 
 * Block getNextBlock:  allows public access to the nextBlock
 * 
 * void lineScore(int):  adds the amount of score corresponding to the number of lines cleared
 * 
 * enum Direction:  LEFT or RIGHT, the direction the block is moving
 * 
 * void move(Direction):  moves the block in the given direction
 * 
 * void garbage(int):  generates garbage in the playing field at the start of a game
 * 
 * void setLevel(int):  sets the internal state of the game level and corresponding changes
 * 
 * void paintComponent(Graphics):  redraws the gameCourt and all its componenets
 * 
 * 
 * 
 * @params JLabels and JPanels that need to be accessed by the gameCourt
 * 
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    private Grid grid;
    private Block nextBlock;
    private Block block;

    private int initialLevel;
    private int level;
    private int score;
    private int linesCleared;

    private Timer dropTimer;
    private Timer saveWaitTimer;

    private NextBlockWindow blockWindow;
    private JLabel score_label;
    private JLabel level_label;
    private JLabel lines_label;

    private JPanel game_over;
    private JPanel buttons;
    private JPanel info_panel;

    private int dropInterval;

    public boolean shadow;
    public boolean playing = false; // whether the game is running

    // Game constants
    public static final int COURT_WIDTH = 10 * Piece.SIZE;
    public static final int COURT_HEIGHT = 20 * Piece.SIZE;

    public GameCourt(JLabel score_widget,
            JLabel level_widget,
            NextBlockWindow nbw,
            JLabel lines_label,
            JPanel buttons,
            JPanel info, 
            JPanel game_over) {

        setBackground(new Color(50,50,50));
        level = 1;
        linesCleared = 0;
        score = 0;

        nbw.court = this;
        grid = new Grid();
        blockWindow = nbw;

        block = newBlock();
        nextBlock = newBlock();

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createEtchedBorder(Color.GREEN, Color.BLUE));

        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
        dropTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropTick();
            }
        });
        dropTimer.start(); // MAKE SURE TO START THE TIMER!

        saveWaitTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (grid.dropCheckCollisions(block)) {
                    savePieces();
                }
                saveWaitTimer.stop();
                dropTimer.start();
                dropTimer.setDelay(dropInterval);
                saveWaitTimer.setDelay(dropInterval / 2);
            }
        });

        setFocusable(true);

        // This key listener allows the square to move as long
        // as an arrow key is pressed, by changing the square's
        // velocity accordingly. (The tick method below actually
        // moves the square.)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

                case KeyEvent.VK_Q:
                    endGame();
                    break;
                case KeyEvent.VK_P:
                    playing = !playing;
                    break;

                case KeyEvent.VK_ESCAPE:
                    System.exit(0);

                case KeyEvent.VK_R:
                    reset();
                }

                if (playing) {

                    switch (e.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                        move(Direction.LEFT);
                        break;

                    case KeyEvent.VK_RIGHT:
                        move(Direction.RIGHT);
                        break;

                    case KeyEvent.VK_DOWN:
                        if (dropTimer.getInitialDelay() < 200) {
                            dropInterval = dropTimer.getInitialDelay() / 5;
                        } else {
                            dropInterval = 100;
                        }
                        dropTimer.setDelay(dropInterval);
                        saveWaitTimer.setDelay(dropInterval / 2);
                        break;

                    case KeyEvent.VK_SPACE:
                        score += block.hardDrop(grid);
                        dropTimer.stop();
                        saveWaitTimer.start();
                        break;
                        
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_X:
                        block.rotate(Block.Direction.CW);
                        break;

                    case KeyEvent.VK_Z:
                        block.rotate(Block.Direction.CCW);
                        break;

                    case KeyEvent.VK_C:
                        swapBlock();
                        break;
                    }

                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    dropInterval = dropTimer.getInitialDelay();
                    dropTimer.setDelay(dropInterval);
                    saveWaitTimer.setDelay(dropInterval / 2);
                    break;
                }
            }
        });
        this.lines_label = lines_label;
        this.score_label = score_widget;
        this.level_label = level_widget;
        this.buttons = buttons;
        this.info_panel = info;
        this.game_over = game_over;

        this.add(game_over);
    }

    private void endGame() {
        buttons.setVisible(true);
        playing = false;
        setVisible(true);
        info_panel.setVisible(false);
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        nextBlock = newBlock();
        advanceBlock();

        grid = new Grid();

        score = 0;
        setLevel(initialLevel);
        linesCleared = 0;

        playing = true;

        game_over.setVisible(false);

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    private void advanceBlock() {
        block = nextBlock;
        if (grid.dropCheckCollisions(block)) {
            playing = false;
            game_over.setVisible(true);
            new HighScore(score, level);
            return;
        }
        nextBlock = newBlock();
        blockWindow.updateNextBlock();
        repaint();
    }

    private void swapBlock() {
        Block old = new Block(block);
        Block next = new Block(nextBlock);

        next.setPosition(block.getOrigin());

        if (grid.checkCollisions(next)) {
            return;
        }

        old.resetPosition();
        block = next;
        nextBlock = old;
        blockWindow.updateNextBlock();
    }

    private Block newBlock() {
        int choice = (int)Math.ceil(Math.random() * 7);

        switch (choice) {
        case 1:
            return new IBlock(grid);
        case 2:
            return new JBlock(grid);
        case 3:
            return new LBlock(grid);
        case 4:
            return new ZBlock(grid);
        case 5:
            return new TBlock(grid);
        case 6:
            return new SBlock(grid);
        case 7:
            return new SquareBlock(grid);
        default: return newBlock();  // shouldnt be possible, recalculate just in case
        }
    }

    private void savePieces() {
        while (grid.checkCollisions(block)) {
            block.undoMove();
        }
        
        for (Piece p: block.getPieces()) {
            grid.add(p);
            if (p.yPos == 1) {
                playing = false;
                game_over.setVisible(true);
                new HighScore(score, level);
                break;
            }
        }

        int currentLines = grid.clearLineCheck(this);
        linesCleared += (currentLines);
        lines_label.setText("Lines Cleared: " + linesCleared);
        lineScore(currentLines);

        if (linesCleared >= (level + 1 - initialLevel)) {
            level ++;
            level_label.setText("Level: " + level);

            dropInterval = dropTimer.getInitialDelay() * 5 / 6;
            dropTimer.setInitialDelay(dropInterval);
            dropTimer.setDelay(dropTimer.getInitialDelay());

            saveWaitTimer.setInitialDelay(dropInterval / 2);
            saveWaitTimer.setDelay(saveWaitTimer.getInitialDelay());
        }

        if (playing) {
            advanceBlock();
        }
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void dropTick() {
        if (playing) {
            if (dropTimer.getDelay() != dropTimer.getInitialDelay()) {
                score++;
            }
            block.drop();
            if (grid.dropCheckCollisions(block)) {
                dropTimer.stop();
                saveWaitTimer.start();
            }
            score_label.setText("Score: " + score);
            score_label.repaint();
            // update the display
            repaint();            
        }
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void lineScore(int linesCleared) {
        int newScore = 0;
        switch(linesCleared) {
        case 1: newScore = 40 * level;
        case 2: newScore = 100 * level;
        case 3: newScore = 300 * level;
        case 4: newScore = 1200 * level;
        }

        score += newScore;
    }

    private enum Direction {
        LEFT,RIGHT
    }

    private void move(Direction d) {
        // move block if user is pressing arrow key
        if (d == Direction.LEFT)
            block.moveLeft();
        else
            block.moveRight();

        if (grid.checkCollisions(block)) {
            block.undoMove();
        }
    }

    public void garbage(int lines) {
        grid.generateGarbage(lines);
    }

    public void setLevel(int l) {
        level = l;
        initialLevel = l;
        level_label.setText("Level: " + l);

        int delay = (int)(1000 * (Math.pow((5.0 / 6.0), (l - 1))));

        dropInterval = delay;
        dropTimer.setInitialDelay(delay);
        dropTimer.setDelay(dropTimer.getInitialDelay());

        saveWaitTimer.setDelay(dropTimer.getDelay() / 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (shadow)
            (new BlockShadow(block, grid)).draw(g);

        grid.draw(g);
        block.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
