package pl.marzlat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.marzlat.gameplayview.BoardView;
import pl.marzlat.gameplayview.PlacementMenu;
import pl.marzlat.model.Area;
import pl.marzlat.model.ComputerPlayer;
import pl.marzlat.model.Player;
import pl.marzlat.model.Ship;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameVsAndroidActivity extends Activity implements Gameplay {

    public static final int GAME_NOT_STARTED = 0;
    public static final int PLACEMENT = 1;
    public static final int PLAYER_1_ROUND = 2;
    public static final int PLAYER_2_ROUND = 3;
    public static final int END_GAME = 4;

    private Player player1;
    private ComputerPlayer player2;
    private Gameplay gameplay;
    
	private BoardView boardView;
	private PlacementMenu placementMenu;
	private Button buttonReset;
	private Button buttonAuto;
	private Button buttonDone;
	private TextView textCurrPlayer;

	private List<Ship> ships = createShips();
	private Area area = new Area();

    private int shipNumberToPlace;
    private int state = GAME_NOT_STARTED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_vs_android);

		gameplay = this;
		
		boardView = (BoardView) findViewById(R.id.board_view);
		placementMenu = (PlacementMenu) findViewById(R.id.placement_menu);
		
		buttonReset = (Button) findViewById(R.id.button_reset);
		buttonAuto = (Button) findViewById(R.id.button_auto);
		buttonDone = (Button) findViewById(R.id.button_done);
		
		textCurrPlayer = (TextView) findViewById(R.id.currentPlayer);

		player1 = new Player("Mariusz", ships, area);
		textCurrPlayer.setText(player1.getName());
		Log.d("GameVsAndroid", "Created player");

		initListeners();

		Log.d("GameVsAndroid", "Add listeners");

		state = PLACEMENT;
		shipNumberToPlace = ships.size();

		player2 = (ComputerPlayer) createAndroidPlayer();
		Log.d("GameVsAndroid", "Create Android player");
	}



	private void initListeners() {
		buttonReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				player1.getArea().removeAllShips();
				boardView.setBoardAndDraw(player1.getArea());
				shipNumberToPlace = ships.size();
				boardView.setBlocked(false);
			}
		});

		buttonAuto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				area.autoPlacement(ships);
				player1.drawArea();
				boardView.setBoardAndDraw(player1.getArea());
				shipNumberToPlace = 0;
				boardView.setBlocked(true);
			}
		});

		buttonDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (shipNumberToPlace == 0 && state == PLACEMENT)
				{
					buttonReset.setEnabled(false);
					buttonAuto.setEnabled(false);
					buttonDone.setEnabled(false);
					boardView.setBoardAndDraw(player1.getOpponentsArea());
					boardView.setBlocked(false);
					state = PLAYER_1_ROUND;
				}
				else
				{
					Toast.makeText(getApplicationContext(), 
							R.string.toast_all_ships_not_placed , Toast.LENGTH_SHORT).show();
				}
			}
		});

		boardView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int x = boardView.getClickedX();
				int y = boardView.getClickedY();
				String opponentsAnswer;
				int retval;
				Log.d("GameVsAndroidClass", x+" "+y);
				if (x == -1 && y == -1)
					return;
				if (boardView.isBlocked() == false)
				{
					if (state == PLAYER_1_ROUND)
					{
						boardView.setBlocked(true);

						opponentsAnswer = player2.receiveShot(x, y);
						Log.d("GameVsAndroidClass", opponentsAnswer);
						retval = player1.receiveOpponentsAnswer(opponentsAnswer);
						if (retval == 1 || retval == 0)
						{
							state = PLAYER_1_ROUND;
							boardView.setBoardAndDraw(player1.getOpponentsArea());
							boardView.setBlocked(false);
						}
						else if (retval == 2)
						{
							state = END_GAME;
							boardView.setBoardAndDraw(player1.getOpponentsArea());
							Toast.makeText(getApplicationContext(), 
									R.string.toast_winner_is + " " + player1.getName(),
									Toast.LENGTH_LONG).show();
							boardView.setBlocked(true);
						}
						else if (retval == 3)
						{
							state = PLAYER_2_ROUND;
							textCurrPlayer.setText(player2.getName());
							boardView.setBoardAndDraw(player1.getOpponentsArea());
							boardView.setBlocked(true);
							new AndroidPlayerRound().execute();
						}
					}
					else if (state == PLACEMENT)
					{

					}
				}
			}
		});
	}

    

	@Override
	public void sendQueryToOppenent(int x, int y, Gameplay gameplay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveAnswerFromOponent(String opponentsAnswer) {
		Log.d("GameVsAndroidClass", "####"+opponentsAnswer);
		int retval;

			if (state == PLAYER_1_ROUND) {
				
				retval = player1.receiveOpponentsAnswer(opponentsAnswer);
				
				if (retval == 1 || retval == 0) {
					state = PLAYER_1_ROUND;
					boardView.setBoardAndDraw(player1.getOpponentsArea());
					boardView.setBlocked(false);
				} else if (retval == 2) {
					state = END_GAME;
					boardView.setBoardAndDraw(player1.getOpponentsArea());
					Toast.makeText(getApplicationContext(), "Wygrana",
							Toast.LENGTH_LONG).show();
					boardView.setBlocked(true);
				} else if (retval == 3) {
					state = PLAYER_2_ROUND;
					textCurrPlayer.setText(player2.getName());
					boardView.setBoardAndDraw(player1.getOpponentsArea());
					boardView.setBlocked(true);
					new AndroidPlayerRound().execute();
				}
			
		}
	}
	
    public Player createAndroidPlayer()
    {
        String name = "Android";
        List<Ship> ships;
        ships = createShips();
        Area area = new Area();
        area.autoPlacement(ships);
        return new ComputerPlayer(name, ships, area);
    }
    
    private List<Ship> createShips() {
        List<Ship> ships;
        ships = new ArrayList<Ship>();
        for (int i = 0; i < 4; i++) {
            ships.add(new Ship(Ship.SMALL));
        }
        for (int i = 0; i < 3; i++) {
            ships.add(new Ship(Ship.MEDIUM));
        }
        for (int i = 0; i < 2; i++) {
            ships.add(new Ship(Ship.LARGE));
        }
        for (int i = 0; i < 1; i++) {
            ships.add(new Ship(Ship.XLARGE));
        }
        return ships;
    }

    @Override
    public void onBackPressed()
    {
    	final Dialog dialog = new Dialog(this);
    	dialog.setContentView(R.layout.dialog_on_back_pressed);
    	dialog.setTitle("Wyjœcie");
    	    	
    	final Button buttonYes = (Button) dialog.findViewById(R.id.button_yes);
    	final Button buttonNo = (Button) dialog.findViewById(R.id.button_no);
    	
    	dialog.show();

    	buttonYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
    	
    	buttonNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
    	
	}
    
    private class AndroidPlayerRound extends AsyncTask<Void, Void, Void>
    {

		@Override
		protected Void doInBackground(Void... arg0) {
			String opponentsArea;
			int retval;
			sleep(500);
			showPlayerArea();
			sleep(300);
			int[] coor = player2.typeCoordinatesOfShot();
			opponentsArea = player1.receiveShot(coor[0], coor[1]);
			showPlayerArea();
			retval = player2.receiveOpponentsAnswer(opponentsArea);
			sleep(300);

			if (retval == 1 || retval == 0)
			{
				state = PLAYER_1_ROUND;
				showPlayerArea();
				return doInBackground((Void[]) null);
			}
			else if (retval == 2)
			{
				state = END_GAME;
//				boardView.setBoardAndDraw(player1.getOpponentsArea());
				showWinnersMassage();
				boardView.setBlocked(true);
			}
			else if (retval == 3)
			{
				state = PLAYER_1_ROUND;
				setCurrentPlayerName();
				showOpponentsArea();
				boardView.setBlocked(false);
			}
			return null;
		}
		
		private void showWinnersMassage()
		{
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), R.string.toast_android_wins, Toast.LENGTH_LONG).show();
				}
			});			
		}

		private void setCurrentPlayerName()
		{
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					textCurrPlayer.setText(player1.getName());
				}
			});			
		}

		private void showPlayerArea()
		{
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					boardView.setBoardAndDraw(player1.getArea());
				}
			});			
		}

		private void showOpponentsArea()
		{
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					boardView.setBoardAndDraw(player1.getOpponentsArea());
				}
			});			
		}

		private void sleep(long time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
    	
    }

}
