



import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatThread implements Runnable{
    
    private ObjectInputStream in;
    private String s;
    public ChatThread(ObjectInputStream in){
        this.in=in;
        
    }
    public void run(){
        try {
            s=(String) in.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String msg(){
        return s;
    }
    public void stp(){
        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
