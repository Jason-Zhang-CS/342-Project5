import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerGUI extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	private ListView<String> gameinfo,trafic,direction;
	private HashMap<String,Scene> sceneMap;
	private Button start;
	private TextField portnum;
	private Text port,pnum,client,cnum;
	private Server serverConnection;
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Server");
		sceneMap = new HashMap<String,Scene>();
		sceneMap.put("connection",createStart());
		sceneMap.put("game",gameScene());

		start.setOnAction(e->{
			if(portnum.getText().isEmpty()) {
				direction.getItems().add("Please Enter a Port Number");
			}
			else {
				primaryStage.setScene(sceneMap.get("game"));
				gameinfo.getItems().clear();
				gameinfo.getItems().addAll("GameInfo:","Waiting on Client to Connect");
				Integer val = Integer.parseInt(portnum.getText());
				pnum.setText(Integer.toString(val));
				serverConnection = new Server(data->{Platform.runLater(()->{
					gameinfo.getItems().add(data.toString());
				});},num->{Platform.runLater(()->{
					cnum.setText(num.toString());
				});}, val);
			}
		});
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		primaryStage.setScene(sceneMap.get("connection"));
		primaryStage.show();
	}
	private Scene createStart() {
		HBox pane = new HBox(20);
		Text ptext = new Text("Port Number:");
		portnum = new TextField("5555");
		VBox port1 = new VBox(10,ptext,portnum);
		direction = new ListView<String>();
		start = new Button("Start");
		direction.getItems().addAll("Enter the Port Number","Press Start to Start the Server");
		gameinfo = new ListView<String>();
		VBox choiceBox = new VBox(20,port1,start);
		pane.getChildren().addAll(direction,choiceBox);
		Scene scene = new Scene(pane,600,600);
		return scene;
	}
	private Scene gameScene() {
		gameinfo = new ListView<String>();
		port = new Text("Port Number: ");
		pnum = new Text("");
		client = new Text("Clients Connected: ");
		cnum = new Text("0");
		HBox info = new HBox(10,port,pnum,client,cnum);
		BorderPane gameList = new BorderPane();
		gameList.setTop(info);
		gameList.setCenter(gameinfo);
		Scene scene = new Scene(gameList,600,600);
		return scene;
	}
	
}
