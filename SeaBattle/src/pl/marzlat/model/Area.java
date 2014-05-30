package pl.marzlat.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.util.Log;

/**
 * Created by Marzec on 2014-04-20.
 */
public class Area {

    public static final int SIZE = 10;
    private HashMap<String, Field> fields = new HashMap<String, Field>();
    private List<Ship> ships = new ArrayList<Ship>();

    public Area() {
        int i, j;
        for (i = 0; i < SIZE; i++)
            for (j = 0; j < SIZE; j++)
                fields.put(i+" "+j, new Field(i, j));
    }

    public void placeShip(Ship ship, int orientation, int x, int y) throws InappropriateLocationException, ShipsAdjoinException {
        int lastX, lastY;
        int i, j;
        if (orientation == Ship.LANDSCAPE)
        {
            lastX = x + ship.getSize() - 1;
            lastY = y;
        }
        else
        {
            lastX = x;
            lastY = y + ship.getSize() - 1;
        }
        if (x < 0 || lastX >= SIZE || y < 0 || lastY >= SIZE)
            throw new InappropriateLocationException();

        if (!fieldsAroundFree(x ,y, lastX, lastY))
            throw new ShipsAdjoinException();

        for (i = x; i <= lastX; i++)
            for (j = y; j <= lastY; j++)
            {
                ship.addField(getField(i, j));
            }
        ship.setOrientation(orientation);
        ships.add(ship);
    }

    /**
     *
     * @param x
     * @param y
     * @return null, when field hasn't ship or field doesn't exist
     */
    public Ship removeShipFromArea(int x, int y) {
        Field field = getField(x, y);
        Ship ship;
        if (field == null)
            return null;
        ship = field.getShip();
        if (ship == null)
            return null;

        ship.removeAllFields();
        ship.setOrientation(Ship.NOT_PLACED);
        return ship;
    }

    public void autoPlacement(List<Ship> ships) {
        Random r = new Random();
        int i, j;
        int orientation;
        int exceptionCount = 0;
        int exceptionMax = 20;
        
        removeAllShips();
        
        for (Ship ship : ships) {
            while (true)
            {
                try
                {
                    orientation = r.nextInt(2);

                    i = r.nextInt(SIZE);
                    j = r.nextInt(SIZE);
                    if (orientation == 0)
                        orientation = Ship.LANDSCAPE;
                    else
                        orientation = Ship.VERTICAL;

                    placeShip(ship, orientation, i, j);
                    break;
                }
                catch (Exception e)
                {
                	exceptionCount++;
                	if (exceptionCount == exceptionMax)
                	{
	                	autoPlacement(ships);
	                	return;
                	}
//                	Log.d("Area -auto", "Zakleszczenie");
                }
            }
        }
    }

    private boolean fieldsAroundFree(int x, int y, int lastX, int lastY) {
        int i, j;
        lastX += 1;
        lastY += 1;
        for (i = y - 1; i <= lastY; i++) {
            for (j = x - 1; j <= lastX; j++) {
                if (i >= 0 && i < SIZE && j >= 0 && j < SIZE) {
                    if (getField(j, i).getState() == Field.BUSY)
                        return false;
                }

            }
        }
        return true;
    }

    /**
     *
     * @param x
     * @param y
     * @return null, when field doesn't exist
     */
    public Field getField(int x, int y) {
        if ((x < SIZE && x >= 0) && (y < SIZE && y >= 0))
            return fields.get(x+" "+y);
        return null;
    }

    /**
     *
     * @param x
     * @param y
     * @param state
     * @return 0 when changing finished with successful
     * @return -1 when field with given coordinates doesn't exist
     * @return -2 when field is busy by ship and new state is Failed.MISSED or Field.FREE,
     * to change status, you must to remove this ship
     */
    public int setFieldState(int x, int y, int state)
    {
        Field field = fields.get(x+" "+y);
        if (field == null)
            return -1;
        if (field.getShip() != null && (state == Field.MISSED || state == Field.FREE))
            return -2;
        field.setState(state);
        return 0;
    }

    /**
     *
     * @param x
     * @param y
     * @return 3 when changing finished with successful
     * @return -1 when field with given coordinates doesn't exist
     */
    public int getFieldState(int x, int y)
    {
        Field field = fields.get(x+" "+y);
        if (field == null)
            return -1;
        return field.getState();
    }
    
    public void removeAllShips()
    {
    	for (Ship s : ships)
    	{
    		s.removeAllFields();
    		s.setOrientation(Ship.NOT_PLACED);
    	}
    }
}
