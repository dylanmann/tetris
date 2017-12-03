/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.LinkedList;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    Clip clip;

    public void run() {
        // NOTE : recall that the 'final' keyword notes inmutability
        // even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Tetris");
        frame.setPreferredSize(new Dimension(600, 850));
        frame.setResizable(false);

        // panel that holds all of the info about the game
        final JPanel game_info_panel = new JPanel();
        game_info_panel.setLayout(new GridLayout(2,1));
        game_info_panel.setPreferredSize(new Dimension(230, 100));
        game_info_panel.setBackground(new Color(0, 14, 163));
        game_info_panel.setBorder(BorderFactory.createRaisedBevelBorder());
        game_info_panel.setVisible(false);

        // subpanel of game_info_panel that holds jlabels for score level and lines
        final JPanel info = new JPanel(new GridLayout(4, 1));
        info.setBackground(new Color(0, 14, 163));

        final JLabel score = new JLabel("Score: 0");
        score.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 30));
        score.setForeground(Color.RED);
        score.setHorizontalAlignment(JLabel.LEFT);

        final JLabel lines = new JLabel("Lines Cleared: 0");
        lines.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 30));
        lines.setForeground(Color.RED);
        lines.setHorizontalAlignment(JLabel.LEFT); 

        final JLabel level = new JLabel("Level: 1");
        level.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 50));
        level.setForeground(Color.RED);
        level.setHorizontalAlignment(JLabel.LEFT);


        // "Game Over words that appear at end of game
        final JPanel gameOver = new JPanel(new GridLayout(2,1));
        JLabel game = new JLabel("GAME");
        JLabel over = new JLabel("OVER!");

        game.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 100));
        game.setForeground(Color.ORANGE);
        game.setOpaque(false);
        over.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 100));
        over.setForeground(Color.ORANGE);
        over.setOpaque(false);

        gameOver.add(game);
        gameOver.add(over);
        gameOver.setOpaque(false);

        // window that shows next block in game
        final NextBlockWindow next_block_window = new NextBlockWindow();

        game_info_panel.add(next_block_window);
        game_info_panel.add(info);
        info.add(level);
        info.add(score);
        info.add(lines);


        // new frame for the instructions to display
        final JFrame iframe = new JFrame("Instructions");

        // words of how to play window
        final JComponent howToPlay = new JTextArea(
                "HOW TO PLAY: \n"
                        + "-Place the Blocks \n"
                        + "-when a line fills up, it clears \n"
                        + "-clear lines to stay alive as long as possible \n\n"
                        + "CONTROLS \n"
                        + "-left/right arrows: move block left or right \n"
                        + "-up arrow: rotate clockwise"
                        + "-down arrow: soft drop piece (speed up) \n"
                        + "-z/x: rotate piece CCW/CW \n"
                        + "-c: swap block with next block \n"
                        + "-Space Bar: hard drop piece (instant drop) \n"
                        + "-p: pause \n"
                        + "-r: restart game \n"
                        + "-q: back to menu \n"
                        + "-esc: close window\n\n");

        final JComponent featuresText = new JTextArea(                    
                "FEATURES:\n"
                        + "|Tetris remix music \n"
                        + "       (source: https://youtu.be/E8FQBjVlERk)\n"
                        + "|Block shadow below current block, \n"
                        + "|window to display next block \n"
                        + "|fully implemented wall kicks \n"
                        + "|garbage mode with initial garbage option\n"
                        + "|initial level selection\n"
                        + "|hard and soft drops \n"
                        + "|full high score system keeping track of level \n"
                        + "|two way rotation\n"
                        + "|slight wait before saving block for adjustments\n"
                        + "|keeps track of lines cleared statistics"
                        );


        howToPlay.setBackground(Color.BLACK);
        howToPlay.setForeground(Color.YELLOW);
        howToPlay.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 25));
        howToPlay.setPreferredSize(new Dimension (500, 450));
        ((JTextComponent)howToPlay).setEditable(false);
        featuresText.setBackground(Color.BLACK);
        featuresText.setForeground(Color.YELLOW);
        featuresText.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 25));
        featuresText.setPreferredSize(new Dimension (500, 450));
        ((JTextComponent)featuresText).setEditable(false);
        

        final JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iframe.setVisible(false);
            }
        });
        back.setPreferredSize(new Dimension(100, 100));


        // font used by all buttons
        Font buttonFont = new Font("Berlin Sans FB Demi", Font.BOLD, 40);
        back.setFont(buttonFont);

        
        iframe.add(featuresText, BorderLayout.LINE_END);
        iframe.add(howToPlay, BorderLayout.LINE_START);
        iframe.add(back, BorderLayout.PAGE_END);

        // home screen of game that has buttons and settings
        final JPanel buttons = new JPanel(new GridLayout(4,1));
        buttons.setPreferredSize(new Dimension(590, 100));

        // sub panel of buttons that holds the settings options
        JPanel settings = new JPanel(new GridLayout(2,1));
        settings.setBackground(new Color(200, 200, 200));
        settings.setBorder((
                BorderFactory.createEtchedBorder(Color.RED, Color.BLUE)));
   
        // words for SETTINGS
        JLabel settings_label = new JLabel("Settings");
        settings_label.setHorizontalAlignment(JLabel.CENTER);
        settings_label.setFont(buttonFont);

        // panel for settings options
        JPanel settingSubPanel = new JPanel(new GridLayout(2, 2));
        settingSubPanel.setOpaque(false);

        // constant values
        Dimension settingDim = new Dimension(100, 40);
        Font settingFont = new Font("Open Sans", Font.BOLD, 22);

        // user checks this box if they want "garbage mode"
        final JCheckBox garbageMode = new JCheckBox("Garbage Mode");
        garbageMode.setPreferredSize(settingDim);
        garbageMode.setFont(settingFont);
        garbageMode.setOpaque(false);

        // user checks this box if they want the shadow tile displayed
        final JCheckBox shadowTile = new JCheckBox("Shadow Tile");
        shadowTile.setPreferredSize(settingDim);
        shadowTile.setFont(settingFont);
        shadowTile.setOpaque(false);

        // user changes this field if they want to start on a specific level
        final JPanel levelPanel = new JPanel(new GridLayout(1,2));
        SpinnerListModel spinnerModel = new SpinnerListModel();
        
        List<Integer> sList = new LinkedList<>();
        for (int x = 1; x <= 15; x++) {
            sList.add(x);
        }
        
        spinnerModel.setList(sList);
        spinnerModel.setValue(1);
        SpinnerListModel sm2 = new SpinnerListModel(sList);
        sm2.setValue(5);
        final JSpinner levelField = new JSpinner(spinnerModel);
        final JLabel levelLabel = new JLabel("Initial Level");

        final JPanel garbagePanel = new JPanel(new GridLayout(1,2));
        final JSpinner garbageField = new JSpinner(sm2);
        final JLabel garbageLabel = new JLabel("Garbage");
        
        levelField.setPreferredSize(new Dimension(40,40));
        levelLabel.setPreferredSize(settingDim);
        garbageField.setPreferredSize(new Dimension(40,40));
        garbageLabel.setPreferredSize(settingDim);        
        levelLabel.setFont(settingFont);
        levelField.setFont(settingFont);
        garbageLabel.setFont(settingFont);
        garbageField.setFont(settingFont);
        levelLabel.setOpaque(false);
        levelPanel.setOpaque(false);
        garbageLabel.setOpaque(false);
        garbagePanel.setOpaque(false);
        
        // adds everything to settings in desired place
        levelPanel.add(levelLabel);
        levelPanel.add((JComponent)levelField);
        garbagePanel.add(garbageLabel);
        garbagePanel.add(garbageField);
        settingSubPanel.add(garbageMode);
        settingSubPanel.add(shadowTile);
        settingSubPanel.add(garbagePanel);
        settingSubPanel.add(levelPanel);
        settings.add(settings_label);
        settings.add(settingSubPanel);


        // Main playing area
        final GameCourt court = new GameCourt(score, level, 
                next_block_window, lines, buttons, game_info_panel, gameOver);
        final JPanel gamePanel = new JPanel();

        gamePanel.add(court);
        gamePanel.setBackground(new Color(130, 200, 130));
        gamePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // starts new game with desired settings
        final JButton newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttons.setVisible(false);
                court.setVisible(true);
                game_info_panel.setVisible(true);
                court.reset();
                court.playing = true;
                court.shadow = shadowTile.isSelected();
                try {
                    levelField.commitEdit();
                    garbageField.commitEdit();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                
                if (garbageMode.isSelected()) 
                    court.garbage(Integer.valueOf((int)garbageField.getValue()));
                court.setLevel(Integer.valueOf((int)levelField.getValue()));
            }
        });

        // opens instructions frame
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iframe.pack();
                iframe.setLocationRelativeTo(null);
                iframe.setVisible(true);
            }
        });

        // opens HighScores frame
        final JButton highScores = new JButton("High Scores");
        highScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HighScore.retrieveHighScores();
            }
        });

        newGame.setFont(buttonFont);
        highScores.setFont(buttonFont);
        instructions.setFont(buttonFont);

        buttons.add(settings);
        buttons.add(newGame);
        buttons.add(highScores);
        buttons.add(instructions);

        // title panel of game
        final JLabel title = new JLabel("");
        title.setIcon(new ImageIcon ("title.jpg"));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(new Color(0, 238, 255));
        title.setBackground(new Color(60, 60, 130));
        title.setOpaque(true);
        title.setFont(new Font("AR DESTINE", Font.BOLD, 150));
        title.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createRaisedBevelBorder()));
        // adds everything needed to frame in correct spot
        frame.add(buttons, BorderLayout.LINE_START);
        frame.add(title, BorderLayout.NORTH);
        frame.add(game_info_panel, BorderLayout.LINE_END);
        frame.add(gamePanel, BorderLayout.CENTER);


        // Put the frame on the screen
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        // Start game
        court.reset();
        court.setVisible(false);
        court.playing = false;

        // start sound
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("music.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }        
    }

    /*
     * Main method run to start and run the game Initializes the GUI elements
     * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
     * this in the final submission of your game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
