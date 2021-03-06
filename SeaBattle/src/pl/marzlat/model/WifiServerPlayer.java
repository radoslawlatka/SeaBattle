package pl.marzlat.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import pl.marzlat.Gameplay;
import android.util.Log;

public class WifiServerPlayer extends Player {

	private ConnectionThread cThread;
	private Gameplay gameplay;
	private int port;
	
	public WifiServerPlayer(String name, List<Ship> ships, Area area, int port) {
		super(name, ships, area);
		this.port = port;
		cThread = new ConnectionThread(port);

		cThread.start();
	}
	
	@Override
	public void receiveShot(int x, int y, Gameplay gameplay) {
		this.gameplay = gameplay;
		Log.d("WifiServPlayer", "Send query to opponent: " + x + " " + y);
		 
		cThread.send(x + " " +y);
		
		//return str;
	}
	
	@Override
	public int receiveOpponentsAnswer(String answer, Gameplay gameplay) {
		this.gameplay = gameplay;
		Log.d("WifiServPlayer", "Send answer to opponent: " + answer);
		cThread.send(answer);

		Log.d("WifiServPlayer", "Answer send: " + answer);
		return 0;
	}
	
	
	private class ConnectionThread extends Thread {

		private ServerSocket socket;
		private Socket clientSocket;
		private PrintWriter output;
		private BufferedReader input;
		private int port;
		private String string;

		public ConnectionThread(int port) {
			this.port = port;
		}

		@Override
		public void run() {

			try {
				socket = new ServerSocket(port);

				Log.d("ConnTread", "Waiting for client..");
				clientSocket = socket.accept();
				
				Log.d("ConnTread", "connected!");
				output = new PrintWriter(clientSocket.getOutputStream(), true);
				input = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));

				while ((string = input.readLine()) != null) {

					
					if(string.length() == 3) {
						Log.e("ConnTread", "Received query: " + string);
						returnQuery(string);
					}
					else {
						Log.e("ConnTread", "Received answer: " + string);
						returnAnswer(string);
					}

				}

				Log.d("ConnTread", "Connection closed...");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(socket!=null)socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		private void returnQuery(String query) {
			Log.d("ConnThread", "returnQuery: " + query);
			gameplay.receiveQueryFromOpponent(Integer.valueOf(String.valueOf(query.charAt(0))),
												Integer.valueOf(String.valueOf(query.charAt(2))));
			
		}

		public void send(String msg) {
			if (output != null) {
				Log.d("ConnectionThread", "Wys�ano: " + msg);
				output.print(msg + "\n");
				output.flush();
			}
		}

		private void returnAnswer(String answer) {
			gameplay.receiveAnswerFromOpponent(answer);
		}

	}


	public void setGameplay(Gameplay gameplay) {
		this.gameplay = gameplay;
	}
	
}
