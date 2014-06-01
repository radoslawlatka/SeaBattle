package pl.marzlat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pl.marzlat.model.Area;
import pl.marzlat.model.Ship;
import pl.marzlat.model.WifiClientPlayer;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentWifiTest extends Fragment {

	private Button send;
	private TextView text;
	private EditText edit;
private ConnectionThread cThread;
WifiClientPlayer player;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wifi_test, container, false);

		send = (Button) view.findViewById(R.id.send);
		text = (TextView) view.findViewById(R.id.text);
		edit = (EditText) view.findViewById(R.id.edit);

		initListeners();

		//cThread = new ConnectionThread();
		//cThread.start();
		
		//player = createAndroidPlayer();
		
		return view;
	}

	private void initListeners() {

		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			cThread.send(edit.getText().toString());	

			}
		});


	}
	
	private void setText(final String txt) {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				text.setText(txt);
				
			}
		});
	}
	
	private class ConnectionThread extends Thread {

		private Socket socket;
		private PrintWriter output;
		private BufferedReader input;
		
		@Override
		public void run() {

			socket = new Socket();
			
			try {
				Log.d("ConnTread", "connecting..");
				socket.connect(new InetSocketAddress("192.168.2.103", 1131), 4000);
				Log.d("ConnTread", "connected!");
				output = new PrintWriter(socket.getOutputStream(), true);
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String str;
				
				output.print("dupa1\n");
				output.flush();

				while((str = input.readLine()) != null) {
					
					Log.e("ConnTread", "Odebrano: " + str);
					
					setText(str);
					//output.print("...\n");
					//output.flush();
					
				}
				
				
				
				System.out.println("end wating");
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
			if(output != null) {
				Log.d("ConnectionThread", "send()");
				output.print(msg + "\n");
				output.flush();
			}
		}
		
	}
	
    public WifiClientPlayer createAndroidPlayer()
    {
        String name = "Android";
        List<Ship> ships;
        ships = createShips();
        Area area = new Area();
        area.autoPlacement(ships);
        return new WifiClientPlayer(name, ships, area, "192.168.2.103", 1131);
    }
	
    private List<Ship> createShips() {
        List<Ship> ships;
        ships = new ArrayList<Ship>();
        for (int i = 0; i < 4; i++) {
            ships.add(new Ship(Ship.SMALL));
        }
        for (int i = 0; i < 3; i++) {
            ships.add(new Ship(Ship.MEDIUM));
        }
        for (int i = 0; i < 2; i++) {
            ships.add(new Ship(Ship.LARGE));
        }
        for (int i = 0; i < 1; i++) {
            ships.add(new Ship(Ship.XLARGE));
        }
        return ships;
    }

}
