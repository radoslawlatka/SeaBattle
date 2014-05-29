package pl.marzlat;

public class User {
	
	private int id;
	private String username;
	private int wins;
	private int losses;
	
	public User(int id, String username, int wins, int losses) {
		this.id = id;
		this.username = username;
		this.wins = wins;
		this.losses = losses;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}
	
	

}
