package pl.marzlat;

import java.sql.SQLException;

import pl.marzlat.database.Database;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class LoginActivity extends Activity {

	private Context context;
	private Database db;
	private EditText usernameEdit, passwordEdit;
	private Button loginButton, cancelButton, createAccountButton;
	private TextView forgetPassword;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		context = getApplicationContext();
		db = new Database(context);

		usernameEdit = (EditText) findViewById(R.id.edit_username);
		passwordEdit = (EditText) findViewById(R.id.edit_password);
		loginButton = (Button) findViewById(R.id.btn_login);
		cancelButton = (Button) findViewById(R.id.btn_cancel);
		createAccountButton = (Button) findViewById(R.id.btn_create_account);
		forgetPassword = (TextView) findViewById(R.id.txt_remind_password);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);

		initListeners();
	}

	private void initListeners() {

		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(usernameEdit.getText().toString().equals("") || passwordEdit.getText().toString().equals(""))
					showToast(getApplicationContext(), getString(R.string.toast_empty_fields));				
				else {

					new Login().execute(usernameEdit.getText().toString(), passwordEdit.getText().toString());

					//finish();
				}
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		forgetPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(v.getContext());
				dialog.setContentView(R.layout.dialog_remind_password);
				dialog.setTitle(getString(R.string.label_remind_passord));

				final EditText emailEdit = (EditText) dialog.findViewById(R.id.email);

				Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
				Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

				dialog.show();

				okButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						// PRZYPOMNIENIE HAS£A
						if(emailEdit.getText().toString().equals(""))
							showToast(getApplicationContext(), getString(R.string.toast_empty_fields));						
						else if( !isValidEmail(emailEdit.getText().toString().trim()) )
							showToast(getApplicationContext(), getString(R.string.toast_invalid_email));
						else {
							showToast(getApplicationContext(), "Mail bêdzie ws³any jak zrobimy wysy³anie");
							dialog.dismiss();
							finish();
						}
					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		});

		createAccountButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(v.getContext());
				dialog.setContentView(R.layout.dialog_create_account);
				dialog.setTitle(getString(R.string.label_create_account));

				final EditText usernameEdit = (EditText) dialog.findViewById(R.id.edit_username);
				final EditText passEdit = (EditText) dialog.findViewById(R.id.edit_password);
				final EditText confPassEdit = (EditText) dialog.findViewById(R.id.edit_confirm_password);
				final EditText emailEdit = (EditText) dialog.findViewById(R.id.edit_email);

				Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
				Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

				dialog.show();

				okButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if(usernameEdit.getText() == null || passEdit.getText() == null
								|| confPassEdit.getText() == null || emailEdit.getText() == null)
							showToast(getApplicationContext(), getString(R.string.toast_empty_fields));
						else if( !passEdit.getText().toString().equals(confPassEdit.getText().toString()))

							showToast(getApplicationContext(), getString(R.string.toast_password_not_match));

						else if( !isValidEmail(emailEdit.getText().toString()) )
							showToast(getApplicationContext(), getString(R.string.toast_invalid_email));

						else {
							
							new CreateAccount(dialog).execute(usernameEdit.getText().toString(),
														passEdit.getText().toString(),
														emailEdit.getText().toString());
						}

					}

				});

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		});

	}

	private void showToast(Context context, String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}

	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	private class Login extends AsyncTask<String, Integer, Boolean>{

		private String username, password;
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);

			if(result) {
				showToast(context, "Zalogowano jako " + username);
				SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(SeaBattle.PREFS_NAME, 0).edit();
				
				editor.putString(SeaBattle.PREFS_USERNAME, username);
				editor.putString(SeaBattle.PREFS_PASSWORD, password);
				
				editor.commit();
				
				finish();
			}
			else
				showToast(context, "Logowanie nie powiod³o siê");
		}

		@Override
		protected void onPreExecute() {
			
			
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Boolean doInBackground(String... params) {

			username = params[0];
			password = Sha256.encode(params[0],params[1], 500);
			try {
				db.open();

				return db.login(username, password);

			} catch (SQLException e) { 
				Log.e("Database", e.toString());
				return false;
			} finally {
				db.close();
			}

		}

	}

	private class CreateAccount extends AsyncTask<String, Integer, Boolean>{

		private Dialog dialog;
		
		public CreateAccount(Dialog dialog) {
			this.dialog = dialog;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			dialog.findViewById(R.id.progressBar).setVisibility(View.GONE);
			
			if(result) {
				showToast(context, "Konto zosta³o utworzone");
				dialog.dismiss();
				finish();
			} else {
				showToast(context, "Nie mo¿na utworzyæ konta");
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				db.open();
				db.addUser(params[0], Sha256.encode(params[0],params[1], 500),  params[2]);
				return true;

			} catch (MySQLIntegrityConstraintViolationException e) {
				showToast(context, "U¿ytkownik o takiej nazwie ju¿ istnieje");
				return false;
			} catch (SQLException e) { 
				Log.e("Database", e.toString());
				return false;
			} finally {
				db.close();
			}

		}

		
	}
}
