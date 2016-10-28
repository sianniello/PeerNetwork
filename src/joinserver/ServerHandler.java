package joinserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
			
			InetSocketAddress address = (InetSocketAddress) in.readObject();	//riceve dal peer il suo indirizzo
			HashMap<InetSocketAddress, Integer> hm = (HashMap<InetSocketAddress, Integer>) in.readObject();	//riceve dal peer il suo VectorClock

			System.out.println("Server " + Thread.currentThread().getName() + " ricevuto: " + address.toString()); 
			System.out.println("Server " + Thread.currentThread().getName() + " ricevuto: " + hm.toString());
			
			synchronized(this){
				set.add(address);
			}
			
			for(InetSocketAddress isa : hm.keySet())
				if(vc.getVector().containsKey(isa))
					vc.getVector().replace(isa, hm.get(isa));
				else
					vc.getVector().put(isa, hm.get(isa));

			out.writeObject(set);
			out.writeObject(vc.getVector());
			System.out.println(vc.toString());

		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
