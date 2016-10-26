/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toctocprotocol;

/**
 *
 * @author daniele
 */
public class TocTocProtocol {
 
    private static final int WAIT =0;
    private static final int COMMIT =1;
    private static final int ABORT =2;
    private static final int END = 3;
    
    private int state; //var che tiene conto dello stato attuale 
    private int n_err; //var che tiene conto errori commessi

    public TocTocProtocol() {
        state=n_err = 0;
    }
    
    public String processIn(String in){
        String out = null;
        
        switch(state){
            case WAIT: if(in.compareTo("Toc Toc")==0){
                            state = COMMIT;
                            out = "Chi è?";
                        
                        }else if(++n_err<3){
                            out="Devi dire Toc Toc";
                        }else{
                            state = ABORT;
                            out = "Sorry ma dovevi dire Toc Toc";
                            n_err = 0;
                        }
            break;
            case COMMIT: out = "Ciao " + in;
                        state = END;
            break;
            case ABORT: out = "Spiacente non apro a chi non segue il protocollo";
                        state = WAIT;
            break;
            case END: out = "Entra pure";
                      state = WAIT;
            
            
                        
            
        }
        
        return(out);
        
    }
    
    
    
}
