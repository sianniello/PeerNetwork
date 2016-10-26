/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import toctocprotocol.TocTocProtocol;

/**
 *
 * @author daniele
 */
class ServerHandler implements Runnable {

    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private TocTocProtocol ttp;
    public ServerHandler(Socket client, TocTocProtocol ttp) throws IOException {
        this.client = client;
        this.ttp = ttp;
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        
    }

    @Override
    public void run() {
    
        try {
            String message = (String) in.readObject();
            System.out.println("Lato Server Peer " +  ": ho ricevuto" + message);
            out.writeObject(ttp.processIn(message));
            
        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
