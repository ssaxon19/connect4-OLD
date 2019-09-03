
/**
 * Write a description of class FileWriter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.PrintStream;
import java.util.*;

public class FWriter
{
    private static int diff = 9;
    
    public static void main(String[] args) throws IOException{
        writeMoves("452", 3);
    }
    
    public static void writeMoves(String moveString, int depth) throws IOException{
        State s = State.template(moveString);
        File file = new File("Test.txt");
        file.createNewFile();
        PrintStream fWriter = new PrintStream(new File("after452"));
        
        int d = 0;
        
        ArrayList<State> children = new ArrayList<State>();
        ArrayList<State> newChildren = new ArrayList<State>();
        for (int i = 0; i < 7; i++) {
            children.add(s.place(i));
        }
        
        
        while (d < depth) {
            
            System.out.println("Children about to be looped ovver vvv" + children);
            for (State child : children) {
                int nextMove = child.getBestMove(child.getPlayerTurn(), diff);
                fWriter.print(child.getMoveString() + "," + (nextMove+1) + "\n");
                
                State c = child.place(nextMove);
                for (int i = 0; i < 7; i++) {
                    State n = c.place(i);
                    if (!n.hasEnded()) {
                        newChildren.add(n);
                        n.printBoard();
                    }
                }
            }
            
            children.clear();
            for (int i = 0; i < newChildren.size(); i++) {children.add(new State());}
            
            Collections.copy(children, newChildren);
            newChildren.clear();
            d++;
        }
        
        
        
        System.out.println("Written successfully!");
    }
}
