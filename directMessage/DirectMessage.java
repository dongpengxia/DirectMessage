//Dongpeng Xia
//DirectMessage is a two-way chat between a server and a client.

package directMessage;

//DirectMessage
public class DirectMessage
{	
	public static void main(String[] args) throws Exception
	{
		//default port and ip
		int port = 11021;
		String ip = "localhost";
		
		if(args.length >= 2)
		{
			//set port and ip to arguments
			ip = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		try
		{
			//try to start server first
			new Server(port);
		}
		catch(Exception error)
		{
			//if server already started, create a client
			new Client(ip, port);
		}
		
	}//end main
	
}//end DirectMessage