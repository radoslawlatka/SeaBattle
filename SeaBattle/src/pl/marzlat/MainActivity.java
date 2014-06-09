package pl.marzlat;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FragmentMenu()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("onActivityResult", "request=" + requestCode + " result=" + resultCode);
		if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
			((FragmentSelectGame)getFragmentManager()
					.findFragmentById(R.id.container))
					.receiveMacAddress(data.getExtras().getString("device_address"));
		}
	}

	@Override
	public void onBackPressed() {
		Fragment fragment = getFragmentManager().findFragmentById(
				R.id.container);

		if (fragment instanceof FragmentGameplay) {

			final Dialog dialog = new Dialog(this);
			dialog.getContext().setTheme(android.R.style.Theme_Dialog);
			dialog.setContentView(R.layout.dialog_on_back_pressed);
			dialog.setTitle(getString(R.string.btn_exit));

			final Button buttonYes = (Button) dialog.findViewById(R.id.button_yes);
			final Button buttonNo = (Button) dialog.findViewById(R.id.button_no);

			dialog.show();

			buttonYes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					onBackPressed2();
				}
			});

			buttonNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

		} else {
			super.onBackPressed();
		}
	}

	private void onBackPressed2() {
		super.onBackPressed();
	}

}
