package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LogInController {

	    @FXML
	    private BorderPane BP;

	    @FXML
	    private Label txtD;

	    @FXML
	    private Label txtS;
	    
	    @FXML
	    private Label lblMsg;

	    @FXML
	    private TextField txtName;

	    @FXML
	    private PasswordField txtPassword;

	    @FXML
	    private Button btnLogin;

	    @FXML
	    private ImageView DS;

	    @FXML
	    private Button btnCreateAccount;

		private Scanner sc;
		

	    @FXML
	    void ChatApp(ActionEvent event) {
	    	System.out.println("ChatApp");
	    	if (txtName.getText().equals("") || txtPassword.getText().equals("")) {
	    		lblMsg.setText("Incorrect User Name or Password.");
	    		return;
	    	}
	    	ServerSocket server;
	    	Socket clientSocket;
	    	try {
	            Socket s = new Socket("192.168.43.106", 8878);  
	            s.getOutputStream().write((txtName.getText() + " # " + "TryingtoLogin..." + " # " + txtPassword.getText()).getBytes());            
	            s.close();
	            server = new ServerSocket(1234);
	            System.out.println("Try to Connect with Server");
	            if((clientSocket = server.accept()) != null){
	            	System.out.println("server connected");
					InputStream is = clientSocket.getInputStream();
					sc = new Scanner(is);
					String line = null;
					if (sc.hasNext()) {
						line = sc.next();
						System.out.println(line);
					}				
					if (line.equals("#Accepted#")){
						MyChatApp.userName = txtName.getText();
						Parent root = null;
						root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
						Scene scene = new Scene(root,600,400);
						//scene.getStylesheets().add(getClass().getResource("MainScreen.css").toExternalForm());
						Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();			
						primaryStage.setScene(scene);
						primaryStage.setTitle("LogIn");
						System.out.println("Accep");
						primaryStage.setResizable(false);						
						System.out.println(txtName.getText());
						primaryStage.show();    
					}
					
					else if (line.equals("#Rejected#")) {
						//This user name is currently using.
						System.out.println("Rejected");
						lblMsg.setText("Incorrect User Name or Password.");
						System.out.println("Rejected");
						
					}
					else {
						lblMsg.setText("Running");
						
					}							
					//break;       
	            }
	            server.close();
	            clientSocket.close();
	        } catch (IOException ex) {
	            Logger.getLogger(CreateAccountController.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    	finally {
	    		
	    	}
	    
	    }

	    @FXML
	    void ShowMsg(MouseEvent event) {	 
	    	Tooltip.install(DS,new Tooltip("Circle of light"));
	    }

	    @FXML
	    void CreateAccount(ActionEvent event) {
	    	System.out.println("CreateAccount is calling");
	    	Parent root = null;
			try {
				root = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));
				Scene scene = new Scene(root,600,400);
				//scene.getStylesheets().add(getClass().getResource("CreateAccount.css").toExternalForm());
				Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();			
				primaryStage.setScene(scene);
				primaryStage.setTitle("CreateAccount");
				//System.out.println("Show");
				//primaryStage.setResizable(false);
				primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    }

}
