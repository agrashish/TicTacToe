import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

// Creating the client class
public class Client extends Thread {

    Socket socketClient;
    private Consumer<Serializable> callback;
    private Consumer<Serializable> callback2;
    private Consumer<Serializable> callback3;
    private Consumer<Serializable> callback4;

    ImageView image2 = new ImageView();
    ImageView image1 = new ImageView();

    String ip;
    int port;

    boolean ready = false;

    //RPSLSClient clients = new RPSLSClient();

    ObjectInputStream in;
    ObjectOutputStream out;

    GameInfo gameInfo = new GameInfo();

    //Creating the constructor for the client class
    public Client(String ip, int port, Consumer<Serializable> call, 
    		Consumer<Serializable> call2,Consumer<Serializable> call3,
    		Consumer<Serializable> call4) {
        callback = call;
        callback2 = call2;
        callback3 = call3;
        callback4 = call4;
        this.ip = ip;
        this.port = port;
    }

    // Creating the run method where the data is read in that is sent from the server
    public void run() {
        try {
            socketClient = new Socket(ip,port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);

            while(true) {

            	//read in server move
                gameInfo = (GameInfo)in.readObject();
                
                //if the server did play something,
                //disable the corresponding client gui button
                if(gameInfo.serverPlayed != -1) {
                    callback3.accept(gameInfo.serverPlayed);
                }
                
                //if top scores isn't blank, update our top scores list
                if(gameInfo.topScores.intern() != "")
                	callback2.accept(gameInfo.topScores);
                
                //if the game is over, disable all buttons until play again
                if(gameInfo.whoWon != -1) {
                	callback4.accept(" ");
                	gameInfo.whoWon = -1;
                }
                
                
                //print to client gui
                //if else statements are to deal with duplicates and blank messages
                if(gameInfo.serverMessage.intern() == gameInfo.clientMessage.intern()) {
                	callback.accept(gameInfo.clientMessage);
                }
                else if(gameInfo.clientMessage.intern() == " ") {
                	callback.accept(gameInfo.serverMessage);
                }
                else {
                	callback.accept(gameInfo.serverMessage + gameInfo.clientMessage);
                }
            }
        } catch(Exception e) {

        }
    }

    // Creating the send method where it sends out the data to the server
    public void send(Integer data) {
        try {
           gameInfo.opponentPlayed = data;
           out.writeObject(gameInfo);
        } catch(IOException f) {

        }
    }
}