import java.io.*;

public class ChatTemp1{
	
	public static void main(String[] args){
		
		try {
		ChatThread s1 = new ChatThread(new ObjectInputStream(System.in));
		s1.run();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
}