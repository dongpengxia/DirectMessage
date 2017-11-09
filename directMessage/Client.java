//Dongpeng Xia
//Client Class

package directMessage;

import java.io.*;
import java.net.*;
import java.util.*;

//Client is a part of the server-client relationship. Client answers the initial call from the server in the chat.
public class Client
{	
	//socket client is connected to
	private Socket sock;
	
	//listens to messages sent from server
	private ClientListener clientListen;
	
	//sends messages out to server
	private ClientTalker clientTalk;

	//Client
	public Client(String ip, int port)
	{		
		try
		{	
			//open socket
	        sock = new Socket(ip, port);
	        
		    System.out.println("Welcome! Type and enter a message to message the server or /quit to quit.");
	 		
		    //assign listener to get messages from server
            clientListen = new ClientListener(sock);
		    	
            //assign talker to send messages to server
			clientTalk = new ClientTalker(sock);
			
			//start listener and talker
			clientListen.start();
			clientTalk.start();
	    }
	    catch ( Exception error ) {}
		
	}//end Client(String, int)
	
	//Talker to Server sends messages to Server from Client
	public class ClientTalker extends Thread
	{
		//socket client is connected to
		private Socket socket;
		
		//keyboard scanner to read input
		private Scanner scanner;
		
		//output buffer to send messages
		private BufferedWriter bOutput;
			
		//Client Talker sends messages out 
		public ClientTalker(Socket client)
		{
			try
			{
				scanner = new Scanner(System.in);
				socket = client;
				bOutput = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );
			}
			catch(Exception error) {}
			
		}//end ClientTalker(Socket)
		
		//run method for ClientTalker
		@Override
	    public void run()
	    {
			try 
			{
				//closes on "/quit"
				while( true )
				{
					//read next line to send out
					String nextLine = scanner.nextLine();
	        		
					//send next line
                    bOutput.write(nextLine + "\n");
                    bOutput.flush();
		        		
                    //if "/quit" is typed
                    if (nextLine.split(" ")[0].equals("/quit"))
                    {
                        System.out.println("Quitting");
                        socket.close();
                        System.exit(0);
                    }
				}
			}
			catch (Exception error) {}
			
	    }//end run
		
	}//end ClientTalker
	
	//ClientListener listens for new messages from the server
	public class ClientListener extends Thread
	{
		//input buffer
		private BufferedReader bInput;
		
		//constructor
		public ClientListener(Socket sckt) throws IOException
		{
	        bInput = new BufferedReader( new InputStreamReader( sckt.getInputStream() ) );
	        
		}//end ClientListener
		
		//run method for ClientListener
		@Override
	    public void run()
	    {
			try
			{
				while(true)
				{
					//read next line
					String nextLine = bInput.readLine();
					
					if (!nextLine.equals(""))
					{
						System.out.println("Server: " + nextLine);
						
						//if "/quit" is typed
                        if (nextLine.split(" ")[0].equals("/quit"))
                        {
                            System.out.println("Quitting");
							System.exit(0);
                        }
					}
				}
			}
			catch (Exception error) {}
	    }//end run
		
	}//end ClientListener
	
}//end Client