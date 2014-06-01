package pl.marzlat;

import java.util.List;
import java.util.Random;

import pl.marzlat.gameplayview.BoardView;
import pl.marzlat.model.Area;
import pl.marzlat.model.ComputerPlayer;
import pl.marzlat.model.Player;
import pl.marzlat.model.Ship;
import pl.marzlat.model.WifiClientPlayer;
import pl.marzlat.model.WifiServerPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameplayFragment extends Fragment implements Gameplay {

	public static final int GAME_NOT_STARTED = 0;
	public static final int PLACEMENT = 1;
	public static final int PLAYER_1_ROUND = 2;
	public static final int PLAYER_2_ROUND = 3;
	public static final int END_GAME = 4;

	private Player player1;
	private Player player2;
	private Gameplay gameplay;

	private BoardView boardView;
	private Button buttonReset;
	private Button buttonAuto;
	private Button buttonDone;
	private TextView textCurrPlayer;

	public GameplayFragment() {
		gameplay = this;
	}

	private List<Ship> ships;
	private Area area = new Area();

	private Random r = new Random();

	private int shipNumberToPlace;
	private int state = GAME_NOT_STARTED;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gameplay, container, false);

		boardView = (BoardView) view.findViewById(R.id.board_view);
		buttonReset = (Button) view.findViewById(R.id.button_reset);
		buttonAuto = (Button) view.findViewById(R.id.button_auto);
		buttonDone = (Button) view.findViewById(R.id.button_done);
		textCurrPlayer = (TextView) view.findViewById(R.id.currentPlayer);

		textCurrPlayer.setText(player1.getName());

		buttonReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				player1.getArea().removeAllShips();
				
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
				if (shipNumberToPlace == 0 && state == PLACEMENT) {
					buttonReset.setEnabled(false);
					buttonAuto.setEnabled(false);
					buttonDone.setEnabled(false);
					boardView.setBoardAndDraw(player1.getOpponentsArea());
					boardView.setBlocked(false);
					state = PLAYER_1_ROUND;
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.toast_all_ships_not_placed,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		boardView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int x = boardView.getClickedX();
				int y = boardView.getClickedY();

				Log.d("Gameplay", "Click: " + x + " " + y);
				if (x == -1 && y == -1)
					return;
				if (boardView.isBlocked() == false) {
					if (state == PLAYER_1_ROUND) {
						sendQueryToOppenent(x, y, gameplay);
					} else if (state == PLACEMENT) {

					}
				}
			}
		});

		Log.d("Gameplay", "Add listeners");

		state = PLACEMENT;
		shipNumberToPlace = ships.size();

		// TODO zrobic przycisk reset, zrobic blokowanie planszy
		// zrobic zczytywanie z planszy

		// Lubie placki

		return view;
	}

	@Override
	public void sendQueryToOppenent(int x, int y, Gameplay gameplay) {
		Log.d("Gameplay", "Send query to opponent: " + x + " " + y);
		boardView.setBlocked(true);

		boardView.setBoardAndDraw(player1.getOpponentsArea());
		player2.receiveShot(x, y, gameplay);
	}

	@Override
	public void receiveAnswerFromOpponent(final String opponentsAnswer) {
		Log.d("Gameplay", "Receive answer from opponent: " + opponentsAnswer);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				int retval;

				if (state == PLAYER_1_ROUND) {

					boardView.setBoardAndDraw(player1.getArea());
					
					retval = player1.receiveOpponentsAnswer(opponentsAnswer, gameplay);
					
					Log.d("Gameplay", "Retval: " + retval);
					if (retval == 1 || retval == 0) {
						state = PLAYER_1_ROUND;
						boardView.setBoardAndDraw(player1.getArea());
						boardView.setBlocked(false);
					} else if (retval == 2) {
						state = END_GAME;
						boardView.setBoardAndDraw(player1.getOpponentsArea());
						Toast.makeText(getActivity().getApplicationContext(),
								"Wygrana", Toast.LENGTH_LONG).show();
						boardView.setBlocked(true);
					} else if (retval == 3) {
						state = PLAYER_2_ROUND;
						textCurrPlayer.setText(player2.getName());
						boardView.setBoardAndDraw(player1.getArea());
						boardView.setBoardAndDraw(player1.getOpponentsArea());
						boardView.setBlocked(true);
						if (player2.getClass() == ComputerPlayer.class)
							new AndroidPlayerRound().execute();
					}


				}

			}
		});
		
	}

	@Override
	public void receiveQueryFromOpponent(final int x, final int y) {
		Log.d("Gameplay", "Receive query from opponent: " + x + " " + y);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
				String answer = player1.receiveShot(x, y);
				sendAnswerToOpponent(answer);
				

				
				

				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void sendAnswerToOpponent(final String answer) {
		Log.d("Gameplay", "Send answer to opponent: " + answer);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
				player2.receiveOpponentsAnswer(answer, gameplay);
				
				boardView.setBlocked(false);
				textCurrPlayer.setText(player1.getName());
			}
		});
	}
	
	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
		this.area = this.player1.getArea();
		this.ships = player1.getShips();
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
		if(this.player2.getClass() == WifiServerPlayer.class){
			((WifiServerPlayer)player2).setGameplay(this);
		}
		if(this.player2.getClass() == WifiClientPlayer.class){
			((WifiClientPlayer)player2).setGameplay(this);
		}
	}	
	
	private class AndroidPlayerRound extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			String opponentsArea;
			int retval;
			sleep(300);
			showPlayerArea();
			sleep(500);
			int[] coor = ((ComputerPlayer) player2).typeCoordinatesOfShot();
			opponentsArea = player1.receiveShot(coor[0], coor[1]);
			showPlayerArea();
			retval = player2.receiveOpponentsAnswer(opponentsArea, gameplay);
			sleep(300);

			if (retval == 1 || retval == 0) {
				state = PLAYER_1_ROUND;
				showPlayerArea();
				return doInBackground((Void[]) null);
			} else if (retval == 2) {
				state = END_GAME;
				
				showWinnersMassage();
				boardView.setBlocked(true);
			} else if (retval == 3) {
				state = PLAYER_1_ROUND;
				setCurrentPlayerName();
				showOpponentsArea();
				boardView.setBlocked(false);
			}
			return null;
		}

		private void showWinnersMassage() {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getActivity().getApplicationContext(),
							"Wygrana Androida", Toast.LENGTH_LONG).show();
				}
			});
		}

		private void setCurrentPlayerName() {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					textCurrPlayer.setText(player1.getName());
				}
			});
		}

		private void showPlayerArea() {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					boardView.setBoardAndDraw(player1.getArea());
				}
			});
		}

		private void showOpponentsArea() {
			getActivity().runOnUiThread(new Runnable() {
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