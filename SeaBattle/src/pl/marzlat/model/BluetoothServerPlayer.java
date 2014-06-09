package pl.marzlat.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import pl.marzlat.Gameplay;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothServerPlayer extends Player {

	private ConnectionThread cThread;
	private Gameplay gameplay;
	private int port;
	
	public BluetoothServerPlayer(String name, List<Ship> ships, Area area) {
		super(name, ships, area);
		this.port = port;
		cThread = new ConnectionThread();

		cThread.start();
	}
	
	@Override
	public void receiveShot(int x, int y, Gameplay gameplay) {
		this.gameplay = gameplay;
		Log.d("WifiServPlayer", "Send query to opponent: " + x + " " + y);
		 
		cThread.send(x + " " +y);
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

		private final BluetoothServerSocket mmServerSocket;
		private BluetoothSocket clientSocket = null;
		private PrintWriter output;
		private BufferedReader input;
		private String string;

		public ConnectionThread() {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			BluetoothServerSocket tmp = null;
			
			try {
				UUID uuid = UUID.fromString("1939c320-ef68-11e3-ac10-0800200c9a66");
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("SeaBattle", uuid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmServerSocket = tmp;
		}
		
		@Override
		public void run() {

			try {
				Log.d("BT ConnTread", "Waiting for client..");
				clientSocket = mmServerSocket.accept();
				
				Log.d("BT ConnTread", "connected!");
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
					if(mmServerSocket!=null) mmServerSocket.close();
					if(clientSocket!=null) clientSocket.close();
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