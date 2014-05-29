package pl.marzlat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import pl.marzlat.User;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Database {

	private static final String DTAG = "ShareCostsDatabase";

	private static String URL;
	private static String ADDRESS;
	private static String PORT;
	private static String DATABASE_NAME;
	private static String USER;
	private static String PASSWORD;
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String PREFS_NAME = "seabattleDatabase";

	private final String PREFS_DB_ADDRESS = "db_address";
	private final String PREFS_DB_PORT = "db_port";
	private final String PREFS_DB_NAME = "db_name";
	private final String PREFS_DB_USERNAME = "db_username";
	private final String PREFS_DB_PASSWORD = "db_password";
	
	// table USER
	public static final String USER_TABLE = "users";
	public static final String USER_ID = "id";
	public static final String USER_USERNAME = "username";
	public static final String USER_PASSWORD = "password";
	public static final String USER_EMAIL = "email";
	public static final String USER_PICTRUE = "picture";
	public static final String USER_WINS = "wins";
	public static final String USER_LOSSES = "losses";

	private Connection conn = null;
	private Context context;
	private SharedPreferences prefs;
	private PreparedStatement pStatement;
	private ResultSet rs;

	public Database(Context _context) { 
		context = _context;
		prefs = context.getSharedPreferences(PREFS_NAME, 0);		
		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			Log.e(DTAG, "Failed to load JDBC!");
		}
	}

	public ArrayList<User> getTopUsers(int amount) throws SQLException {
		Log.d(DTAG, "Getting top " + amount +" users...");
		ArrayList<User> users = new ArrayList<>();
		try {			
			pStatement = conn.prepareStatement("SELECT * FROM " + USER_TABLE + " ORDER BY " + USER_WINS + " DESC LIMIT " + amount);

			rs = pStatement.executeQuery();

			while (rs.next())
				users.add(new User(rs.getInt(USER_ID), rs.getString(USER_USERNAME), rs.getInt(USER_WINS), rs.getInt(USER_LOSSES)));

		} finally {
			if( rs != null )
				rs.close();
		}
		return users;	
	}
	
	public User getUserByUsername(String username) throws SQLException {
		Log.d(DTAG, "Getting credits ammount...");
		try {			
			pStatement = conn.prepareStatement("SELECT * FROM " + USER_TABLE + " WHERE " + USER_USERNAME + "=?");
			pStatement.setString(1, username);
			pStatement.executeQuery();

			rs = pStatement.executeQuery();

			if( rs.first() ){
				return new User(rs.getInt(USER_ID), rs.getString(USER_USERNAME), rs.getInt(USER_WINS), rs.getInt(USER_LOSSES));
			} 

		} finally {
			if( rs != null )
				rs.close();
		}
		return new User(-1, "", 0, 0);	
	}

/*	public void setStatus(Debt debt, Status status) throws SQLException {
		Log.d(DTAG, "Setting status...");

		pStatement = conn.prepareStatement("UPDATE " + DEBT_TABLE + " SET "  + 
				DEBT_STATUS_ID + "=?" + 
				" WHERE " + DEBT_ID + "=?");

		pStatement.setInt(1, status.getId());
		pStatement.setInt(2, debt.getId());

		if( pStatement.executeUpdate() == 0 )
			throw new SQLException("Creating flat failed, no rows affected");

	}*/

	public void addUser(String username, String password, String email) throws SQLException {
		Log.d(DTAG, "Inserting new user...");

		try {			
			pStatement = conn.prepareStatement("INSERT INTO " + USER_TABLE + " (" +
					USER_USERNAME + ", " +
					USER_PASSWORD + ", " +
					USER_EMAIL + ") " +
					" VALUES (?, ? ,?)",
					Statement.RETURN_GENERATED_KEYS);

			pStatement.setString(1, username);
			pStatement.setString(2, password);
			pStatement.setString(3, email);

			if( pStatement.executeUpdate() == 0 )
				throw new SQLException("Creating flat failed, no rows affected");

			rs = pStatement.getGeneratedKeys();

			if( rs.next() ) {
				
			} else {
				throw new SQLException("Inserting user failed, no generated key obtained");
			}

		} finally {
			if( rs != null )
				rs.close();
		}
	}
	
	public boolean login(String username, String password) throws SQLException {
		Log.d(DTAG, "Login to database...");

		try {			
			pStatement = conn.prepareStatement("SELECT * FROM " + USER_TABLE + " WHERE " +
					USER_USERNAME + "=? AND " + USER_PASSWORD + "=?" );

			pStatement.setString(1, username);
			pStatement.setString(2, password);

			rs = pStatement.executeQuery();

			if( rs.first() ){
				return true;
			} else {
				return false;
			}

		} finally {
			if( rs != null )
				rs.close();
		}
	}
	
    public void open() throws SQLException {	
    	conn = createConnection();
    }

    public void close() {
		try {
			if(conn != null)
				conn.close();
			Log.d(DTAG, "Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public boolean connectionTest() {
    	prepareDataToLogin();
    	try {
    		Log.d(DTAG, "Connecting to database...");
    			conn = DriverManager.getConnection(URL, USER, PASSWORD);
    		Log.d(DTAG, "Connected");
    		return true;
    	} catch (SQLException e) {
    		Log.d(DTAG, "Connection failed!");
    		e.printStackTrace();
    		return false;
    	} finally {
    		try {
    			conn.close();
    			Log.d(DTAG, "Connection closed");
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
	
    private Connection createConnection() throws SQLException {    	
        Connection connection = null;
		
		prepareDataToLogin();
    	
    	Log.d(DTAG, "Connecting to database...");
    	DriverManager.setLoginTimeout(5);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Log.d(DTAG, "Connected");
        return connection;
    } 	
    
	private void prepareDataToLogin() {
		ADDRESS 		= prefs.getString(PREFS_DB_ADDRESS, "192.168.2.103"); 
    	PORT 			= prefs.getString(PREFS_DB_PORT, "3306");
    	DATABASE_NAME 	= prefs.getString(PREFS_DB_NAME, "seabattle");
    	USER 			= prefs.getString(PREFS_DB_USERNAME, "radek") ;
    	PASSWORD 		= prefs.getString(PREFS_DB_PASSWORD, "asd123");
    	
    	URL = "jdbc:mysql://" + ADDRESS + ":" + PORT + "/" + DATABASE_NAME + "?useUnicode=true&characterEncoding=utf8";
    	
    	System.out.println(URL);
    	System.out.println(USER + " " + PASSWORD);
	} 	
}
