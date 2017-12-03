import java.awt.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author Dylan
 *
 *  updates highscore list in a text file with the constructor
 *  
 *  allows access to highscore list in same text file through method retrieveHighScores();
 *  
 *  scores:  maps scores to player names
 *  levels:  maps scores to level achieved
 *  in:  reads the file
 *  filename:  name of file
 *  name:  name of player
 *  newScore:  new score achieved by player
 *  newLevel:  level achieved by player
 *  nameField:  allows player to input their name
 *  nameFrame:  contains nameField
 *  
 *  @Params
 *  int score: score of player
 *  int level: level reached during game
 *
 */
public final class HighScore {

    private Map<Integer, String> scores;
    private Map<Integer, Integer> levels;
    private static BufferedReader in;
    private final static String filename = "HighScores.txt";
    private String name;
    private int newScore;
    private int newLevel;
    private JTextField nameField;
    private JFrame nameFrame;
    
    HighScore(int score, int level) {
        newScore = score;
        newLevel = level;
        
        nameField = new JTextField("InputName");
        nameFrame = new JFrame();
        
        nameField.setFont(new Font("Calibri", Font.PLAIN, 20));

        nameFrame.add(nameField, BorderLayout.CENTER);

        scores = new TreeMap<>();
        levels = new TreeMap<>();
        name = "noName";
        
        final JButton saveName = new JButton("Save Name");
        saveName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText();
                saveHighScore();
                nameFrame.dispose();
                nameFrame.setVisible(false);
                retrieveHighScores();
            }});
        
        saveName.setPreferredSize(new Dimension(50, 25));
        nameFrame.add(saveName, BorderLayout.PAGE_END);
        nameFrame.pack();
        nameFrame.setVisible(true);
        nameFrame.setLocationRelativeTo(null);
        
        saveHighScore();
    }

    
    private void saveHighScore() {
        try {
            in = new BufferedReader(new FileReader(filename));
            mapHighScores();
            addHighScore();
            writeScores();
        } catch (IOException io) {
            System.out.println("error while checking document: " + io.getMessage());
        }
    }


    private void mapHighScores() throws IOException {
        String line = "";
        while ((line = in.readLine()) != null) {
            String[] args = line.split(":");
            String name = args[1].trim();
            int score = Integer.valueOf(args[2].trim());
            int level = Integer.valueOf(args[3].trim());
            
            scores.put(score, name);
            levels.put(score, level);
        }
    }


    private void addHighScore() {
        scores.put(newScore, name);
        levels.put(newScore, newLevel);
    }

    private void writeScores() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        int ctr = 0;

        List<Integer> sortedScores = new LinkedList<>(scores.keySet());
        Collections.sort(sortedScores);
        Collections.reverse(sortedScores);

        for (int current: sortedScores) {
            if (ctr > 10) break;
            
            ctr++;
            int points = current;
            String name = scores.get(current);
            int level = levels.get(current);
            out.write(ctr + ": " 
                    + name + ": " 
                    + points + ": " 
                    + level + String.valueOf((char)13));
            if (ctr > 10) {
                break;
            }
        }
        out.flush();
        out.close();

    }

    public static void retrieveHighScores() {
        final JFrame highScoreFrame = new JFrame();


        String text = "";
        try {
            in = new BufferedReader(new FileReader(filename));

            String line = "";
            for (int x = 0; x < 5; x++) {
                line = in.readLine();
                if (line == null) return;
                
                String[] pieces = line.split(":");
                
                text += (pieces[0] + "." + pieces[1]
                        + "\nScore: " + pieces[2]
                        + "| Level: " + pieces[3]
                        + "\n\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                highScoreFrame.setVisible(false);
                highScoreFrame.dispose();
            }
        });
        back.setPreferredSize(new Dimension(100, 100));
        final JComponent textPanel = new JTextArea(
                "HIGH SCORES: \n" + text);

        textPanel.setBackground(Color.BLACK);
        textPanel.setForeground(Color.YELLOW);
        textPanel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 30));
        highScoreFrame.setPreferredSize(new Dimension(500, 700));
        highScoreFrame.add(textPanel, BorderLayout.CENTER);
        ((JTextComponent)textPanel).setEditable(false);
        back.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 40));
        
        highScoreFrame.add(back, BorderLayout.PAGE_END);
        highScoreFrame.pack();
        highScoreFrame.setVisible(true);
        highScoreFrame.setLocationRelativeTo(null);
    }
}
