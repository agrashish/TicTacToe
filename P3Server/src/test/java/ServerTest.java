import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

// Creating the class for the Server tests
class ServerTest {
	RPSLSServer server;
	Server servers;
	GameInfo gameInfo;
	AI_MinMax aiMinMax;
	String[] minmax = new String[9];
	Node node;
	MinMax Minmax;
	FindNextMove nextMove;
	int port = 5;
	Consumer<Serializable> call;
	Consumer<Serializable> call2;


	@BeforeEach
	void init() {
		server = new RPSLSServer();
		servers = new Server(port,call,call2);
		gameInfo = new GameInfo();
		for(int i = 0; i < 9; i++) {
		    minmax[i] = "b";
        }
		aiMinMax = new AI_MinMax(minmax);
		node = new Node(minmax,port);
		Minmax = new MinMax(minmax);
		nextMove = new FindNextMove();
	}

	// Testing for the right class
    @Test
    void testRPSLSServer() {
        assertEquals("RPSLSServer",server.getClass().getName(),"The names match.");
    }

    // Testing for the right class
    @Test
    void testServer() {
        assertEquals("Server",servers.getClass().getName(),"The names match.");
    }

    // Testing for the right class
    @Test
    void testGameInfo() {
        assertEquals("GameInfo",gameInfo.getClass().getName(),"The names match.");
    }

    // Testing for the right class
    @Test
    void testAIMinMax() {
	    assertEquals("AI_MinMax",aiMinMax.getClass().getName(),"The names match.");
    }

    // Testing for the right class
    @Test
    void testNode() {
	    assertEquals("Node",node.getClass().getName(),"The names match.");
    }

    // Testing for the right class
    @Test
    void testMinMax() {
	    assertEquals("MinMax",Minmax.getClass().getName(),"The names match.");
    }

    // Testing for the right class
    @Test
    void testFindNextMove() {
	    assertEquals("FindNextMove",nextMove.getClass().getName(),"The names match.");
    }

    // Testing for the right port
    @Test
    void testPort() {
        assertEquals(5,port,"The ports match.");
    }

    // Testing to see if the ports don't match
    @Test
    void testPort2() {
        assertNotEquals(7, port, "The values do not match.");
    }

    // Testing the MinMax Algorithm for Server Win
    @Test
    void testMinMax1() {
        minmax[0] = "X";
        minmax[1] = "X";
        minmax[3] = "O";
        minmax[4] = "O";
        Minmax = new MinMax(minmax);
	    ArrayList<Node> movesList = Minmax.findMoves();
	    boolean temp = false;
	    for(Node mov : movesList) {
	        if(mov.getMinMax() == 10) {
	           temp = true;
            }
        }
        assertTrue(temp);
    }

    // Testing the MinMax Algorithm for Client Win
    @Test
    void testMinMax2() {
        minmax[0] = "X";
        minmax[1] = "X";
        minmax[3] = "O";
        minmax[4] = "O";
        Minmax = new MinMax(minmax);
        ArrayList<Node> movesList = Minmax.findMoves();
        boolean temp = false;
        for(Node mov : movesList) {
            if(mov.getMinMax() == -10) {
                temp = true;
            }
        }
        assertTrue(temp);
    }

    // Testing the MinMax Algorithm for a Draw
    @Test
    void testMinMax3() {
        minmax[0] = "X";
        minmax[1] = "X";
        minmax[3] = "O";
        minmax[4] = "O";
        Minmax = new MinMax(minmax);
        ArrayList<Node> movesList = Minmax.findMoves();
        boolean temp = false;
        for(Node mov : movesList) {
            if(mov.getMinMax() == 0) {
                temp = true;
            }
        }
        assertTrue(temp);
    }

    // Testing the function we added to AI Min Max for Server Win
    @Test
    void testIntMoveList1() {
        minmax[0] = "X";
        minmax[1] = "X";
        minmax[3] = "O";
        minmax[4] = "O";
        aiMinMax = new AI_MinMax(minmax);
        ArrayList<Integer> movesList = aiMinMax.intMoveList();
        boolean temp = false;
        for (int i = 0; i < movesList.size(); i++) {
            if (movesList.get(i) == 3) {
                temp = true;
                break;
            }
        }
        assertTrue(temp);
    }

    // Testing the function we added to AI Min Max for Client Win
    @Test
    void testIntMoveList2() {
        minmax[0] = "X";
        minmax[1] = "X";
        minmax[3] = "O";
        minmax[4] = "O";
        aiMinMax = new AI_MinMax(minmax);
        ArrayList<Integer> movesList = aiMinMax.intMoveList();
        boolean temp = false;
        for (int i = 0; i < movesList.size(); i++) {
            if (movesList.get(i) == 6) {
                temp = true;
                break;
            }
        }
        assertTrue(temp);
    }

    // Testing the function we added to AI Min Max for a Draw
    @Test
    void testIntMoveList3() {
        minmax[0] = "X";
        minmax[1] = "X";
        minmax[2] = "O";
        minmax[3] = "O";
        minmax[4] = "O";
        minmax[5] = "X";
        minmax[7] = "O";
        minmax[8] = "O";
        aiMinMax = new AI_MinMax(minmax);
        ArrayList<Integer> movesList = aiMinMax.intMoveList();
        boolean temp = false;
        for(int i = 0; i < movesList.size(); i++) {
            if(movesList.get(i) == 7) {
                temp = true;
                break;
            }
        }
        assertTrue(temp);
    }
}
