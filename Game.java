/**
 * This class is uses the State and GameWindow class to run games of Connect4
 * 
 * @author (Sam Saxon)
 * @version (2/11/2018)
 */
public class Game {
    private static GameWindow window;  
    public static String code = "";
    /**
     * Main method initiates the GameWindow. Starting point for the program 
     */
    public static void main(String[] args) {
        window = new GameWindow();
        window.setVisible(true);
        //window.setState(State.template("444453335355"));
        /*
        State s = new State();
        while (!s.hasEnded()) {
                s = s.place(s.getBestMove(s.getPlayerTurn(), 12));               
                s.printBoard();                                                 
        }
        */
    }
    
    /**
     * Plays 1 or 2 turns of the game given a State. if computerPlays is false, only player will play. If move is -1, only computer will play
     *
     * @param  s  the current State of the game
     * @param  move  the player's move to play the turn with.
     * @param  computerPlays  whether or not the computer plays this turn (false in PvP mode)
     */
    public static void playTurn(State s, int move, boolean computerPlays) {
        if (!s.hasEnded() && (s.getAvailable().contains(move) || move == -1)) {
            if (move > -1) {
                window.setState(s.place(move));
                code += (move+1);
                s = window.getCurState();
            }
            if (!s.hasEnded() && computerPlays) {
                int computerMove = s.getBestMove(s.getPlayerTurn(), window.getDifficulty());
                window.setState(s.place(computerMove));
                code += (computerMove+1);
            }
            System.out.println(code);
        }
    }
    
    
}
