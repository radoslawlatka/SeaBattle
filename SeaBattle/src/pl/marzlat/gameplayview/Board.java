package pl.marzlat.gameplayview;

import java.util.ArrayList;

import android.graphics.Canvas;

public class Board {

	private int n;
	private float x;
	private float y;
	private float width;

	private float a;
	private ArrayList<Square> squares;

	public Board(int n, float x, float y, float width) {
		int i, j;
		this.n = n;
		this.x = x;
		this.y = y;
		this.width = width;
		a = width / 10f;

		squares = new ArrayList<Square>();
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				squares.add(new Square(i, j, (j * a) + x, (i * a) + y, a));
			}
		}
	}

	public void draw(Canvas c) {
		for (Square s : squares)
			s.draw(c);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return null when on position (x, y) there isn't Square
	 */
	public Square getSquaresOnPosition(float x, float y) {
		for (Square s : squares) {
			if (clickedSquare(s, x, y))
				return s;
		}
		return null;
	}

	private boolean clickedSquare(Square s, float x, float y) {
		if (s.getX() < x && x < s.getXb() && s.getY() < y && y < s.getYb())
			return true;
		return false;
	}
}
