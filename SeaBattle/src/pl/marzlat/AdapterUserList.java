package pl.marzlat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterUserList extends ArrayAdapter<User> {

	private ArrayList<User> users;
	private Context context;
	private int resource;

	public AdapterUserList(Context context, int resource, List<User> objects) {
		super(context, resource, objects);

		this.users = (ArrayList<User>) objects;
		this.resource = resource;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		UserHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(resource, parent, false);

			holder = new UserHolder();
			holder.lp = (TextView) row.findViewById(R.id.txt_lp);
			holder.img = (ImageView) row.findViewById(R.id.image);
			holder.username = (TextView) row.findViewById(R.id.txt_username);
			holder.wins = (TextView) row.findViewById(R.id.txt_wins);
			holder.losses = (TextView) row.findViewById(R.id.txt_losses);

			row.setTag(holder);
		} else {
			holder = (UserHolder) row.getTag();
		}

		User user = users.get(position);
		holder.lp.setText(String.valueOf(position + 1) + ".");
		holder.username.setText(user.getUsername());
		holder.wins.setText(user.getWins() + "");
		holder.losses.setText(user.getLosses() + "");
		holder.img.setImageResource(android.R.drawable.ic_menu_gallery);

		return row;
	}

	private static class UserHolder {
		TextView lp;
		ImageView img;
		TextView username;
		TextView wins;
		TextView losses;
	}
}
