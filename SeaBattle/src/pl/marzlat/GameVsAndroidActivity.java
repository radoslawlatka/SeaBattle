package pl.marzlat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.marzlat.gameplayview.BoardView;
import pl.marzlat.model.Area;
import pl.marzlat.model.ComputerPlayer;
import pl.marzlat.model.Player;
import pl.marzlat.model.Ship;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameVsAndroidActivity extends Activity {

    public static final int GAME_NOT_STARTED = 0;
    public static final int PLACEMENT = 1;
    public static final int PLAYER_1_ROUND = 2;
    public static final int PLAYER_2_ROUND = 3;
    public static final int END_GAME = 4;

    private Player player1;
    private Player player2;
    
	private BoardView boardView;
	private Button buttonReset;
	private Button buttonAuto;
	private Button buttonDone;
	private TextView textCurrPlayer;
	
	private List<Ship> ships = createShips();
	private Area area = new Area();
	
    private Random r = new Random();

    private int shipNumberToPlace;
    private int state = GAME_NOT_STARTED;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_vs_android);
		
		boardView = (BoardView) findViewById(R.id.board_view);
		buttonReset = (Button) findViewById(R.id.button_reset);
		buttonAuto = (Button) findViewById(R.id.button_auto);
		buttonDone = (Button) findViewById(R.id.button_done);
		textCurrPlayer = (TextView) findViewById(R.id.currentPlayer);
		
		player1 = new Player("Mariusz", ships, area);
		textCurrPlayer.setText(player1.getName());
		Log.d("GameVsAndroid", "Created player");
		
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
				if (shipNumberToPlace == 0)
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
		
		Log.d("GameVsAndroid", "Add listeners");
		
		state = PLACEMENT;
		shipNumberToPlace = ships.size();
		
		player2 = createAndroidPlayer();
		Log.d("GameVsAndroid", "Create Android player");
		
		//TODO zrobic przycisk reset, zrobic blokowanie planszy
		// zrobic zczytywanie z planszy
		
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
}