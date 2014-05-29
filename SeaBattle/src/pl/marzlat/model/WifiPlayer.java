package pl.marzlat.model;

import java.net.Socket;
import java.util.List;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

public class WifiPlayer extends Player {

    private WifiP2pManager mManager;
    private Channel mChannel;
	private Socket socket;
	
	public WifiPlayer(String name, List<Ship> ships, Area area) {
		super(name, ships, area);
		// TODO Auto-generated constructor stub
	}

}
