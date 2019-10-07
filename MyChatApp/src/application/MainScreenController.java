package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mychatappp.networking.MessageListener;
import mychatappp.networking.MessageTransmitter;
import mychatappp.networking.WritableGUI;

public class MainScreenController implements WritableGUI,Initializable{	
	
	@FXML
    private TextField txtMsg;

    @FXML
    private Button btnSend;

    @FXML
    private TextField IP;

    @FXML
    private TextField Port;
    
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnReceive;

    @FXML
    private TextField ReceiverPort;
    
    @FXML
    private TextArea ChatArea;
    
    @FXML
    private Button btnFile;
    
    @FXML
    private Button btnRefresh;

    @FXML
    private ComboBox<String> cmbFriendList;
    
    @FXML
    private Button btnDisconnect;    
    
    @FXML
    private TextField txtuserName;
    
    @FXML
    private Label lbluserName;
    
    @FXML
    private MenuItem close;
    
    @FXML
    private MenuItem deleteMsg;

    @FXML
    private MenuItem about;
    
    Map<String,Client> Clients;

	private BufferedReader bufferReader;
    
    @FXML
    void gotoLogin(ActionEvent event) {
    	Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene = new Scene(root,600,400);
		scene.getStylesheets().add(getClass().getResource("LogIn.css").toExternalForm());
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();			
		primaryStage.setScene(scene);
		primaryStage.setTitle("LogIn");
		System.out.println("Accept");
		primaryStage.setResizable(false);
		primaryStage.show();    
    }
    		
    @FXML
    void receiveClient(ActionEvent event) {
    	System.out.println("Receive");
    	MessageListener Msglistener = new MessageListener(this, Integer.parseInt(ReceiverPort.getText()),IP.getText());
        Msglistener.start();  
    }

    @FXML
    void sendServer(ActionEvent event) {
    	 System.out.println("Send");
    	 MessageTransmitter transmitter = new MessageTransmitter("SendMsg",txtMsg.getText(), IP.getText(), Integer.parseInt(Port.getText()),lbluserName.getText());
         transmitter.start();
         txtMsg.setText("");
    }
    
    @FXML
    void sendFile(ActionEvent event) {
    	FileChooser fc = new FileChooser();
    	File file = fc.showOpenDialog(null);
    	
    	if (file != null) {
    		MessageTransmitter transmitter = new MessageTransmitter("SendFile",file, IP.getText(), Integer.parseInt(Port.getText()));
            transmitter.start();    		
    	}
    }
    
    @FXML
    void updateFriendList(ActionEvent event) {
        try {
            Socket s = new Socket("192.168.43.106", 8878);                   
            s.getOutputStream().write((lbluserName.getText() + " # " + "TryingtoConnect...").getBytes()); 
            cmbFriendList.setItems(null);
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void deleteMsg(ActionEvent event) {
    	ChatArea.clear();
    }
    
    @FXML
    void showDeveloperInfo(ActionEvent event) {
    	
    	System.out.println("About");
    	String[] process = {"CMD", "/c","D:\\Abrar\\Eclipse\\MyChatApp\\developerInfo.txt"};
    	ProcessBuilder p = new ProcessBuilder (process);
    	p.directory(new File("D:\\"));
    	Process openTxt = null;
		try {
			openTxt = p.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	InputStream inputStream = openTxt.getInputStream();
    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    	bufferReader = new BufferedReader(inputStreamReader);
    }
    
    @FXML
    void disconnectServer(ActionEvent event) {
    	try {
            Socket s = new Socket("192.168.43.106", 8878);                   
            s.getOutputStream().write((lbluserName.getText() + " # " + "Disconnected").getBytes());     
            cmbFriendList.setItems(null);
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void closeApplication(ActionEvent event) {
    	//System.out.println("Platform.exit()");
    	Platform.exit();
    	System.exit(0);
    }

	@Override
	public void writeMsg(String Msg) {
		ChatArea.appendText( Msg + System.lineSeparator());
	}
	
    @FXML
    void selectFriend(ActionEvent event) {
    	IP.clear();
    	IP.setText(Clients.get(cmbFriendList.getValue()).IP);
    }
    
	@Override
	public void  writeFriendList(Map<String,Client> Clients) {
			
		this.Clients = Clients;
		ObservableList<String> FriendList = FXCollections.observableArrayList();
		cmbFriendList.setItems(null);
		FriendList.clear();
		System.out.println(FriendList.size());
		for (Map.Entry<String,Client> client : Clients.entrySet()) {
			System.out.println(client.getKey());
			FriendList.add(client.getKey());
   		}		
		cmbFriendList.setItems(FriendList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lbluserName.setText(MyChatApp.userName);
		Port.setText("1234");
		ReceiverPort.setText("1234");
	}
}
