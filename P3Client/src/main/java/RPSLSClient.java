/*
	The imported libraries are below this comment head.
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.HashMap;

// Creating the class RPSLSClient
public class RPSLSClient extends Application{
	// Declaring all the buttons needed for gameplay
	Button play;
	Button playAgain;
	Button quit;
	HashMap<Integer,Button> boardButtons;

	GridPane board;

	int choice = -1;



	// Creating the scenes,listview,text,textfields,and the object for the client class so the game can run smoothly.
	int portAdds;

	ListView<String> clientView;
	ListView<String> top3Scores;
	ListView<ImageView> imagesPlayed;

	Scene scene1;
	Scene scene2;
	Scene scene3;

	String ipAdd;
	String portAdd;

	Text welcomeMessage;
	Text rulesMessage;

	TextField ipAddress;
	TextField portNumber;

	Client clients;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tic-Tac-Toe");

        welcomeMessage = new Text("Welcome to Tic Tac Toe!");
        welcomeMessage.setFont(Font.font("", FontPosture.ITALIC, 18));
        rulesMessage = new Text("The Server will play first as 'X', you will be 'O'.");

        playAgain = new Button("Play Again");
        quit = new Button("Quit");
        play = new Button("Play");

        // Setting up the textfields for the first scene
        ipAddress = new TextField();
        portNumber = new TextField();
        Label label1 = new Label("IP Address");
        Label label2 = new Label("Port");
        label1.setLabelFor(ipAddress);
        label2.setLabelFor(portNumber);
		label1.setMnemonicParsing(true);
		label2.setMnemonicParsing(true);

		TilePane first = new TilePane();
		first.getChildren().add(label1);
		first.getChildren().add(ipAddress);
		first.getChildren().add(label2);
		first.getChildren().add(portNumber);
		first.getChildren().add(play);
		first.setAlignment(Pos.CENTER);

		// Making the scenes look better
		VBox vbox = new VBox();
		vbox.getChildren().addAll(welcomeMessage, rulesMessage);
		vbox.setAlignment(Pos.CENTER);


		HBox hbox3 = new HBox();
		hbox3.getChildren().addAll(playAgain,quit);
		hbox3.setAlignment(Pos.BOTTOM_CENTER);
		hbox3.setSpacing(15);


		// Declaring the listview needed for the program
		clientView = new ListView<String>();
		top3Scores = new ListView<String>();

		boardButtons = new HashMap<Integer, Button>();
		for(int i = 1; i <= 9; i++) {
			boardButtons.put(i,new Button());
			int size = 60;
			boardButtons.get(i).setMinWidth(size);
			boardButtons.get(i).setMinHeight(size);
		}
		board = new GridPane();

		int count = 1;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				board.add(boardButtons.get(count),j,i);
				count++;
			}
		}
		board.setAlignment(Pos.CENTER);


		VBox main = new VBox(5);
		main.getChildren().addAll(vbox,clientView, hbox3);
		main.setAlignment(Pos.CENTER);
		main.setPadding(new Insets(15, 15, 15, 15));

		BorderPane border = new BorderPane();
		border.setCenter(main);
		Text topScoreText = new Text("Top Scores:\n");
		VBox clientBox = new VBox(topScoreText,top3Scores);
		clientBox.setPadding(new Insets(70,60,20,60));
		border.setRight(clientBox);
		
		board.setPadding(new Insets(20, 0, 0, 0));
		border.setTop(board);

		scene1 = new Scene(first,200,200);

		scene2 = new Scene(border,800,800);


		// Setting up the Actions for when the buttons are pressed
		play.setOnAction(g->{
			ipAdd = ipAddress.getText();
			portAdd = portNumber.getText();
			portAdds = Integer.parseInt(portAdd);
			clients = new Client(ipAdd,portAdds,data -> 
				Platform.runLater(()-> {
					clientView.getItems().add(data.toString());
			}), data2 ->
				Platform.runLater(()-> {
					top3Scores.getItems().clear();
					top3Scores.refresh();
					top3Scores.getItems().add(data2.toString());
				}), data3 ->
					Platform.runLater(() -> {
						boardButtons.get(data3).setText("X");
						boardButtons.get(data3).setDisable(true);
					}), data4 ->
						Platform.runLater(()-> {
							for(int i = 1; i <= 9; i++) {
								boardButtons.get(i).setDisable(true);
							}
						}));
			clients.start();
			primaryStage.setScene(scene2);

		});

		playAgain.setOnAction(e -> {
			choice = -3;
			try {
				for(int i = 1; i <= 9; i++) {
					boardButtons.get(i).setText("");
					boardButtons.get(i).setDisable(false);
				}
				clients.send(choice);
			} catch (Exception playAgain) {

			}
		});

		//this exits the program
		quit.setOnAction(e -> {
			choice = -2;
			try {
				clients.send(choice);
			} catch (Exception quit) {

			}
			Platform.exit();
			System.exit(0);
		});
		
		//these are the player tic tac toe choice button triggers

		boardButtons.get(1).setOnAction(e -> {
			try {
				boardButtons.get(1).setText("O");
				boardButtons.get(1).setDisable(true);
				clients.send(1);
			} catch(Exception a) {

			}
		});

		boardButtons.get(2).setOnAction(e -> {
			try {
				boardButtons.get(2).setText("O");
				boardButtons.get(2).setDisable(true);
				clients.send(2);
			} catch(Exception j) {

			}
		});

		boardButtons.get(3).setOnAction(e -> {
			try {
				boardButtons.get(3).setText("O");
				boardButtons.get(3).setDisable(true);
				clients.send(3);
			} catch(Exception w) {

			}
		});

		boardButtons.get(4).setOnAction(e -> {
			try {
				boardButtons.get(4).setText("O");
				boardButtons.get(4).setDisable(true);
				clients.send(4);
			} catch(Exception w) {

			}
		});

		boardButtons.get(5).setOnAction(e -> {
			try {
				boardButtons.get(5).setText("O");
				boardButtons.get(5).setDisable(true);
				clients.send(5);
			} catch(Exception w) {

			}
		});

		boardButtons.get(6).setOnAction(e -> {
			try {
				boardButtons.get(6).setText("O");
				boardButtons.get(6).setDisable(true);
				clients.send(6);
			} catch(Exception w) {

			}
		});

		boardButtons.get(7).setOnAction(e -> {
			try {
				boardButtons.get(7).setText("O");
				boardButtons.get(7).setDisable(true);
				clients.send(7);
			} catch(Exception w) {

			}
		});

		boardButtons.get(8).setOnAction(e -> {
			try {
				boardButtons.get(8).setText("O");
				boardButtons.get(8).setDisable(true);
				clients.send(8);
			} catch(Exception w) {

			}
		});
		
		boardButtons.get(9).setOnAction(e -> {
			try {
				boardButtons.get(9).setText("O");
				boardButtons.get(9).setDisable(true);
				clients.send(9);
			} catch(Exception w) {

			}
		});
		
				
		//this closes up all the threads when exiting the program
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

		primaryStage.setScene(scene1);
		primaryStage.show();
	}
	
}


