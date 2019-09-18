import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

// Creating the class for the Server
public class Server {

    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    RPSLSServer theServer;
    TheServer server;
    private Consumer<Serializable> callback;
    private Consumer<Serializable> callback2;
    int port;
    //initialize an instance of FindNextMove, which we'll use to make the moves needed
    FindNextMove nextMove = new FindNextMove();
    
    //store top scorse and their corresponding clients
    int top1score = 0;
    int top1client = 0;
    int top2score = 0;
    int top2client = 0;
    int top3score = 0;
    int top3client = 0;


    // Creating the constructor for the Server
    Server(int ports, Consumer<Serializable> call, Consumer<Serializable> call2) {

        callback = call;
        callback2 = call2;
        this.port = ports;
        server = new TheServer();
        server.start();
    }

    // Creating the class TheServer which extends the Thread class
    public class TheServer extends Thread {

        public void run() {

            try (ServerSocket mysocket = new ServerSocket(port)) {

                while (true) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("Client has connected to server: " + "Client #" + count);
                    clients.add(c);
                    c.start();
                    count++;
                }
            }//end of try
            catch (Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }

    // Creating the ClientThread class so data can be read in correctly
    class ClientThread extends Thread {

        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
        GameInfo gameInfo = new GameInfo();
        String[] currState;
        int move;
        int clientWins = 0;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.count = count;
            currState = new String[9];
            for(int i = 0; i < 9; i++) {
                currState[i] = "b";
            }
        }

        public void setOutputStream(ObjectOutputStream o2) {
            this.out = o2;
        }

        public ObjectOutputStream getOutputStream() {
            return out;
        }

        public void setInputStream(ObjectInputStream i2) {
            this.in = i2;
        }

        public ObjectInputStream getInputStream() {
            return in;
        }

        // Creating the run method so the comparing of the two clients can take place correctly
        public void run() {

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);

                setOutputStream(out);
                setInputStream(in);

                //Server plays first move
                move = nextMove.getBestMove(currState);
                currState[move-1] = "X";
                gameInfo.serverPlayed = move;
                gameInfo.serverMessage = "Server plays X at position " + move +
                        " against Client " + count + "\n";
                gameInfo.clientMessage = gameInfo.serverMessage;
                
                //Write moves to server and client listviews
                clients.get(count - 1).getOutputStream().writeObject(gameInfo);
                callback.accept(gameInfo.serverMessage);
            } catch (Exception e) {

            }

