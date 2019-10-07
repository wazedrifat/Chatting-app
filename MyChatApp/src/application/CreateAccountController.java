package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreateAccountController {
	
	@FXML
	private Button btnSignIn;	

    @FXML
    private Button btnLogin;
	
	@FXML
	private Button btnChoosePhoto;
	
	@FXML
    private ImageView Image;
	
	@FXML
	private TextField txtuserName;
	
    @FXML
    private PasswordField txtuserPassword;
    
    @FXML
    private Label lblMsg;

	private Scanner sc;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	InetAddress localhost;

    @FXML
    void CreateAccount(ActionEvent event) { 
    	if (txtuserName.getText().equals("") || txtuserPassword.getText().equals("")) {
    		lblMsg.setText("Incorrect User Name or Password.");
    		return;
    	}
    	ServerSocket server = null;
    	Socket clientSocket;
    	try {
            Socket s = new Socket("192.168.43.106", 8878); 
            localhost = InetAddress.getLocalHost(); 
            //System.out.println("System IP Address : " + (localhost.getHostAddress()).trim());
            //out = new ObjectOutputStream(s.getOutputStream());
            //out.writeObject(new Client(s,txtuserName.getText(),localhost.getHostAddress().trim(),"TryingtoSignIn...",txtuserPassword.getText(),Image.getImage()));
            s.getOutputStream().write((txtuserName.getText() + " # " + "TryingtoSignIn..." + " # " + txtuserPassword.getText()).getBytes());
            //out = new ObjectOutputStream(s.getOutputStream());
            //out.writeObject(Image.getImage());
            //out.flush();
            //out.close();
            s.close();
            server = new ServerSocket(1234);            
            System.out.println("Try to Connect with Server");
            if((clientSocket = server.accept()) != null){
            	//System.out.println("server accept");
				InputStream is = clientSocket.getInputStream();
				sc = new Scanner(is);
				String line = null;
				if (sc.hasNext()) {
					line = sc.next();
					System.out.println(line);
				}				
				if (line.equals("#Accepted#")){
					Parent root = null;
					root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
					Scene scene = new Scene(root,600,400);
					//scene.getStylesheets().add(getClass().getResource("LogIn.css").toExternalForm());
					Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();			
					primaryStage.setScene(scene);
					primaryStage.setTitle("LogIn");
					System.out.println("Accep");
					primaryStage.setResizable(false);
					primaryStage.show();    
					
                }
				else if (line.equals("#Rejected#")) {
					//This user name is currently using.
					System.out.println("Rejected");
					lblMsg.setText("This user name is currently using.");
					
				}
				else {
					lblMsg.setText("Running");
					
				}							
				//break; 
	    		server.close();
	            clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(CreateAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    	finally {

    	}
    	    	
    }
    
    @FXML
    void ChoosePhoto(ActionEvent event) {
    	System.out.println("ChoosePhoto is calling");
    	FileChooser fc = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("IMAGE files (*.png)", "*.png");fc.getExtensionFilters().add(extFilter);
    	File file = fc.showOpenDialog(null);
    	FileInputStream input;
		try {
			input = new FileInputStream(file);
			Image image = new Image(input);
			Image.setImage(image);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	    	               
    }
    

    @FXML
    void gotoLogin(ActionEvent event) {
    	System.out.println("CreateAccount is calling");
    	Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login");
			//System.out.println("Show");
			//primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
