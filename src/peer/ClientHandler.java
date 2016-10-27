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
        vc = new VectorClock();
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
        
        vc.setKey(new InetSocketAddress(port));
        
        out.writeObject(new InetSocketAddress(port));
        out.writeObject(vc);
        vc.tock();
        show((HashSet<InetSocketAddress>) in.readObject());
        vc.receiveAction((HashMap<InetSocketAddress, Integer>) in.readObject());
        
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
                out.writeObject(vc);
                System.out.println("Peer " + peerPort + " ha risposto: " + (String) in.readObject());
                vc.receiveAction((HashMap<InetSocketAddress, Integer>) in.readObject());
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
