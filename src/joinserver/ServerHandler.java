package joinserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
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


	public ServerHandler(Socket client, HashSet<InetSocketAddress> set, VectorClock vc) throws IOException {
		this.client = client;
		this.set = set;
		this.vc = vc;
		vc.add();
		out = new ObjectOutputStream(client.getOutputStream());
		in = new ObjectInputStream(client.getInputStream());

	}

	@Override
	public void run() {

		try {
			InetSocketAddress address = (InetSocketAddress) in.readObject();
			System.out.println("Server " + Thread.currentThread().getName() + " ricevuto: " + address.toString()); 
			vc.tick();
			synchronized(this){
				set.add(address);
			}

			out.writeObject(set);
			out.writeObject(vc);

		} catch (IOException ex) {
			Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
