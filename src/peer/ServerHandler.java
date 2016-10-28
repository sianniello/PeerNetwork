package peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import toctocprotocol.TocTocProtocol;
import vectorclock.VectorClock;

class ServerHandler implements Runnable {

    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private TocTocProtocol ttp;
    private VectorClock vc;
    
    public ServerHandler(Socket client, TocTocProtocol ttp, SocketAddress socketAddress) throws IOException {
        this.client = client;
        this.ttp = ttp;
        vc = new VectorClock((InetSocketAddress) socketAddress);
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        
    }

    @SuppressWarnings("unchecked")
	@Override
    public void run() {
    
        try {
            String message = (String) in.readObject();
            vc.receiveAction((HashMap<InetSocketAddress, Integer>) in.readObject());
            System.out.println("Lato Server Peer " +  ": ho ricevuto" + message);
            System.out.println(vc.toString());
            out.writeObject(ttp.processIn(message));
            vc.tock();
            out.writeObject(vc.getVector());
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
