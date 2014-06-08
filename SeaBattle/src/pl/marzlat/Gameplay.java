package pl.marzlat;

public interface Gameplay {
	
	public void sendQueryToOppenent(int x, int y, Gameplay gameplay);
	public void receiveAnswerFromOpponent(String aswer);
	
	public void receiveQueryFromOpponent(int x, int y);
	public void sendAnswerToOpponent(String answer);

}
