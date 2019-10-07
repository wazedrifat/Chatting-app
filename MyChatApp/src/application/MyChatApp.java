package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class MyChatApp extends Application {
	public static String userName;

	@Override
	public void start(Stage primaryStage) {
		try { 
			Pane root1 = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
			Scene scene = new Scene(root1,600,400);
			//scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("MyChatApp");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(null);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(MyChatApp.userName);
		launch(args);
	}
}
