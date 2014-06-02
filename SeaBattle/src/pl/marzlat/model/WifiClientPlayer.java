package pl.marzlat.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import pl.marzlat.Gameplay;
import android.util.Log;

public class WifiClientPlayer extends Player {

	private InetSocketAddress iAddress;
	private ConnectionThread cThread;
	private Gameplay gameplay;
	
	public WifiClientPlayer(String name, List<Ship> ships, Area area, String address, int port) {
		super(name, ships, area);
		
		iAddress = new InetSocketAddress(address, port);
		cThread = new ConnectionThread(iAddress);

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

		private Socket socket;
		private PrintWriter output;
		private BufferedReader input;
		private InetSocketAddress sAddress;
		private String string;

		public ConnectionThread(InetSocketAddress sAddress) {
			this.sAddress = sAddress;
		}

		@Override
		public void run() {

			socket = new Socket();

			try {
				Log.d("ConnTread", "connecting..");
				socket.connect(iAddress, 4000);
				Log.d("ConnTread", "connected!");
				output = new PrintWriter(socket.getOutputStream(), true);
				input = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));


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
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		public void send(String msg) {
			if (output != null) {
				Log.d("ConnectionThread", "Wys³ano: " + msg);
				output.print(msg + "\n");
				output.flush();
			}
		}
		
		private void returnQuery(String query) {
			Log.d("ConnThread", "returnQuery: " + query);
			gameplay.receiveQueryFromOpponent(Integer.valueOf(String.valueOf(query.charAt(0))),
												Integer.valueOf(String.valueOf(query.charAt(2))));
		
		}
		
		private void returnAnswer(String answer) {
			gameplay.receiveAnswerFromOpponent(answer);
		}

	}


	public void setGameplay(Gameplay gameplay) {
		this.gameplay = gameplay;
	}
	
}
