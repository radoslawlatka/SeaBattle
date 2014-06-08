package pl.marzlat;

import java.util.ArrayList;
import java.util.List;

import android.text.format.Formatter;
import pl.marzlat.model.Area;
import pl.marzlat.model.ComputerPlayer;
import pl.marzlat.model.Player;
import pl.marzlat.model.Ship;
import pl.marzlat.model.WifiClientPlayer;
import pl.marzlat.model.WifiServerPlayer;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSelectGame extends Fragment {

	private Button vsAndroidButton, bluetoothButton, wifiButton;
	private BluetoothAdapter btAdapter;
	public static final int REQUEST_ENABLE_BT=0, REQUEST_BT_DEVICES = 1;
	private FragmentGameplay gameplay;
	
	public FragmentSelectGame() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		gameplay = new FragmentGameplay();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_select_game, container, false);
		
		vsAndroidButton = (Button) view.findViewById(R.id.btn_vs_android);
		bluetoothButton = (Button) view.findViewById(R.id.btn_bluetooth);
		wifiButton = (Button) view.findViewById(R.id.btn_wifi);

		initListeners();

		return view;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {

		case REQUEST_ENABLE_BT:
			if(resultCode == Activity.RESULT_OK) {
				Intent selectBtDevice = new Intent(getActivity().getApplicationContext(), BluetoothDevicesActivity.class);
				startActivityForResult(selectBtDevice, REQUEST_BT_DEVICES);
			} else {
				Toast.makeText(getActivity(), getString(R.string.toast_bluetooth_not_available), Toast.LENGTH_SHORT).show();
			}
			break;

		case REQUEST_BT_DEVICES:
			if(resultCode == Activity.RESULT_OK) {


			} else {

			}
			break;
		}
	}

	private void initListeners() {
		vsAndroidButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				FragmentGameplay vsAndroid = new FragmentGameplay();
				vsAndroid.setPlayer1(createLocalPlayer());
				vsAndroid.setPlayer2(createAndroidPlayer());

				replaceFragment(vsAndroid);
			}
		});

		bluetoothButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if( !btAdapter.isEnabled() ) {
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				} else {
					Intent selectBtDevice = new Intent(getActivity().getApplicationContext(), BluetoothDevicesActivity.class);
					startActivityForResult(selectBtDevice, REQUEST_BT_DEVICES);
				}
			}
		});

		wifiButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*GameplayFragment wifi = new GameplayFragment();
				wifi.setPlayer1(createLocalPlayer());
				//wifi.setPlayer2(new WifiClientPlayer("Wifi", createShips(), new Area(), "192.168.2.103", 1131));
				wifi.setPlayer2(new WifiServerPlayer("Wifi", createShips(), new Area(), 1131));

				replaceFragment(wifi);
				 */
				initWifiDialog(v);
				
				
			}


		});
	}

	public ComputerPlayer createAndroidPlayer()
	{
		String name = "Android";
		List<Ship> ships;
		ships = createShips();
		Area area = new Area();
		area.autoPlacement(ships);
		return new ComputerPlayer(name, ships, area);
	}

	public Player createLocalPlayer()
	{
		String name = getActivity().getSharedPreferences(SeaBattle.PREFS_NAME, 0)
				.getString(SeaBattle.PREFS_USERNAME, "Player");
		if(name.equals(""))
			name = "Player";
		List<Ship> ships;
		ships = createShips();
		Area area = new Area();
		return new Player(name, ships, area);
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

	private void replaceFragment(Fragment fragment) {
		FragmentTransaction ft = getActivity().getFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out,
				R.anim.slide_in, R.anim.slide_out);
		ft.replace(R.id.container, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	private void initWifiDialog(View v) {

		final Dialog dialog = new Dialog(v.getContext());
		dialog.getContext().setTheme(android.R.style.Theme_Dialog);
		dialog.setContentView(R.layout.dialog_wifi_client_server);
		dialog.setTitle(getString(R.string.label_create_account));

		final TextView yourIpAddr = (TextView) dialog.findViewById(R.id.txt_your_ip);
		final EditText enterIpAddr = (EditText) dialog.findViewById(R.id.edit_enter_ip);
		final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
		yourIpAddr.setText(getLocalIpAddress(getActivity().getApplicationContext()));

		Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
		Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

		dialog.show();

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(radioGroup.getCheckedRadioButtonId()) {
				case R.id.radio_client :
					gameplay.setPlayer2(new WifiClientPlayer("Przeciwnik", createShips(), new Area(), enterIpAddr.getText().toString(), 1131));
					break;
				case R.id.radio_server :
					gameplay.setPlayer2(new WifiServerPlayer("Przeciwnik", createShips(), new Area(),  1131));
				}
				gameplay.setPlayer1(new Player(getActivity().getSharedPreferences(SeaBattle.PREFS_NAME, 0).getString(SeaBattle.PREFS_USERNAME, "Gracz"),
						createShips(), new Area()) );
				dialog.dismiss();
				replaceFragment(gameplay);
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		radioGroup.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {


				switch(checkedId) {
				case R.id.radio_client:
					yourIpAddr.setText(getLocalIpAddress(getActivity().getApplicationContext()));
					yourIpAddr.setVisibility(View.GONE);
					enterIpAddr.setVisibility(View.VISIBLE);
					break;
				case R.id.radio_server:
					yourIpAddr.setVisibility(View.VISIBLE);
					enterIpAddr.setVisibility(View.GONE);
					break;
					
				}
				
			}
		});

	}

	public static String getLocalIpAddress(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService("wifi");
		return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
	}

}
