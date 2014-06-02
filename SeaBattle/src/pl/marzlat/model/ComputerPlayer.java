package pl.marzlat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Marzec on 2014-05-07.
 */
public class ComputerPlayer extends Player {

    private Random r = new Random();
    private List<int[]> hitedShip = new ArrayList<int[]>();
    private boolean huntForShip = false;

    public ComputerPlayer(String name, List<Ship> ships, Area area) {
        super(name, ships, area);
    }

    public int[] typeCoordinatesOfShot()
    {
        int[] coor;
        if (!huntForShip && hitedShip.size() == 0)
        {
            coor = randomCoordinates();
        }
        else
        {
            if (hitedShip.size() == 1)
            {
                int[] hitedField = hitedShip.get(0);
                coor = randomAroundPoint(hitedField[0], hitedField[1]);
            }
            else // if (hitedShip.size() > 1)
            {
                coor = randomAroundHitedShip();
            }
        }
        return coor;
    }

    private int[] randomAroundHitedShip() {
        int orientation = shipsOrientation();
        if (orientation == Ship.VERTICAL)
            return randomFromVerticalDirection();
        return randomFromLandscapeDirection();
    }

    private int[] randomFromLandscapeDirection() {
        int y = hitedShip.get(0)[1];
        int minX = minX();
        int maxX = maxX();
        if (isExistingAndFree(minX-1, y))// && r.nextInt(2) == 1)
        {
            return new int[] {minX-1, y};
        }
        else if (isExistingAndFree(maxX+1, y))
        {
            return new int[] {maxX+1, y};
        }
        return randomCoordinates();
    }

    private int[] randomFromVerticalDirection() {
        int x = hitedShip.get(0)[0];
        int minY = minY();
        int maxY = maxY();
        if (isExistingAndFree(x, minY-1))// && r.nextInt(2) == 1)
        {
            return new int[] {x, minY-1};
        }
        else if (isExistingAndFree(x, maxY+1))
        {
            return new int[] {x, maxY+1};
        }
        return randomCoordinates();
    }

    private int maxY() {
        int max = -1;
        for (int[] coor : hitedShip) {
            if (coor[1] > max)
                max = coor[1];
        }
        return max;
    }

    private int minY() {
        int min = Area.SIZE;
        for (int[] coor : hitedShip) {
            if (coor[1] < min)
                min = coor[1];
        }
        return min;
    }

    private int maxX() {
        int max = -1;
        for (int[] coor : hitedShip) {
            if (coor[0] > max)
                max = coor[0];
        }
        return max;
    }

    private int minX() {
        int min = Area.SIZE;
        for (int[] coor : hitedShip) {
            if (coor[0] < min)
                min = coor[0];
        }
        return min;
    }

    private int shipsOrientation() {
        int x1 = hitedShip.get(0)[0];
        int x2 = hitedShip.get(1)[0];
        if (x1 == x2)
            return Ship.VERTICAL;
        return Ship.HORIZONTAL;
    }

    private int[] randomAroundPoint(int x, int y) {
        List<int[]> freeFieldsAround = new ArrayList<int[]>();
        if (isExistingAndFree(x+1, y)) {
            freeFieldsAround.add(new int[]{x+1, y});
        }
        if (isExistingAndFree(x, y+1)) {
            freeFieldsAround.add(new int[]{x, y+1});
        }
        if (isExistingAndFree(x-1, y)) {
            freeFieldsAround.add(new int[]{x-1, y});
        }
        if (isExistingAndFree(x, y-1)) {
            freeFieldsAround.add(new int[]{x, y-1});
        }

        if (freeFieldsAround.size() == 0)
            return randomCoordinates();
        else
            return freeFieldsAround.get(r.nextInt(freeFieldsAround.size()));
    }

    private boolean isExistingAndFree(int x, int y) {
        if ((x >= 0 && x < Area.SIZE) && (y >= 0 && y < Area.SIZE))
            if (opponentsArea.getFieldState(x, y) == Field.FREE)
                return true;
        return false;
    }

    private int[] randomCoordinates() {
        int[] coor = new int[2];
        do {
            coor[0] = r.nextInt(Area.SIZE);
            coor[1] = r.nextInt(Area.SIZE);
        } while (opponentsArea.getFieldState(coor[0], coor[1]) != Field.FREE);
        return coor;
    }

    @Override
    public int receiveOpponentsAnswer(String answer) {
        int result = super.receiveOpponentsAnswer(answer);
        if (result == 0)
        {
            huntForShip = false;
            hitedShip.clear();
        }
        else if (result == 1) {
            String[] parts = answer.split("\\.");
            int[] last = new int[] {
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3])
            };
            hitedShip.add(last);
            huntForShip = true;
        }
        return result;
    }
}
