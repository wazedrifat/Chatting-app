package application;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main {
	static ServerSocket server;	
	static ObjectOutputStream out;
	static ObjectInputStream in;
	
	public static void main(String[] args) {
		
		try {
			server =  new ServerSocket(8878);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Socket clientSocket;
		Map<String,Client> Clients = new HashMap<>();
		String name;
		String IP;
		String state;
		System.out.println("Trying to cacth Client");
	   	try {
	   		while((clientSocket = server.accept()) != null){
	   			System.out.println("One Client Found");
	   			InputStream is = clientSocket.getInputStream();
	   			BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
	   			String line = br.readLine();
	   			String[] info = line.split(" # ");
	   			System.out.println(line);
	   			name = info[0];
	   			//System.out.println(name);
	   			state = info[1];
	   			//System.out.println(state);
	   			IP = clientSocket.getRemoteSocketAddress().toString();	
	   			if (state.equals("TryingtoSignIn...")) {
	   				System.out.println(state);
	   				String Password = info[2];	   	
	   				File file = new File("Client.txt");
	   				FileReader fr = new FileReader(file);
	   				Scanner sc = new Scanner(fr);	   			
	   				boolean find = false;
	   				while(sc.hasNextLine()) {
	   					line = sc.nextLine();
	   					String[] p = line.split(" ");
	   					System.out.println(info[0] + " " + p[0]);
	   					if (info[0].equals(p[0])) {
	   						System.out.println("Rejected");
	   						new Connection(new Client(clientSocket, name, IP, "Rejected")).start();
	   						find = true;
	   						break;
	   					}	   				
	   					//System.out.println(name + " " + state + " " +Password);
	   				}	
	   				fr.close();
	   				if (!find) {
	   					System.out.println("Accepted");
	   					new Connection(new Client(clientSocket, name, IP, "Accepted")).start();	   					
		   				FileWriter fw = new FileWriter(file,true); 
		   				BufferedWriter fbw = new BufferedWriter(fw);
		   				fbw.write(info[0] + " " +info[2] + "\n");
		   				fbw.flush();
		   				fbw.close();
		   				fw.close();
	   				}
	   				//System.out.println("If" + name + " " + state + " " +Password);
	   			}
	   			else if (state.equals("TryingtoLogin...")) {
	   				System.out.println(state + "Login");
	   				File file = new File("Client.txt");
	   				FileReader fr = new FileReader(file);
	   				Scanner sc = new Scanner(fr);	
	   				boolean notMatch = true;
	   				while(sc.hasNextLine()) {
	   					line = sc.nextLine();
	   					String[] p = line.split(" ");
	   					;System.out.print(line);
	   					System.out.println(info[0] + " " + p[0]);
	   					System.out.println(info[2] + " " + p[1]);
	   					if ((info[0].equals(p[0])) && (info[2].equals(p[1]))) {
	   							System.out.println("Login Accepted");
	   							Client c = new Client(clientSocket, name, IP, "Accepted");
	   			   				Clients.put(name,c);
	   			   				c.setFriendList(Clients);
	   			   				new Connection(c).start();
	   			   				notMatch = false;
	   							break;	   						
	   					}	   				
	   					//System.out.println(name + " " + state + " " +Password);
	   				}
	   				fr.close();
	   				if (notMatch) {
	   					System.out.println("Login Rejected");
   						new Connection(new Client(clientSocket, name, IP, "Rejected")).start();
	   				}
	   			}
	   			
	   			else if (state.equals("TryingtoConnect...")) {
	   				Client c = new Client(clientSocket, name, IP, "Connected");
	   				Clients.put(name,c);
	   				c.setFriendList(Clients);
	   				new Connection(c).start();
	   				System.out.println(state);
	   			}
	   			else if (state.equals("Connected")){
	   				Clients.get(name).setFriendList(Clients);
	   			}
	   			else if (state.equals("Disconnected")){
	   				Clients.remove(name);
	   			}
	   			
	   			for (Map.Entry<String,Client> client : Clients.entrySet()) {
	   				//System.out.println(client);
	   			}
	   				   		
	   		}
	   	} catch (IOException ex) {
	   		Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	    }
		//launch(args);
	}

}
