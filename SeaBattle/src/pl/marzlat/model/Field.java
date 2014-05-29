package pl.marzlat.model;


/**
 * Created by Marzec on 2014-04-20.
 */
public class Field {

    public static int FREE = 0;
    public static int BUSY = 1;
    public static int STRUCK = 2;
    public static int MISSED = 3;

    private int x;
    private int y;
    private int state = FREE;
    private Ship ship = null;

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addToShip(Ship ship)
    {
        this.ship = ship;
        state = BUSY;
    }

    public void removeFromShip()
    {
        if (ship != null)
        {
            ship = null;
            state = FREE;
        }
    }

    public Ship getShip() {
        return ship;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
