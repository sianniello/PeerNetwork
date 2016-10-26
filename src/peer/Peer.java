/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import toctocprotocol.TocTocProtocol;

/**
 *
 * @author daniele
 */
public class Peer {
    
    private int port;
    private ServerSocket server;
    private TocTocProtocol ttp;
    public Peer(int port) throws IOException {
        this.port = port;
        
        ttp= new TocTocProtocol();
        server = new ServerSocket(port);
        
        (new Thread(new ClientHandler(port))).start();
        
        Executor executor = Executors.newFixedThreadPool(1500);
        
        while(true){
            executor.execute(new ServerHandler(server.accept(), ttp));
        }
        
    }
    
    public static void main(String[] args) {
        
        if(args.length<1){
            System.err.println("BAD USAGE: inseri porta");
            System.exit(0);
        }
        
        try {
            Peer peer = new Peer(Integer.parseInt(args[0]));
        } catch (IOException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
}
