import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
    
public class ServerForTwo extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output,output2;
    private ObjectInputStream input, input2;
    private ServerSocket server,server2;
    private Socket Connection,Connection2;
    
    public ServerForTwo(){
        super("messenger-serverForTwo");
        userText= new JTextField();
        userText.setEditable(false);
        
        userText.addActionListener(
                new ActionListener(){
                  public void actionPerformed(ActionEvent Event){
                     sendMessage(Event.getActionCommand());
                     userText.setText("");
                     
                  }  
                });
        add(userText,BorderLayout.SOUTH);
        chatWindow= new JTextArea();
        add(new JScrollPane(chatWindow));
        chatWindow.setEditable(false);

        setSize(300,600);
        setVisible(true);
    }
    
    //server
    public void startRunning(){
        try{
            
   //         runServer("172.27.16.154",3128,1234);
            server= new ServerSocket(1234,100);
            server2=new ServerSocket(8117,100);
            while(true){
                try{
                	waitForConnection();
                	setupStreams();
                	whileChatting();
                }catch(EOFException eofException){
                    showMessage("\nServer Ended the connection ");
                }finally{
                	closeCrap();
                }
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
            
        }
    }
    
    private void connect(final ServerSocket s){
        
    } 
    private void waitForConnection() throws IOException{
        
    	showMessage("Waiting to connect..\n");
    	Connection = server.accept(); //System.out.println("connection1");
    	showMessage("Connected to "+Connection.getInetAddress().getHostName()+"\n");
    	showMessage("Waiting to connect2..\n");
    	Connection2 = server2.accept();//System.out.println("connection2");
    	showMessage("Connected to "+Connection2.getInetAddress().getHostName()+"\n");
    	
        /*showMessage("Waiting to connect..\n");
    	ConnectThread c1= new ConnectThread(server);
        ConnectThread c2= new ConnectThread(server2);
        c1.run();
        c2.run();
        Connection= c1.con();
        Connection2= c2.con();*/
    }
    
    private void setupStreams() throws IOException{
    	output = new ObjectOutputStream(Connection.getOutputStream());
    	output.flush();
    	input = new ObjectInputStream(Connection.getInputStream());
    	showMessage("\nStreams are setup\n");
    	output2 = new ObjectOutputStream(Connection2.getOutputStream());
    	output2.flush();
    	input2 = new ObjectInputStream(Connection2.getInputStream());
    	showMessage("\nSecond Streams are setup\n");
    }
    
  /*  private String getMessage(final ObjectInputStream in){
    	String s;
    	Thread t= new Thread(
    				new Runnable(){
    					public void run(){
    						s=(String)in.readObject();
    					}
    				}
    			);
    }*/
	
    ChatThread t1=new ChatThread(input);
    ChatThread t2=new ChatThread(input2);
    
	private void whileChatting() throws IOException{
    	String msg="You are connected", msg2;
    	showMessage(msg);
    	ableToType(true);
        
        
    	do{
    		
    			t1.run();
                        msg= "Client1 - "+t1.msg() ;
    			showMessage("Client1 - "+msg+"\n");
    			sendMessage2(msg);
    		
    	
                        t2.run();
    			msg2= "Client2 - "+t2.msg();
    			showMessage("Client2 - "+msg2+"\n");
    			sendMessage1(msg2);
    		 
    	}while(true);
    }
    
    //close crap
    private void closeCrap(){
    	showMessage("\n Closing Connections..\n");
    	ableToType(false);
    	try{
    		Connection.close();
    		Connection2.close();
    		
    		output.close();
    		t1.stp();
    		
    		output2.close();
                t2.stp();
    	}catch(IOException ioException ){
    		ioException.printStackTrace();
    	}
    	showMessage("\n Closed Connections..\n");
    }
    private void sendMessage(String msg){
    	try{
    		output.writeObject("\nServer - "+msg);
    		output.flush();
    		showMessage("\nServer - "+msg);
    	}catch(IOException ioException){
    		chatWindow.append("\nError: cannot send that msg");
    	}
        try{
    		output2.writeObject("\nServer - "+msg);
    		output2.flush();
    		showMessage("\nServer - "+msg);
    	}catch(IOException ioException){
    		chatWindow.append("\nError: cannot send that msg");
    	}
    	
    }
    private void sendMessage1(String msg){
    	try{
    		output.writeObject("\nClient2 - "+msg);
    		output.flush();
    		showMessage("\nClient2 - "+msg);
    	}catch(IOException ioException){
    		chatWindow.append("\nError: cannot send that msg");
    	} 
    	
    }
    private void sendMessage2(String msg){
    	try{
    		output2.writeObject("\nClient1 - "+msg);
    		output2.flush();
    		showMessage("\nClient1 - "+msg);
    	}catch(IOException ioException){
    		chatWindow.append("\nError: cannot send that msg");
    	} 
    	
    }
    
    private void showMessage(final String txt){
    	SwingUtilities.invokeLater(
    				new Runnable(){
    					public void run(){
    						chatWindow.append(txt);
    					}
    				}
    			);
    }
    
    private void ableToType(final Boolean tof){
    	SwingUtilities.invokeLater(
    				new Runnable(){
    					public void run(){
    						userText.setEditable(tof);
    					}
    				}
    			);
    }
}
