package pl.marzlat;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.fabric.xmlrpc.base.Array;

import pl.marzlat.database.Database;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StatisticActivity extends FragmentActivity {

	private Database db;
	private ViewPager pager;
	private Button okButton;
	private ArrayList<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		db = SeaBattle.getDatabase();
		
		fragments = new ArrayList<>();
		fragments.add(new FragmentUserStatitics());
		fragments.add(new FragmentRanking());
				
		okButton = (Button) findViewById(R.id.btn_ok);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
		
		new GetData().execute(15);
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		private ArrayList<Fragment> fragments;
		
		public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int pos) {
			return fragments.get(pos);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}
	
	private class GetData extends AsyncTask<Integer, Integer, Boolean> {
	
		private ArrayList<User> userList;
		private User user;
		
	@Override
	protected void onPostExecute(Boolean result) {
		
		if(result) {
			((FragmentUserStatitics)fragments.get(0)).setUser(user);
			((FragmentRanking)fragments.get(1)).setUserListAdapter(userList);
		} else 
			Toast.makeText(getApplicationContext(), "B³¹d w po³¹czeniu", Toast.LENGTH_SHORT).show();

	}

	
	@Override
	protected Boolean doInBackground(Integer... params) {

		try {
			db.open();
			
			user = db.getUserByUsername(getApplication().getSharedPreferences(SeaBattle.PREFS_NAME, 0).getString(SeaBattle.PREFS_USERNAME, ""));
			userList = db.getTopUsers(params[0]);
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		
	}
	
}

}
