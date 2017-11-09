//Dongpeng Xia
//Server class

package directMessage;

import java.io.*;
import java.net.*;
import java.util.*;

//Server
public class Server
{
	//socket for server
	private ServerSocket sock;
	
	//ServerListener lets server receive messages
	private ServerListener sListener;
	
	//ServerTalker lets server send messages
	private ServerTalker sTalker;
	
	//true if server is running, false otherwise
	boolean running;
	
	//Server
	public Server(int port) throws Exception
	{
	    try
	    {
            //open socket
            sock = new ServerSocket(port);
	    		
            System.out.println("Starting Server");
	    		
            running = true;
            //listen for connections
			while (running) //server is waiting
		    {
			    Socket client = sock.accept(); //waits until client calls 
			    
			    System.out.println("Server Accepted a Client Connection");
			    
			    System.out.println("Welcome! Please type and enter a message to message the client or /quit to quit.");
			    
			    //assign server listener for client
			    sListener = new ServerListener(client);
			    
			    //assign server talker for client
			    sTalker = new ServerTalker(client);
			    
			    //start listener and talker
			    sTalker.start();
			    sListener.start();
		    }
			
	    }//end try
	    catch( Exception error ) 
	    { 
	    		//port is already taken
	    		throw new Exception("PORT UNAVAILABLE");
	    		
        }//end catch
	    
	}//end Server(int)
	
	//Listener to Client-sent messages
	public class ServerListener extends Thread
	{
		//socket that client is connected to
		private Socket clientSocket;
		
		//input buffer
		private BufferedReader bInput;
		
		//constructor
		public ServerListener(Socket sckt) throws IOException
		{
			clientSocket = sckt;
			
			//input stream
			bInput = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
			
		}//end ServerListener(Socket)
		
		//run method for ServerListener
		@Override
	    public void run()
	    {
			try 
			{
				//closes on "/quit"
				while(!clientSocket.isClosed())
				{
					//read client's message (one line)
					String nextLine = bInput.readLine();
					
					if(!(nextLine.equals("")))
					{
						System.out.println("Client: " + nextLine);
						
						//closes socket if /quit is typed
						if(nextLine.split(" ")[0].equals("/quit"))
						{
							System.out.println("Quitting");
							clientSocket.close();
							sock.close();
							running = false;
							System.exit(0);
						}
					}
				}
			}
			catch (Exception error) {}
	    }//end run
		
	}//end ServerListener
	
	//Talker to Client
	public class ServerTalker extends Thread
	{
		//client's socket
		private Socket clientSocket;
		
		//scanner reads keyboard input
		private Scanner scanner;
		
		//writes messages to client
		private PrintWriter pOut;
		
		//ServerTalker allows the server to send messages to the client
		public ServerTalker(Socket client)
		{
			try
			{
				scanner = new Scanner(System.in);
				clientSocket = client;
                pOut = new PrintWriter( client.getOutputStream(), true);
			}
			catch(Exception error) {}
			
		}//end ServerTalker(Socket)
		
		//run method for ServerTalker
		@Override
	    public void run()
	    {
			try 
			{
				//closes on "/quit"
				while(true)
				{
					String serverMsg = scanner.nextLine();
					
					if(!(serverMsg.equals("")))
					{
						pOut.println( serverMsg );
			            pOut.flush();
						
						//closes socket if "/quit" is typed
						if(serverMsg.split(" ")[0].equals("/quit"))
						{
							System.out.println("Quitting");
							clientSocket.close();
							sock.close();
							running = false;
							System.exit(0);
						}
					}
				}
			}
			catch (Exception error) {}
	    }//end run
		
	}//end ServerTalker
	
}//end Server