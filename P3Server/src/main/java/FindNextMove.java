import java.util.ArrayList;

public class FindNextMove {
	//used to store the current winning or tying moves
    ArrayList<Integer> movesList;

    //this function will get the integer of the best position
    //based on our current gameboard layout, that the server could make
    //it is synchronized to lock it to one client/move at a time
    synchronized int getBestMove(String[] currState) {
    	//declare a new AI_MinMax using our current state
        AI_MinMax aiMinMax = new AI_MinMax(currState);
        
        //we will return this value as the move to make
        int move;
        
        //we'll use the function we added to AI_MinMax to get a list of moves
        movesList = aiMinMax.intMoveList();
        //if the list of moves isn't empty
        if(movesList.size() != 0) {
        	//then the move will just be the first move in the list
        	//since all the moves are equally valid
            move = movesList.get(0);
        } else {
        	//in case something went wrong, move will be -1
            move = -1;
        }
        //we'll clear our moveList variable, for the next call to this function
        movesList.clear();
        
        //return the move
        return move;
    }
}
