package pl.marzlat;

public interface Gameplay {
	
	public void sendQueryToOppenent(int x, int y, Gameplay gameplay);
	public void receiveAnswerFromOponent(String aswer);

}