            while (true) {
                try {
                	
                	//read in client move
                	gameInfo = (GameInfo)in.readObject();
                	
                	//if opponent quit, print that to the server
                	if(gameInfo.opponentPlayed == -2) {
                		gameInfo.serverMessage = "Client " + count + " has quit the game.\n";
                		callback.accept(gameInfo.serverMessage);
                	}
                	//if opponent wants to play gain, reset the board
                	//then, server makes the first move
                	else if(gameInfo.opponentPlayed == -3) {
                		gameInfo.serverMessage = "Client " + count + " wants to play again.";
                	    callback.accept(gameInfo.serverMessage);
                        for(int i = 0; i < 9; i++) {
                            currState[i] = "b";
                        }
                        move = nextMove.getBestMove(currState);
                        currState[move-1] = "X";
                        gameInfo.serverPlayed = move;
                        gameInfo.serverMessage = "Server plays X at position " + move +
                                " against Client " + count + "\n";
                        gameInfo.clientMessage = gameInfo.serverMessage;

                        //Write moves to server and client listviews
                        clients.get(count - 1).getOutputStream().writeObject(gameInfo);
                        callback.accept(gameInfo.serverMessage);
                	}
                	//otherwise, do this
                	else {

	                	//write client move to game state
	                    currState[gameInfo.opponentPlayed - 1] = "O";
	                    
	                    //write server message
	                    gameInfo.serverMessage = "Client " + count + " plays O at position " 
	                    						+ gameInfo.opponentPlayed 
	                    						+ " against Server\n";
	                    //send client move to server listview
	                    callback.accept(gameInfo.serverMessage);
	                    
	                    //figure out the win state
	                    gameInfo.whoWon = checkWin(currState);
	                    //if someone won or it's a tie, do this:
	                    if(gameInfo.whoWon == 0) {
	                        gameInfo.clientMessage = "This game was a draw.\n";
	                        gameInfo.topScores = generateScores().intern();
	                        //print top scores out to server gui
	                        if(gameInfo.topScores.intern() != "")
	                        	callback2.accept(gameInfo.topScores);
	                    } else if(gameInfo.whoWon == 1) {
	                        gameInfo.clientMessage = "This game was won by the Server.\n";
	                        gameInfo.topScores = generateScores().intern();
	                        //print top scores out to server gui
	                        if(gameInfo.topScores.intern() != "")
	                        	callback2.accept(gameInfo.topScores);
	                    } else if(gameInfo.whoWon == 2) {
	                        gameInfo.clientMessage = "This game was won by Client " + count + ".\n";
	                        clientWins++;
	                        gameInfo.topScores = generateScores().intern();
	                        //print top scores out to server gui
	                        if(gameInfo.topScores.intern() != "")
	                        	callback2.accept(gameInfo.topScores);
	                    }
	                    
	                    //if the game is not over yet, server makes its next move
	                    if (gameInfo.whoWon == -1) {
	                    	//server makes its move
	                        move = nextMove.getBestMove(currState);
	                        currState[move - 1] = "X";
	                        gameInfo.serverPlayed = move;
	                        gameInfo.whoWon = checkWin(currState);
	
	                        gameInfo.serverMessage = "Server plays X at position " + move +
	                                " against Client " + count + "\n";
	                        gameInfo.clientMessage = " ";
	                    }
	                    
	                    //if someone won or it's a tie, do this:
	                    if(gameInfo.whoWon == 0) {
	                        gameInfo.clientMessage = "This game was a draw.\n";
	                        gameInfo.topScores = generateScores().intern();
	                        //print top scores out to server gui
	                        if(gameInfo.topScores.intern() != "")
	                        	callback2.accept(gameInfo.topScores);
	                    } else if(gameInfo.whoWon == 1) {
	                        gameInfo.clientMessage = "This game was won by the Server.\n";
	                        gameInfo.topScores = generateScores().intern();
	                        //print top scores out to server gui
	                        if(gameInfo.topScores.intern() != "")
	                        	callback2.accept(gameInfo.topScores);
	                    } else if(gameInfo.whoWon == 2) {
	                        gameInfo.clientMessage = "This game was won by Client " + count + ".\n";
	                        clientWins++;
	                        gameInfo.topScores = generateScores().intern();
	                        //print top scores out to server gui
	                        if(gameInfo.topScores.intern() != "")
	                        	callback2.accept(gameInfo.topScores);
	                    }
	                    
	                    //write gameinfo to client
	                    clients.get(count - 1).getOutputStream().writeObject(gameInfo);
	                    
	                    //print to server gui
	                    //if else statements are to deal with duplicates and blank messages
	                    if(gameInfo.serverMessage.intern() == gameInfo.clientMessage.intern()) {
	                    	callback.accept(gameInfo.serverMessage);
	                    }
	                    else if(gameInfo.clientMessage.intern() == " ") {
	                    	callback.accept(gameInfo.serverMessage);
	                    }
	                    else {
	                    	callback.accept(gameInfo.serverMessage + gameInfo.clientMessage);
	                    }
                	}
                    
                } catch (Exception e) {
                    for(int i = 0; i < clients.size(); i++) {
                        if(clients.get(i).count == count) {
                            clients.remove(i);
                        }
                    }
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    break;
                }
            }
        }//end of run
        
        //a function to see if someone won (or drew) yet
        int checkWin(String[] gameBoard) {
            int win;

            if((gameBoard[0] == "X" && gameBoard[1] == "X" && gameBoard[2] == "X") ||
                    (gameBoard[3] == "X" && gameBoard[4] == "X" && gameBoard[5] == "X") ||
                    (gameBoard[6] == "X" && gameBoard[7] == "X" && gameBoard[8] == "X") ||
                    (gameBoard[0] == "X" && gameBoard[3] == "X" && gameBoard[6] == "X") ||
                    (gameBoard[1] == "X" && gameBoard[4] == "X" && gameBoard[7] == "X") ||
                    (gameBoard[2] == "X" && gameBoard[5] == "X" && gameBoard[8] == "X") ||
                    (gameBoard[0] == "X" && gameBoard[4] == "X" && gameBoard[8] == "X") ||
                    (gameBoard[2] == "X" && gameBoard[4] == "X" && gameBoard[6] == "X")) {
                win = 1;
            } else if ((gameBoard[0] == "O" && gameBoard[1] == "O" && gameBoard[2] == "O") ||
                    (gameBoard[3] == "O" && gameBoard[4] == "O" && gameBoard[5] == "O") ||
                    (gameBoard[6] == "O" && gameBoard[7] == "O" && gameBoard[8] == "O") ||
                    (gameBoard[0] == "O" && gameBoard[3] == "O" && gameBoard[6] == "O") ||
                    (gameBoard[1] == "O" && gameBoard[4] == "O" && gameBoard[7] == "O") ||
                    (gameBoard[2] == "O" && gameBoard[5] == "O" && gameBoard[8] == "O") ||
                    (gameBoard[0] == "O" && gameBoard[4] == "O" && gameBoard[8] == "O") ||
                    (gameBoard[2] == "O" && gameBoard[4] == "O" && gameBoard[6] == "O")) {
                win = 2;
            } else {
                boolean isFull = true;
                for(int i = 0; i < 9; i++) {
                    if(gameBoard[i] == "b") {
                       isFull = false;
                    }
                }
                if(isFull) {
                    win = 0;
                }
                else {
                    win = -1;
                }
            }

            return win;
        }
        
        //function to generate a string representing the top scores
        String generateScores() {
        	String result = "";
        	if(clientWins >= top3score) {
        		if(clientWins >= top2score) {
        			if(clientWins >= top1score) {
        				//update scores
        				top3score = top2score;
        				top2score = top1score;
        				top1score = clientWins;
        				
        				//update their corresponding clients
        				top3client = top2client;
        				top2client = top1client;
        				top1client = count;
        			}
        			else {
        				top3score = top2score;
        				top2score = clientWins;
        				
        				top3client = top2client;
        				top2client = count;
        			}
        		}
        		else {
        			top3score = clientWins;
        			top3client = count;
        		}
        	}
        	
        	
        	//build results string
        	
        	if(top1client == 0) {
        		result = result + "No top scores yet";
        	}
        	else {
        		result = result + "1. Client " + top1client + " with " + top1score + " points.\n";
        	}
        	
        	if(top2client == 0) {
        		result = result + "2. No one yet.\n";
        	}
        	else {
        		result = result + "2. Client " + top2client + " with " + top2score + " points.\n";
        	}
        	
        	if(top3client == 0) {
        		result = result + "3. No one yet.\n";
        	}
        	else {
        		result = result + "3. Client " + top3client + " with " + top3score + " points.\n";
        	}
        	
        	return result;
        }

    }//end of client thread
    
}