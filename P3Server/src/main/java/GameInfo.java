import java.io.Serializable;

// Creating the GameInfo class so communication between the server and client can go smoothly
class GameInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    String topScores = "";
    int opponentPlayed = -1;
    int serverPlayed = -1;
    int whoWon = -1;
    String serverMessage = " ";
    String clientMessage = " ";
}