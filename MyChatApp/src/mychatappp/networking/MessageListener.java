/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mychatappp.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Client;

/**
 *
 * @author Abrar
 */

public class MessageListener extends Thread {
    
	ServerSocket server;
	Socket clientSocket;
    int port = 1234;
    WritableGUI gui;
    InputStream is ;
    BufferedReader br; 
    Scanner sc;
    String fileName;
    String line;   
    String IP;
    Map<String,Client> Clients = new HashMap<>();
    
    public MessageListener(WritableGUI gui, int port, String IP){
    	this.port = port;
        this.gui = gui;
        this.IP = IP;
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public MessageListener(){
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public void Listen() {
    	try {
            while((clientSocket = server.accept()) != null){
                is = clientSocket.getInputStream();
                //br = new BufferedReader(is);
                sc = new Scanner(is);
                line = sc.nextLine();   
                System.out.println(line);
                if (line.equals("#MsgFromServer#")){
                	MsgFromServer();                	        
                }
                else if (line.equals("#MsgFromClient#")){
                	MsgFromClient();
                }
                else if (line.equals("#FileShare#")){
                	System.out.print("File Receiveing");
                    fileName = sc.nextLine();                    
                    MsgFromClient();      
                	clientSocket.close();
                	System.out.println("File Received");
                	FileShare();
                }
                clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void MsgFromServer() {
    	Clients.clear();
		while(sc.hasNext()){
			String name = sc.next();
			String IP = sc.next();
			Clients.put(name, new Client(name,IP));
			System.out.println(name + " " + IP);	    	    					  	
		}
		/*ObjectInputStream in = new ObjectInputStream(is);
		Clients.clear();
		Client client = null;
		try {
			client = (application.Client) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
			while(client != null){	    		
				client = (application.Client) in.readObject();	
				Clients.put(client.name, client);
			}*/
		gui.writeFriendList(Clients);   	    			     
    }
    
    public void MsgFromClient() {
		line = sc.nextLine();   
		System.out.print("MsgFromClient: " + ": " + line);		
		gui.writeMsg(line);	    				  
    }
    
    public void FileShare() {
		SocketChannel socketchannel = null;
		try {
			ServerSocketChannel serverSocket = ServerSocketChannel.open();
			serverSocket.socket().bind(new InetSocketAddress(9000));
			SocketChannel client = serverSocket.accept();
			socketchannel = client;
			System.out.println("Connection Established .." + client.getRemoteAddress());
			Path path = Paths.get("C:\\Users\\a\\Desktop\\BackUP\\MyChatApp\\File\\" + fileName);	
			FileChannel fileChannel = null;
			fileChannel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE));
			ByteBuffer buffer = ByteBuffer.allocate(1024);		
			while(socketchannel.read(buffer) > 0) {
				buffer.flip();
;				fileChannel.write(buffer);
				buffer.clear();				
				//socketchannel.
				System.out.println(socketchannel.isConnected());
			}			
			fileChannel.close();
			socketchannel.close();
			serverSocket.close();
			System.out.println("Receive File Successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				    		
		finally {
			
		}	
		
        /*try {        	            
            System.out.print("File Receiveing");
            line = sc.next();
            file = new File (line); 
            bytearray = new byte [Integer.parseInt(sc.nextLine())];        
            fos = new FileOutputStream(file);
            is.read(bytearray, 0, bytearray.length);
            fos.write(bytearray, 0, bytearray.length);
            fos.flush();
            is.close();
            fos.close();
    	   	System.out.print("File Received");
            
        } catch (IOException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    

    @Override
    public void run() {
    	Listen();
    }
    
}
