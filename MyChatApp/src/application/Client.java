package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;


public class Client implements Serializable{
	
	Socket clientSocket;
	public String name;
	String IP;
	String state;
	String Password;
	ObjectOutputStream out; 
	Image profilePic;
	Map<String,Client> Clients = new HashMap<>();
	
	public Client(String name,String IP){
		this.clientSocket = null;
		this.name = name;
		this.IP = IP;
		this.state = null;
	}
	
	public Client(Socket clientSocket, String name, String IP, String state, String Password, Image profilePic){
		this.clientSocket = clientSocket;
		this.name = name;
		this.IP = IP;
		this.state = state;
		this.Password = Password;
		this.profilePic = profilePic;
	}
	
	void setFriendList(Map<String,Client> Clients) {
		this.Clients = Clients;
	}
	
	public String toString() {
		return name + " " + IP;		
	}
	
	public void sendList() {
		try {
			Socket s = new Socket("localhost", 1234);
			s.getOutputStream().write("#MsgFromServer#\n".getBytes());	
			//out = new ObjectOutputStream(s.getOutputStream());			
			for (Map.Entry<String,Client> client : Clients.entrySet()) {
				s.getOutputStream().write((client.getKey() + " " + client.getValue().IP + "\n").getBytes());	
				//out.writeObject(client);
	   		}			
			s.close();
	     } catch (IOException ex) {
	    	 Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
	     }
	}
	
	public void sendMsg() {
		
	}
}
