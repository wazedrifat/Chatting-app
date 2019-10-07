/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mychatappp.networking;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Abrar
 */
public class MessageTransmitter extends Thread {

    String message, hostname;
    int port;
    File file;
    String command;
    Socket s;
    String name;
    
    public MessageTransmitter() {
    }

    public MessageTransmitter(String command, String message, String hostname, int port,String name) {
        this.command = command;
    	this.message = message;
        this.hostname = hostname;
        this.port = port;
        this.file = null;
        this.name = name;
    }
    
    public MessageTransmitter(String command,File file, String hostname, int port) {
    	this.command = command;
    	this.file = file;
        this.hostname = hostname;
        this.port = port;
    }
    
    public void SendMsg() {
    	message = "#MsgFromClient#\n" + name + ": " + message + "\n";            
        try {
        	System.out.println("hstNm="+hostname + " prt" + port); 
        	s = new Socket(hostname, port);         	
			s.getOutputStream().write(message.getBytes());
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }
    
    public void SendFile() {     
		try {
			s = new Socket(hostname, port);
			message = "#FileShare#\n";
			s.getOutputStream().write(message.getBytes());  
			message = file.getName()+ "\n";
			s.getOutputStream().write(message.getBytes());
			message = "Receiving " + file.getName()+ "\n";
			s.getOutputStream().write(message.getBytes()); 
			s.close();
	   		System.out.print("File Send");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 		
    	SocketChannel socketChannel = null;
    	    	
		try {
			socketChannel = SocketChannel.open();
			InetSocketAddress sockAddr = new InetSocketAddress(hostname,9000);
			System.out.println(hostname);
			socketChannel.connect(sockAddr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		Path path = file.toPath();	
		//System.out.println(file.getAbsolutePath());
		//Path path = Paths.get(file.getAbsolutePath());
		FileChannel inChannel = null;
		try {
			inChannel = FileChannel.open(path);
			ByteBuffer buffer = ByteBuffer.allocate(1024);		
			while(inChannel.read(buffer) > 0) {
				buffer.flip();
;				socketChannel.write(buffer);
				buffer.clear();
				System.out.println(socketChannel.isConnected());
			}						
			socketChannel.close();
			inChannel.close();
			System.out.println("Send File Successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			
		}		
    	/*try { 
    		message = "#FileShare#\n";
    		s = new Socket(hostname, port); 
    		s.getOutputStream().write(message.getBytes());  
    		message = file.getName()+ " " + file.length() + "\n";
    		s.getOutputStream().write(message.getBytes());       
            FileInputStream fis = new FileInputStream(file);            
           	OutputStream os = s.getOutputStream();
           	int count;
           	byte[] buffer = new byte[filesize]; // or 4096, or more
           	while ((count = fis.read(buffer)) > 0)
           	{
           	  os.write(buffer, 0, count);
           	}
	   		os.flush();
	   		os.close();
	   		fis.close();
	   		s.close();
	   		System.out.print("File Send");
        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    @Override
    public void run() {                    
    	if (command.equals("SendMsg")) SendMsg();
    	else if (command.equals("SendFile")) SendFile();       
    }
}
