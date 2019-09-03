import java.util.ArrayList;
/**
 * This class is for a specific State of a Connect4 game
 *  
 * @author (Sam Saxon)
 * @version (2/11/2018)
 */
public class State {
    private char[][] board;
    private char pTurn;
    private int[] winningIndex;
    String mString;
    
    /**
     * Default constructor for objects of class State. 'X' is default player
     */
    public State() {
        board = new char[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = '-';
            }
        }
        pTurn = 'X';
        winningIndex = null;
        mString = "";
    }
    /**
     * Copy constructor for objects of class State. 
     * 
     * @param old the State to be copied
     */
    public State(State old) {
        board = new char[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = old.board[i][j];
            }
        }
        pTurn = old.pTurn;
        winningIndex = null;
        mString = old.mString;
    }
    
    public static State template(String moveString) {
        State s = new State();
        for (int i = 0; i < moveString.length(); i++) {
            s = s.place(Character.getNumericValue(moveString.charAt(i))-1);
        }
        
        return s;
    }
    public String getMoveString() {
        return mString;
    }
    public void addMove(int m) {
        mString += (m+1);
    }
    
    /**
     * gets the current players turn
     * 
     * @return the current player's turn
     */
    public char getPlayerTurn() {
        return pTurn;
    }
    /**
     * gets the State's board
     * 
     * @return the current State's board
     */    
    public char[][] getBoard() {
        return board;
    }
    /**
     * gets the winning index of the board (null if neither player has won)
     * 
     * @return the winningIndex of the game (null if game is not over)
     */    
    public int[] getWinningIndex() {
        return winningIndex;
    }
    /**
     * changes the turn of the current player (O becomes X or X becomes O)
     */
    public void swapTurn() {
        if (pTurn == 'X') {
            pTurn = 'O';
        } else {
            pTurn = 'X';
        }
    }
    /**
     * Prints the game board of this State to the console
     */
    public void printBoard() {
        System.out.println("1 2 3 4 5 6 7");
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Places a piece in a column, swaps the player turn and returns the new State
     *
     * @param  col  the column to place the piece in. must be from [0,6]
     * @return    a copy of the old State with the piece placed in the column
     */
    public State place(int col) {
        State newState = new State(this);
        
        int index = 0;
        while (true) {
            if (board[index][col] != '-') {
                return this;
            }
            if (index < board.length -1 && board[index+1][col] == '-') {
                index++;
            } else {
                newState.board[index][col] = newState.pTurn;
                newState.swapTurn();
                newState.addMove(col);
            
                return newState;
            }
        }
    }

    /**
     * Returns an ArrayList of the valid moves of this state (column is not full)
     *
     * @return    an ArrayList containting indexes of non-empty columns
     */    
    public ArrayList<Integer> getAvailable() {
        ArrayList<Integer> available = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            if (board[0][i] == '-') {
                available.add(i);
            }
        }
        return available;
    }
    
    /**
     * Returns true if the game is over (either player has 4 in a row or the board is full)
     * 
     * @return    true if the game is over, false otherwise
     */
    public boolean hasEnded() {
        //Columns
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] != '-' && board[i][j] == board[i+1][j] && board[i+1][j] == board[i+2][j] && board[i+2][j] == board[i+3][j]) {
                    winningIndex = new int[]{i,j,1,0};
                    return true;
                }
            }
        }
        
        //Rows
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != '-' && board[i][j] == board[i][j+1] && board[i][j+1] == board[i][j+2] && board[i][j+2] == board[i][j+3]) {
                    winningIndex = new int[]{i,j,0,1};
                    return true;
                }
            }
        }
        
        //Diagonal (Down-Right)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != '-' && board[i][j] == board[i+1][j+1] && board[i+1][j+1] == board[i+2][j+2] && board[i+2][j+2] == board[i+3][j+3]) {
                    winningIndex = new int[]{i,j,1,1};
                    return true;
                }
            }
        }
        
        //Diagonal (Down-Left)
        for (int i = 0; i < 3; i++) {
            for (int j = 6; j > 2; j--) {
                if (board[i][j] != '-' && board[i][j] == board[i+1][j-1] && board[i+1][j-1] == board[i+2][j-2] && board[i+2][j-2] == board[i+3][j-3]) {
                    winningIndex = new int[]{i,j,1,-1};
                    return true;
                }
            }
        }
        
        if (getAvailable().size() == 0) {
            return true;
        }
        
        return false;
    }
    /**
     * Calculates the heuristic score of this State. Higher score is better for player X, lower score is better for player O
     *
     * @return    the score of the state
     */    
    public int score() {
        if (hasEnded()) {
            if (getAvailable().size() == 0) {
                return 0;
            } else if (getPlayerTurn() == 'X') {
                return -10000;
            } else {
                return 10000;
            }
        }
        int score = 0;
        
        /*
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == 'X') {
                    score += -1*Math.abs(i-1) + 4 -1*Math.abs(j-3) + 4;
                } else if (board[i][j] == 'O') {
                    score -= -1*Math.abs(i-1) + 4 -1*Math.abs(j-3) + 4;
                }
            }
        }
        */
       
        //Columns (2 in a row and 3 in a row)
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] != '-' && board[i][j] == board[i+1][j]) {
                    if(board[i][j] == 'X') {
                        score += 2;
                    } else {
                        score -= 2;
                    }
                }
            }
        }
        
        //Rows (2 and 3 in a row)
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] != '-' && board[i][j] == board[i][j+1]) {
                    if(board[i][j] == 'X') {
                        score += 2;
                    } else {
                        score -= 2;
                    }   
                }
            }
        }
        
        //Diagonals Down Right (2 and 3 in a row)
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] != '-' && board[i][j] == board[i+1][j+1]) {
                    if(board[i][j] == 'X') {
                        score += 2;
                    } else {
                         score -= 2;
                    }   
                }
            }
        }
        
        //Diagonals Down Left (2 and 3 in a row)
        for (int i = 0; i < 5; i++) {
            for (int j = 6; j > 0; j--) {
                if (board[i][j] != '-' && board[i][j] == board[i+1][j-1]) {
                    if(board[i][j] == 'X') {
                        score += 2;
                    } else {
                         score -= 2;
                    }   
                }
            }
        }
        
        //SCORES FOR ALL THE 3 IN A ROWS
        // in a set of 4 spaces, if 3 are occupied by pieces from 1 player and the other empty, then that player has a valid 3 in a row
        
        //Columns
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                // index 0 of pieces represents number of X pieces, index 1 represents number of O pieces
                int[] pieces = {0,0};
                for (int k = 0; k < 4; k++) {
                    if (board[i+k][j] == 'X') {
                        pieces[0]++;
                    } else if (board[i+k][j] == 'O') {
                        pieces[1]++;
                    }
                }
                if (pieces[0] == 3 && pieces[1] == 0) {
                    score += 12;
                }
                if (pieces[0] == 0 && pieces[1] == 3) {
                    score -= 12;
                }
            }
        }
        //Rows
        for(int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                int[] pieces = {0,0};
                for (int k = 0; k < 4; k++) {
                    if (board[i][j+k] == 'X') {
                        pieces[0]++;
                    } else if (board[i][j+k] == 'O') {
                        pieces[1]++;
                    }
                }
                if (pieces[0] == 3 && pieces[1] == 0) {
                    score += 12;
                }
                if (pieces[0] == 0 && pieces[1] == 3) {
                    score -= 12;
                }
            }
        }
        // Diagonal (Down-Right)
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int[] pieces = {0,0};
                for (int k = 0; k < 4; k++) {
                    if (board[i+k][j+k] == 'X') {
                        pieces[0]++;
                    } else if (board[i+k][j+k] == 'O') {
                        pieces[1]++;
                    }
                }
                if (pieces[0] == 3 && pieces[1] == 0) {
                    score += 12;
                }
                if (pieces[0] == 0 && pieces[1] == 3) {
                    score -= 12;
                }
            }
        } 
        // Diagonal (Down-Right)
        for(int i = 0; i < 3; i++) {
            for (int j = 6; j > 2; j--) {
                int[] pieces = {0,0};
                for (int k = 0; k < 4; k++) {
                    if (board[i+k][j-k] == 'X') {
                        pieces[0]++;
                    } else if (board[i+k][j-k] == 'O') {
                        pieces[1]++;
                    }
                }
                if (pieces[0] == 3 && pieces[1] == 0) {
                    score += 12;
                }
                if (pieces[0] == 0 && pieces[1] == 3) {
                    score -= 12;
                }
            }
        }
        
        return score;
    }
    
    /**
     * Gets the minimax score for a State
     *
     * @param  s the State to get the minimax value of
     * @param  depth the current depth of the recursive funtion (usually starts at 0)
     * @param  maxDepth the maximum depth of the minimax function, equal to the game difficulty
     * @return    the minimax score for the State
     */
    private int minimax(State s, int depth, int maxDepth) {
        if (depth >= maxDepth || s.hasEnded()) {
            return s.score();
        }
        
        int bestScore;
        if (s.getPlayerTurn() == 'X') {
            bestScore = -10000;
        } else {
            bestScore = 10000;
        }
        
        
        ArrayList<Integer> moves = s.getAvailable();
        State[] children = new State[moves.size()];
        
        for (int i = 0; i < children.length; i++) {
            children[i] = s.place(moves.get(i));
        }
        
        for (State child: children) {
            int score = minimax(child, depth+1, maxDepth);
            
            if (s.getPlayerTurn() == 'X') {
                if (score > bestScore) {
                    bestScore = score;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                }
            }
        }
        return bestScore;
    }
    
    public int alphaBeta(State s, int depth, int alpha, int beta, int maxDepth) {
        if (depth >= maxDepth || s.hasEnded()) {
            return s.score();
        }
        
        int bestScore;
        if (s.getPlayerTurn() == 'X') {
            bestScore = -10000;
        } else {
            bestScore = 10000;
        }
        
        ArrayList<Integer> moves = s.getAvailable();
        State[] children = new State[moves.size()];
        
        for (int i = 0; i < children.length; i++) {
            children[i] = s.place(moves.get(i));
        }
        
        
        //explore the center columns first.
        ArrayList<State> orderedChildren = new ArrayList<State>();
        
        int x = moves.size()/2;
        for (int i = 0; i <= moves.size()/2; i++) {
            if (i == 0) {
                orderedChildren.add(children[x]);
            } else {
                if (!(i == moves.size()/2) || moves.size()%2 == 1) {
                    orderedChildren.add(children[x+i]);
                }
                orderedChildren.add(children[x-i]);
            }
        }
        

    
        for (State child: orderedChildren) {
            int score = alphaBeta(child, depth+1, alpha, beta, maxDepth);
            
            if (s.getPlayerTurn() == 'X') {
                if (score > bestScore) {
                    bestScore = score;
                }
                if (alpha < bestScore) {
                    alpha = bestScore;
                }
                
                if (beta <= alpha) {
                    break;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                }
                if (beta > bestScore) {
                    beta = bestScore;
                }
                
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestScore;
    }
    
    // 1 = player1, -1 = player2
    public int negamax(State s, int depth, int alpha, int beta, int player, int maxDepth) {
        if (depth >= maxDepth || s.hasEnded()) {
            return s.score()*player;
        }
        
        int bestScore = -10000;
        ArrayList<Integer> moves = s.getAvailable();
        State[] children = new State[moves.size()];
        
        for (int i = 0; i < children.length; i++) {
            children[i] = s.place(moves.get(i));
        }
        //explore the center columns first.
        ArrayList<State> orderedChildren = new ArrayList<State>();
        
        int x = moves.size()/2;
        for (int i = 0; i <= moves.size()/2; i++) {
            if (i == 0) {
                orderedChildren.add(children[x]);
            } else {
                if (!(i == moves.size()/2) || moves.size()%2 == 1) {
                    orderedChildren.add(children[x+i]);
                }
                orderedChildren.add(children[x-i]);
            }
        }
        
        for (State child : orderedChildren) {
            int score = -1*negamax(child, depth+1, -1*beta, -1*alpha, -1*player, maxDepth);
            if (score > bestScore) {
                bestScore = score;
            }
            if (score > alpha) {
                alpha = score;
            }
            if (alpha >= beta) {
                break;
            }
        }
        
        return bestScore;
    }
    
    /**
     * Uses the minimax function to calculate the best move for a player
     *
     * @param  player  the player to get the best move for. MUST be either 'O' or 'X'
     * @param  difficulty  the difficulty of the game, higher difficulty returns a better move. should never be higher than 8
     * @return    the column number representing the best move for the given player
     */
    public int getBestMove(char player, int difficulty) {
        ArrayList<Integer> moves = getAvailable();
        State[] children = new State[moves.size()];
        int bestMove = moves.get(0), bestScore = 0;
        
        for (int i = 0; i < children.length; i++) {
            children[i] = this.place(moves.get(i));
        }
        
        //list of all the scores of the children
        ArrayList<Integer> scores = new ArrayList<Integer>();
        
        for (int i = 0; i < children.length; i++) {
            //int score = minimax(children[i], 0, difficulty);
            //int score = alphaBeta(children[i], 0, -10000, 10000, difficulty);
            int score = negamax(children[i], 0, -10000, 10000, 1, difficulty);
            scores.add(score);
            System.out.println("Score for column " + (moves.get(i)+1) + " = " + score);
            
            if (player == 'X') {
                if (i == 0) {bestScore = -10000;}
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = moves.get(i);
                }
                
            } else {
                if (i == 0) {bestScore = 10000;}
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = moves.get(i);
                }
            }
        }
        
        System.out.println();
        
        // if there are multiple moves with the same score, this method will return the index of the move closest to column 4
        for (int i = 0; i < moves.size()/2; i++) {
            if (scores.get(moves.size()/2 + i) == bestScore) {
                return moves.get(moves.size()/2 + i);
            }
            if (scores.get(moves.size()/2 - i) == bestScore) {
                return moves.get(moves.size()/2 - i);
            }            
        }
        
        
        return bestMove;
    }
}