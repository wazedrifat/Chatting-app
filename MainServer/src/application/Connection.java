package application;

public class Connection extends Thread{
	Client client;
	Connection(Client client){
		this.client = client;
	}
	
	@Override
	public void run() {	
		//System.out.println("Send Msg");
		//System.out.println(client.IP);
		if (this.client.state.equals("Rejected")) {
			client.sendMsg("#Rejected#");
		}
		else if (this.client.state.equals("Accepted")) {
			client.sendMsg("#Accepted#");
		}
		else {
			client.sendMsg("#MsgFromServer#");
			client.sendList();
		}		
		System.out.println("Thread Ended");

	}
}
