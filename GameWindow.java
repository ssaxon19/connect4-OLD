import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * This class is for a GUI designed to display a Connect4 game
 * 
 * @author (Sam Saxon)
 * @version (2/11/2018)
 */
public class GameWindow extends JFrame{
    private GamePanel board;
    private JButton[] buttons;
    private JButton resetButton;
    private JPanel mainPanel;
    private JLabel text;
    private JComboBox<String> players;
    private boolean gameMode;
    private JLabel playerText;
    private int difficulty;
    private State curState;
    
    /**
     * Default constructor for objects of class GameWindow.
     */
    public GameWindow() {
        difficulty = 1;
        curState = new State();
        
        board = new GamePanel();
        JPanel buttonPanel = new JPanel();
        mainPanel = new JPanel();
        text = new JLabel("");
        
        setSize(722,712+40);
        setTitle("Connect4");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Integer[] nums = {1,2,3,4,5,6,7,8,9,10};
        JComboBox<Integer> difficulty = new JComboBox<Integer>(nums);
        difficulty.addActionListener(new BoxListener());
        
        String[] options = {"Player vs. Computer", "Player vs. Player"};
        players = new JComboBox<String>(options);
        players.addActionListener(new BoxListener());
        
        buttonPanel.setLayout(new GridLayout(1, 7));
        buttons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            buttons[i] = new JButton(Integer.toString(i+1));
            buttons[i].addActionListener(new ButtonListener());
            buttons[i].setPreferredSize(new Dimension(100,68));
            buttonPanel.add(buttons[i]);
        }
        
        
        mainPanel.setLayout(new GridLayout(2,3,5,5));
        for (int i = 0; i < 2; i++) {
            mainPanel.add(new JPanel());
        }
        playerText = new JLabel("YELLOW's Turn");
        mainPanel.add(playerText);
        mainPanel.add(players);
        
        mainPanel.add(new JLabel("                                        Difficulty: "));
        mainPanel.add(difficulty);
        mainPanel.add(text);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ButtonListener());
        mainPanel.add(resetButton);
        
        add(board, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH);
        
        gameMode = true;
    }
    /**
     * Sets the current State of the game
     * 
     * @param s the State to set as the current State
     */
    public void setState(State s) {
        curState = s;
    }
    /**
     * Gets the current State of the game
     * 
     * @return the current State of the game
     */
    public State getCurState() {
        return curState;
    }
    /**
     * Gets the current difficulty of the game
     * 
     * @return the difficulty of the game
     */
    public int getDifficulty() {
        return difficulty;
    }
    /**
     * Class for the GamePanel which contains the board graphics.
     */
    private class GamePanel extends JPanel{
        
        private final int WIDTH = 714;
        private final int HEIGHT = 614;
        
        /**
         * Default constructor for objects of class GameWindow.
         */
        public GamePanel() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setVisible(true);
        }
        
        /**
         * Paints the current State to the panel
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            char[][] b = curState.getBoard();
            
            for (int i = 0; i <= 7; i++) {
                g.fillRect(102*i, 0, 2, HEIGHT);
                if (i < 7) {
                    g.fillRect(0, 102*i, WIDTH+2, 2);
                }
                
            }
            
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[0].length; j++) {
                    if (b[i][j] == 'X') {
                        g.setColor(Color.YELLOW);
                        g.fillRect(102*j+2, 102*i+2, 100, 100);
                    } else if (b[i][j] == 'O') {
                        g.setColor(Color.RED);
                        g.fillRect(102*j+2, 102*i+2, 100, 100);
                    }
                }
            }
            
            if (curState.hasEnded() && curState.getAvailable().size() > 0) {
                int[] win = curState.getWinningIndex();
                for (int i = 0; i < 4; i++) {
                    g.setColor(Color.BLACK);
                    g.fillRect(10+102*(win[1]+win[3]*i), 10+102*(win[0]+win[2]*i), 84, 84);
                }
                
            }
        }
        
    }
    /**
     * BoxListener class to detect input for dropdown lists.
     */
    private class BoxListener implements ActionListener {
        
        /**
         * Detects input from dropdown lists and either changes the difficulty or game mode
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == players) {
                String selected = (String)players.getSelectedItem();
                    if (selected.charAt(11) == 'C') {
                        gameMode = true;
                        if (curState.getPlayerTurn() == 'O') {
                            Game.playTurn(curState, -1, gameMode);
                            board.paintComponent(board.getGraphics());
                            playerText.setText("YELLOW's Turn");
                        }
                    } else {
                        gameMode = false;
                    }
            } else {
            
                JComboBox<Integer> selected = (JComboBox<Integer>)e.getSource();
                difficulty = (int) selected.getSelectedItem();
                System.out.println("Difficulty is now " + difficulty);
            }
        }
        
    }
    /**
     * ButtonListener class to detect input for reset and numbered buttons.
     */
    private class ButtonListener implements ActionListener {
        /**
         * Detects input from buttons and either resets the game or plays a turn for the player.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == resetButton) {
                curState = new State();
                text.setText("");
                Game.code = "";
            }
            
            for(int i = 0; i < buttons.length; i++) {
                if (e.getSource() == buttons[i]) {
                    //changeDifficulty();
                    Game.playTurn(curState, i, gameMode);
                }
            }
            board.paintComponent(board.getGraphics());
            if (curState.hasEnded()) { text.setText("GAME OVER!");}
            if (curState.getPlayerTurn() == 'X') {
                playerText.setText("YELLOW's Turn");
            } else {
                playerText.setText("RED's Turn");
            }
            if (curState.hasEnded()) {playerText.setText("");}
        }
        
    }
    
    private void changeDifficulty() {
        difficulty = 6 + (7-curState.getAvailable().size());
        System.out.println("Difficulty is " + difficulty);
    }
}
