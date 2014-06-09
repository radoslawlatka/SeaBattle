package pl.marzlat.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import pl.marzlat.BluetoothDevicesActivity;
import pl.marzlat.Gameplay;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothClientPlayer  extends Player {

	private ConnectionThread cThread;
	private Gameplay gameplay;
	
	public BluetoothClientPlayer(String name, List<Ship> ships, Area area, String address) {
		super(name, ships, area);
		
		cThread = new ConnectionThread(address);

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

		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private PrintWriter output;
		private BufferedReader input;
		private String address;
		private String string;

		public ConnectionThread(String address) {
			this.address = address;
			BluetoothSocket tmp = null;
			mmDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(this.address);
			try {
				UUID uuid = UUID.fromString("1939c320-ef68-11e3-ac10-0800200c9a66");
				tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmSocket = tmp;
		}

		@Override
		public void run() {
			try {
				Log.d("BT ConnTread", "connecting..");
				mmSocket.connect();
				Log.d("BT ConnTread", "connected!");
				output = new PrintWriter(mmSocket.getOutputStream(), true);
				input = new BufferedReader(new InputStreamReader(
						mmSocket.getInputStream()));


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
					mmSocket.close();
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
