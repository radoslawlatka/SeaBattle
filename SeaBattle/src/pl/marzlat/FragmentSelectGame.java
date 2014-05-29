package pl.marzlat;

import pl.marzlat.gameplayview.BoardFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentSelectGame extends Fragment {

	private Button vsAndroidButton, bluetoothButton, wifiButton;
	private BluetoothAdapter btAdapter;
	public static final int REQUEST_ENABLE_BT=0, REQUEST_BT_DEVICES = 1;
	
	public FragmentSelectGame() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
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
				
				replaceFragment(new BoardFragment());
				
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
				
			}
		});
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

}
