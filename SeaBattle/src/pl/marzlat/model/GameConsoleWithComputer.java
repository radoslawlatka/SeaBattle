package pl.marzlat.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Marzec on 2014-05-01.
 */
public class GameConsoleWithComputer {

    public static final int GAME_NOT_STARTED = 0;
    public static final int PLACEMENT = 1;
    public static final int PLAYER_1_ROUND = 2;
    public static final int PLAYER_2_ROUND = 3;
    public static final int END_GAME = 4;

    private Player player1;
    private Player player2;

    private int[] lastHited = null;
    private Random r = new Random();

    private int state = GAME_NOT_STARTED;

    public Player createPlayer1()
    {
        String name = "Mariusz";
        // System.out.println("Podaj imie gracza:");
        List<Ship> ships;
        ships = createShips();

        Area area = new Area();
        area.autoPlacement(ships);
        return new Player(name, ships, area);
    }

    public Player createPlayer2()
    {
        String name = "Android";
        // System.out.println("Podaj imie gracza:");
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

    public void run()
    {
        String answer;
        Scanner scanner = new Scanner(System.in);
        Player player1c, player2c;
        int x, y;
        player1 = createPlayer1();
        player2 = createPlayer2();

        state = PLACEMENT;
        state = PLAYER_1_ROUND;
//        System.out.println(player1.receiveShot(0, 0));
//        System.out.println(player1.receiveShot(1, 0));
//        System.out.println(player1.receiveShot(2, 0));
//        System.out.println(player1.receiveShot(3, 0));
//        System.out.println(player1.receiveShot(4, 0));
//        System.out.println(player1.receiveShot(5, 0));
//        System.out.println(player1.receiveShot(6, 0));
//        System.out.println(player1.receiveShot(7, 0));
//        System.out.println(player1.receiveShot(8, 0));
//        System.out.println(player1.receiveShot(9, 0));
        // TODO gracz komputerowy
//        player1.receiveOpponentsAnswer("STRUCK.SUNK.2.2");
//        player1.receiveOpponentsAnswer("STRUCK.SUNK.3.2");
//        player1.receiveOpponentsAnswer("STRUCK.SUNK.4.2");
//        player1.receiveOpponentsAnswer("STRUCK.SUNK.5.2.2.2.4.10");
//        showStatus(player1);
        while (state != END_GAME)
        {
            if (state == PLAYER_1_ROUND)
            {
                player1c = player1;
                player2c = player2;
            }
            else //if (state == PLAYER_2_ROUND)
            {
                player1c = player2;
                player2c = player1;
            }
            if (state == PLAYER_1_ROUND) {
                String coor;

                showStatus(player1c);
                System.out.println("Miejsce strzału:");
                coor = scanner.next();
                x = Integer.parseInt(coor.substring(0, 1));
                y = Integer.parseInt(coor.substring(1, 2));
            }
            else
            {
                System.out.println("Android wykonuje ruch");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int[] coor = ((ComputerPlayer) player2).typeCoordinatesOfShot();
                x = coor[0];
                y = coor[1];
                System.out.println(coor[0]+" "+coor[1]);
            }
            answer = player2c.receiveShot(x, y);
            int retval = player1c.receiveOpponentsAnswer(answer);
            if (retval == 1 || retval == 0)
            {
                continue;
            }
            else if (retval == 2)
            {
                state = END_GAME;
                System.out.println("Wygrał "+player1c.getName());
            }
            else
            {
                if (state == PLAYER_1_ROUND)
                    state = PLAYER_2_ROUND;
                else if (state == PLAYER_2_ROUND)
                    state = PLAYER_1_ROUND;
            }
        }
    }

    private void showStatus(Player player) {
        System.out.println("Player: "+ player.getName());
        player.drawArea();
        System.out.println("Arena przeciwnika");
        player.drawOpponentsArea();
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args)
    {
        GameConsoleWithComputer game = new GameConsoleWithComputer();
        game.run();
    }
}

/*
Tworzenie gracza dwustopniowe - wpisanie imienia, jezeli zalogowany to jego imie zostaje
potem ulozenie statkow na planszy
 */