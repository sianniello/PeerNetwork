package joinserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import vectorclock.VectorClock;

class ServerHandler implements Runnable {
	private Socket client;
	private HashSet<InetSocketAddress> set;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private VectorClock vc;


	public ServerHandler(Socket client, HashSet<InetSocketAddress> set) throws IOException {
		this.client = client;
		this.set = set;
		
		vc = new VectorClock();
		
		out = new ObjectOutputStream(client.getOutputStream());
		in = new ObjectInputStream(client.getInputStream());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		try {
			InetSocketAddress address = (InetSocketAddress) in.readObject();
			vc.receiveAction((HashMap<InetSocketAddress, Integer>) in.readObject());
			System.out.println("Server " + Thread.currentThread().getName() + " ricevuto: " + address.toString()); 
			
			synchronized(this){
				set.add(address);
			}
			
			out.writeObject(set);
			out.writeObject(vc);

		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
