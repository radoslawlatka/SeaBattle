package pl.marzlat;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentRanking extends Fragment {

	private ListView usersListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ranking, container, false);
		usersListView = (ListView) view.findViewById(R.id.listView_users);
		return view;
	}

	public void setUserListAdapter(ArrayList<User> userList) {
		usersListView.setAdapter(new AdapterUserList(getActivity(),
				R.layout.row_user, userList));
	}

}
