package pl.marzlat;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentOptions extends Fragment {

	private Button okButton, cancelButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_options, container, false);

		okButton = (Button) view.findViewById(R.id.btn_ok);
		cancelButton = (Button) view.findViewById(R.id.btn_cancel);

		initListeners();

		return view;
	}

	private void initListeners() {

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish();

			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});

	}
	
	private void finish() {
		getFragmentManager().popBackStack();
	}

}
