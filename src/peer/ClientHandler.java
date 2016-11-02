package peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import vectorclock.VectorClock;

class ClientHandler implements Runnable {
    private int port;// questa è la porta della parte Server del peer
    private VectorClock vc;
    
    public ClientHandler(int port) {
        this.port = port;
        vc = new VectorClock(new InetSocketAddress(port));
    }

    public void show(HashSet<InetSocketAddress> set){
        
        for(InetSocketAddress addr: set)
            System.out.println(addr.getPort());
    }
    
    @SuppressWarnings({ "resource", "unchecked" })
	public void join() throws IOException, ClassNotFoundException{
        Socket client = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        client = new Socket("localhost", 1099);
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        
        out.writeObject(new InetSocketAddress(port));	//il client spedisce il proprio indirizzo
        out.writeObject(vc.getVector());	//il client spedisce il proprio vector clock
        vc.tock();
        System.out.println(vc.toString());
        show((HashSet<InetSocketAddress>) in.readObject());	//il client riceve la lista degli indirizzi disponibili
        vc.receiveAction((HashMap<InetSocketAddress, Integer>) in.readObject());	//il client riceve il VectorClock
        System.out.println(vc.toString());
        
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void run() {
    
        Scanner s = new Scanner(System.in);
        int peerPort = 0;
        String message = null;
        for(;;){
            try {
                System.out.println("LISTA PEER CONNESSI");
                join();
                System.out.println("QUALE PEER VUOI CONTATTARE?");
                peerPort = Integer.parseInt(s.nextLine());
                System.out.println("COSA VUOI SPEDIRE?");
                message = s.nextLine();
                
                Socket client = new Socket("localhost", peerPort);
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                
                out.writeObject(message);
                vc.tock();
                out.writeObject(vc.getVector());
                
                System.out.println("Peer " + peerPort + " ha risposto: " + (String) in.readObject());
                vc.receiveAction((HashMap<InetSocketAddress, Integer>) in.readObject());
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
