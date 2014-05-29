package pl.marzlat;

import java.sql.SQLException;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentMenu extends Fragment {

	private Button newGameButton, loginButton, statisticsButton, optionsButton,
			exitButton;
	private SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);

		prefs = getActivity().getSharedPreferences(SeaBattle.PREFS_NAME, 0);

		newGameButton = (Button) view.findViewById(R.id.btn_new_game);
		loginButton = (Button) view.findViewById(R.id.btn_login);
		statisticsButton = (Button) view.findViewById(R.id.btn_statistics);
		optionsButton = (Button) view.findViewById(R.id.btn_options);
		exitButton = (Button) view.findViewById(R.id.btn_exit);

		initListeners();

		return view;
	}

	private void initListeners() {

		newGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				replaceFragment(new FragmentSelectGame());

			}

		});

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (loginButton.getText().toString().endsWith(getString(R.string.btn_login))) {
					
					Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
					startActivity(loginIntent);
					
				} else {

					SharedPreferences.Editor editor = prefs.edit();

					editor.putString(SeaBattle.PREFS_USERNAME, "");
					editor.putString(SeaBattle.PREFS_PASSWORD, "");

					editor.commit();

					loginButton.setText(getString(R.string.btn_login));

				}
			}
		});

		statisticsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent statisticsIntent = new Intent(getActivity(), StatisticActivity.class);
				startActivity(statisticsIntent);
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							SeaBattle.getDatabase().open();

							SeaBattle.getDatabase().getTopUsers(10);
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {

							SeaBattle.getDatabase().close();
						}
						
					}
				}).start();
				
			}
		});

		optionsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				replaceFragment(new FragmentOptions());

			}
		});

		exitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		if (!prefs.getString(SeaBattle.PREFS_USERNAME, "").equalsIgnoreCase("")) {
			loginButton.setText(R.string.btn_logout);
		}
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
