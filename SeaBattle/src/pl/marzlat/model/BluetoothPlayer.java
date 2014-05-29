package pl.marzlat.model;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothPlayer extends Player {

	private BluetoothDevice btDevice;
	private BluetoothSocket btSocket;
	
	public BluetoothPlayer(String name, List<Ship> ships, Area area, String macAddress) {
		super(name, ships, area);
		// TODO Auto-generated constructor stub
	}
	
	public void connect() {
		
	}
	
	private void receive() {
		
	}
	
	private class ConnectionThread extends Thread {
		
	}

}
