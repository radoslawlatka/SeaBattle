package pl.marzlat.model;


import java.util.List;

import pl.marzlat.Gameplay;
import pl.marzlat.gameplayview.Board;
import pl.marzlat.gameplayview.Square;

/**
 * Created by Marzec on 2014-05-01.
 */
public class Player {

    private static String DOESNT_EXIST = "DOESNT_EXIST";
    private static String MISSED = "MISSED";
    private static String DISCOVERED = "DISCOVERED";
    private static String STRUCK = "STRUCK";
    private static String SUNK = "SUNK";
    private static String LOSE = "LOSE";
    private static String NOT_SUNK = "NOT_SUNK";

    private String name;
    private Area area;
    private List<Ship> ships;
    private int[] toSinkShip = new int[4];

    protected Area opponentsArea = new Area();

    public Player(String name, List<Ship> ships, Area area) {
        this.name = name;
        this.ships = ships;
        this.area = area;
        for (Ship ship : ships)
        {
            switch (ship.getSize())
            {
                case Ship.SMALL:
                    toSinkShip[0]++;
                    break;
                case Ship.MEDIUM:
                    toSinkShip[1]++;
                    break;
                case Ship.LARGE:
                    toSinkShip[2]++;
                    break;
                case Ship.XLARGE:
                    toSinkShip[3]++;
                    break;
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return "DOESNT_EXIST" when field with given coordinates doesn't exist<br />
     *  "STRUCK.SUNK.x.y.xs.ys.size.orientation.LOSE" when on the field stays part of last ship and player lose<br />
     *  "STRUCK.SUNK.x.y" when on the field stays part of ship<br />
     *  "STRUCK.NOT_SUNK.x.y" when on the field stays part of ship<br />
     *  "MISSED.x.y" when this field is empty<br />
     *  "DISCOVERED.x.y" when this field was already discovered
     */
	public void receiveShot(int x, int y, Gameplay gameplay) {
		int fieldState = area.getFieldState(x, y);
		
		if (fieldState == -1) {
			gameplay.receiveAnswerFromOponent(DOESNT_EXIST);
		} else if (fieldState == Field.BUSY) {
			Ship ship = area.getField(x, y).getShip();
			int xs = ship.getX();
			int ys = ship.getY();
			area.setFieldState(x, y, Field.STRUCK);
			if (ship.isSunk()) {
				setMissedAroundSunkShip(area, xs, ys, ship.getSize(),
						ship.getOrientation());
			}
			gameplay.receiveAnswerFromOponent(buildStruckAnswer(x, y));
		} else if (fieldState == Field.FREE) {
			area.setFieldState(x, y, Field.MISSED);
			gameplay.receiveAnswerFromOponent(MISSED + "." + x + "." + y);
		} else {
		gameplay.receiveAnswerFromOponent(DISCOVERED + "." + x + "." + y);
		}
	}

    public String receiveShot(int x, int y)
    {
        int fieldState = area.getFieldState(x, y);
        if (fieldState == -1) {
            return DOESNT_EXIST;
        }
        if (fieldState == Field.BUSY)
        {
            Ship ship = area.getField(x,y).getShip();
            int xs = ship.getX();
            int ys = ship.getY();
            area.setFieldState(x, y, Field.STRUCK);
            if (ship.isSunk())
            {
                setMissedAroundSunkShip(area, xs, ys, ship.getSize(), ship.getOrientation());
            }
            return buildStruckAnswer(x, y);
        }
        else if (fieldState == Field.FREE)
        {
            area.setFieldState(x, y, Field.MISSED);
            return MISSED+"."+x+"."+y;
        }
        return DISCOVERED+"."+x+"."+y;
    }
    
    /**
     * Call only when on field with coordinates (x, y) is there ship
     * @param x
     * @param y
     * @return
     */
    private String buildStruckAnswer(int x, int y) {
        StringBuilder answer = new StringBuilder(STRUCK+".");
        Ship ship = area.getField(x, y).getShip();
        int xs, ys;
        xs = ship.getX();
        ys = ship.getY();
        if (ship.isSunk())
        {
            answer.append(SUNK);
            answer.append("."+x+"."+y);
            answer.append("."+xs+"."+ys);
            answer.append("."+ship.getSize());
            answer.append("."+ship.getOrientation());
            if (isLoser())
                answer.append("."+LOSE);
        }
        else
        {
            answer.append(NOT_SUNK);
            answer.append("."+x+"."+y);
        }
        return answer.toString();
    }

    /**
     *
     * @param answer
     * @return 1 when player hited opponent's ship, but didn't sink
     * <br /> 0 when player hited opponent's ship and sink ship
     * <br /> 2 when player hited opponent's ship and sink ship and wins match
     * <br /> 3 when player didn't hit ship or hited the discovered field or field doesn't exist
     */
    public int receiveOpponentsAnswer(String answer)
    {
        String[] parts = answer.split("\\.");
        if (parts.length == 8 || parts.length == 9)
        {
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);
            int xs = Integer.parseInt(parts[4]);
            int ys = Integer.parseInt(parts[5]);
            int size = Integer.parseInt(parts[6]);
            int orientation = Integer.parseInt(parts[7]);

            opponentsArea.setFieldState(x, y, Field.STRUCK);
            setMissedAroundSunkShip(opponentsArea, xs, ys, size, orientation);
            decrementToSinkShipCounter(size);
            if (parts.length == 9 && parts[8].equals(LOSE))
                return 2;
            return 0;
        }
        else if (parts.length == 4)
        {
            opponentsArea.setFieldState(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Field.STRUCK);
            return 1;
        }
        else if (parts.length == 3)
        {
            if (parts[0].equals(MISSED))
                opponentsArea.setFieldState(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Field.MISSED);
            return 3;
        }
        else
            return 4;
    }

    private void decrementToSinkShipCounter(int size) {
        switch (size)
        {
            case Ship.SMALL:
                toSinkShip[0]--;
                break;
            case Ship.MEDIUM:
                toSinkShip[1]--;
                break;
            case Ship.LARGE:
                toSinkShip[2]--;
                break;
            case Ship.XLARGE:
                toSinkShip[3]--;
                break;
        }
    }

    private void setMissedAroundSunkShip(Area area, int x, int y, int size, int orientation) {
        int i, j, lastX, lastY;
        if (orientation == Ship.LANDSCAPE)
        {
            lastX = x + size;
            lastY = y + 1;
        }
        else //if (orientation == Ship.VERTICAL)
        {
            lastX = x + 1;
            lastY = y + size;
        }
        for (i = y - 1; i <= lastY; i++) {
            for (j = x - 1; j <= lastX; j++) {
                if (i >= 0 && i < Area.SIZE && j >= 0 && j < Area.SIZE) {
                    Field field = area.getField(j, i);
                    if (field.getState() != Field.STRUCK)
                        field.setState(Field.MISSED);
                }
            }
        }
    }

    public boolean isLoser()
    {
        for (Ship ship : ships)
        {
            if (!ship.isSunk())
            {
                return false;
            }
        }
        return true;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public String getName() {
        return name;
    }

    public void drawArea() {
        int i, j;
        System.out.print("  0123456789");
        System.out.println();
        for (i = 0; i < Area.SIZE; i++) {
            System.out.print(i+" ");
            for (j = 0; j < Area.SIZE; j++) {
                Field field = area.getField(j, i);
                if (field.getState() == Field.BUSY)
                    System.out.print("#");
                else if (field.getState() == Field.STRUCK)
                    System.out.print("x");
                else if (field.getState() == Field.MISSED)
                    System.out.print("a");
                else
                    System.out.print("0");
            }
            System.out.println();
        }
    }
    
    public void drawOpponentsArea() {
        int i, j;
        System.out.print("  0123456789");
        System.out.println();
        for (i = 0; i < Area.SIZE; i++) {
            System.out.print(i+" ");
            for (j = 0; j < Area.SIZE; j++) {
                Field field = opponentsArea.getField(j, i);
                if (field.getState() == Field.BUSY)
                    System.out.print("#");
                else if (field.getState() == Field.STRUCK)
                    System.out.print("x");
                else if (field.getState() == Field.MISSED)
                    System.out.print("a");
                else
                    System.out.print("0");
            }
            System.out.println();
        }
    }

    public int[] getToSinkShip() {
        return toSinkShip;
    }

	public Area getArea() {
		return area;
	}

	public Area getOpponentsArea() {
		return opponentsArea;
	}
    
    
}
