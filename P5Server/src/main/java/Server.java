import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public class Server {
	ArrayList<ClientThread> clients;
	ArrayList<Integer> topThree,topPlayers;
	int scount = 1;
	TheServer server;
	ArrayList<String> topScore; 
	Integer portnum,cnum;
	FindNextMove f;
	private Consumer<Serializable> callback, updateCnum;
	String defaultString = "b b b b b b b b b";
	String delim = "[ ]+" ;
	String[] defaultBoard = defaultString.split(delim);
	Server(Consumer<Serializable> call, Consumer<Serializable> clientnum, Integer pnum){
		cnum = 0;
		portnum = pnum;
		callback = call;
		updateCnum = clientnum;
		server = new TheServer();
		clients = new ArrayList<ClientThread>();
		topThree = new ArrayList<Integer>();
		topPlayers = new ArrayList<Integer>();
		topScore = new ArrayList<String>();
		f = new FindNextMove();
		server.start();
	}
	 class TheServer extends Thread{
		public void run() {
			//port connection -----------------------------------need to be changed
			try(ServerSocket mysocket = new ServerSocket(portnum);){
			    System.out.println("Server is waiting for a client!");
			    topThree.addAll(Arrays.asList(0,0,0));
			    topPlayers.addAll(Arrays.asList(-1,-1,-1));
			    while(true) {
					ClientThread c = new ClientThread(mysocket.accept(), scount);
					callback.accept("client has connected to server: " + "client #" + cnum);
					updateCnum.accept(++cnum);
					clients.add(c);
					topScore.add("Top 1: ");
					topScore.add("Top 2: ");
					topScore.add("Top 3: ");
					c.start();
					scount++;
					
				}
			}//end of try
			catch(Exception e) {
					callback.accept("Server socket did not launch");
			}
		}//end of run
	}
	 class ClientThread extends Thread{
			Socket connection;
			ObjectInputStream in;
			ObjectOutputStream out;
			int status = 1;
			String[] state;
			int playernum;
			int count;
			int points = 0;
			
			String difficulty;
			ClientThread(Socket s, int scount){
				this.playernum = scount;
				this.count = scount;
				connection = s;
			}
			public void updateClient(Gameinfo gameinfo) {
				ClientThread t = clients.get(count - 1);
				try {
					if(t.status == 1) {
						t.out.writeObject(gameinfo);
					}
					t.out.reset();
					}
					catch(Exception e) {}
				 
			 }
			 public void checkTop(int p_id,int wins) {
				 int check = 0;
				 for(int i = 0;i < 3;i++) {
					 if(p_id == topPlayers.get(i)) {
						 check = 1;
						 break;
					 }
					 else if(topPlayers.get(i) == -1) {
						 topPlayers.set(i, p_id);
						 check = 1;
						 break;
					 }
				 }
				 if(check == 0) {
					 if(topThree.get(2) < wins) {
						 for(int i = 0; i < 3; i++) {
							 if(clients.get(topPlayers.get(i)).points == topThree.get(2)) {
								 topPlayers.set(i, p_id);
								 break;
							 }
						 }
						 topThree.set(2, wins);
					 }
				 }
				 else {
					 for(int i = 0; i < 3;i++) {
						 if(topThree.get(i) == wins-1) {
							 topThree.set(i, wins);
							 break;
						 }
					 }
				 }
				 Collections.sort(topThree);
				 Collections.reverse(topThree);
				 topScore.set(0, "Top 1: "+topThree.get(0)+" points");
				 topScore.set(1, "Top 2: "+topThree.get(1)+" points");
				 topScore.set(2, "Top 3: "+topThree.get(2)+" points");
				 
			 }
			 public void run() {
				 try {
						in = new ObjectInputStream(connection.getInputStream());
						out = new ObjectOutputStream(connection.getOutputStream());
						connection.setTcpNoDelay(true);	
					}
					catch(Exception e) {
						System.out.println("Streams not open");
					}
					Gameinfo tmp = new Gameinfo();
					tmp.state = defaultBoard;
					tmp.topScore = topScore;
					tmp.word = "You are Player# " +playernum;
					tmp.wins = 0;
					tmp.Id = playernum;
					tmp.instruction = "initialize";
					try {
						clients.get(count-1).out.writeObject(tmp);
						clients.get(count -1).out.reset();
					}catch (IOException e) {
						e.printStackTrace();
					}
				while(true) {
					try {
						Gameinfo input = (Gameinfo)in.readObject();
						callback.accept(input.instruction);
						if(input.instruction.equals("difficulty")) {
							difficulty = input.difficulty;
							callback.accept(input.word);
							input.Id = playernum;
							state = f.find(defaultBoard, difficulty);
							input.state = state;
							input.instruction = "change";
							input.word = "server made a play";
							updateClient(input);
						}
						else if(input.instruction.equals("find")) {
								state = f.find(input.state, input.difficulty);
									callback.accept(input.word);
									input.state = state;
									input.instruction = "change";
									input.word = "server made a play";							
									updateClient(input);
									if(f.checkO(state)) {
										input.instruction = "end";
										input.word = "you won";
										input.wins++;
										points = input.wins;
										checkTop(playernum,input.wins);
										input.topScore = topScore;
										updateClient(input);
									}
									else if(f.checkX(state)) {
										input.instruction = "end";
										input.topScore = topScore;
										input.word = "you lose";
							
										updateClient(input);
									}
									else if(f.enddraw(state)) {
										input.instruction = "end";
										input.word = "draw";
										input.topScore = topScore;
										updateClient(input);
									}
						}
					}
					catch(Exception e){
						cnum--;
						callback.accept("OOOOPPs...Something wrong with the socket from client: " + playernum + "....closing down!");
				    	clients.get(count -1).status = 0;
				    	updateCnum.accept(cnum);
				    	break;
					}
				}

			 }
	}
	}
	 
