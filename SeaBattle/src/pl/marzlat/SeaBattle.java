package pl.marzlat;

import pl.marzlat.database.Database;
import android.app.Application;

public class SeaBattle extends Application {

	public static final String PREFS_NAME = "seabattle";
	public static final String PREFS_USERNAME = "username";
	public static final String PREFS_PASSWORD = "password";
	
	private static SeaBattle instance;
	private static Database database;
	
	
	public SeaBattle() {
		instance = this;
	}
	
	public static SeaBattle getInstance() {
	    if (instance == null)
	        synchronized (SeaBattle.class) {
	            if (instance == null)
	                instance = new SeaBattle();
	        }
	    return instance;
	}

	public static Database getDatabase() {
		if (database == null)
			synchronized (SeaBattle.class) {
				if (database == null)
					database = new Database(getInstance().getApplicationContext());
			}
		return database;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	}

}
