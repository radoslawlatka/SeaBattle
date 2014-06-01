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
		cThread.send(x + " " +y);
		
		//return str;
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

				output.print("Connected!\n");
				output.flush();

				while ((string = input.readLine()) != null) {

					Log.e("ConnTread", "Odebrano: " + string);
					if(string.charAt(0)=='S')
					returnAnswer(string);

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

		private void returnAnswer(String answer) {
			gameplay.receiveAnswerFromOpponent(answer);
		}

	}


	public void setGameplay(Gameplay gameplay) {
		this.gameplay = gameplay;
	}
	
}
