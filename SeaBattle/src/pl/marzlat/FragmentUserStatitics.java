package pl.marzlat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

public class FragmentUserStatitics extends Fragment {

	private TextView username, wins, losses;
	private TableLayout statisticTable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_statistics, container, false);

		statisticTable = (TableLayout) view.findViewById(R.id.statistic_table);
		username = (TextView) view.findViewById(R.id.txt_username);
		wins = (TextView) view.findViewById(R.id.txt_wins);
		losses = (TextView) view.findViewById(R.id.txt_losses);

		return view;
	}

	public void setUser(User user) {
		
		if (!user.getUsername().equals("")) {
			setVisibility(View.VISIBLE);
			username.setText(user.getUsername());
			wins.setText(user.getWins() + "");
			losses.setText(user.getLosses() + "");
		} else {
			username.setText(getString(R.string.txt_not_logged_in));
			setVisibility(View.INVISIBLE);
		}
	}
	
	private void setVisibility(int visibility) {
		statisticTable.setVisibility(visibility);
	}

}
