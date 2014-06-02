package pl.marzlat.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marzec on 2014-04-20.
 */
public class Ship {

    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int LARGE = 3;
    public static final int XLARGE = 4;

    public static int HORIZONTAL = 10;
    public static int VERTICAL = 11;
    public static int NOT_PLACED = 12;

    private List<Field> fields = new ArrayList<Field>();
    private int size;
    private int orientation;

    public Ship(int size) {
        if (size < SMALL)
            size = SMALL;
        else if (size > XLARGE)
            size = XLARGE;
        this.size = size;
        orientation = NOT_PLACED;
    }

    public void addField(Field field) {
        if (field.getState() == Field.FREE) {
            fields.add(field);
            field.addToShip(this);
        }
    }

    public void removeField(Field field) {
        if (fields.contains(field))
        {
            fields.remove(field);
            field.removeFromShip();
        }
    }


    public void removeAllFields() {
        for (Field field : fields) {
            field.removeFromShip();
        }
        fields.removeAll(fields);
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getSize() {
        return size;
    }

    /**
     * When ship is not placed on area function return true.
     * @return
     */
    public boolean isSunk() {
        for (Field field : fields)
        {
            if (field.getState() != Field.STRUCK)
                return false;
        }
        return true;
    }

    /**
     *
     * @return -1 when ship is not placed on area
     * @return x coordinate of first field occupied by ship
     */
    public int getX() {
        Field field;
        if (fields.size() == 0)
            return -1;
        field = fields.get(0);
        return field.getX();
    }

    /**
     *
     * @return -1 when ship is not placed on area
     * @return y coordinate of first field occupied by ship
     */
    public int getY() {
        Field field;
        if (fields.size() == 0)
            return -1;
        field = fields.get(0);
        return field.getY();
    }
}
