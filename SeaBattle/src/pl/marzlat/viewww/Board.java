package pl.marzlat.viewww;

import java.util.ArrayList;

public class Board {

	private int n;
	private float x;
	private float y;
	private float width;
	private float height;

	private float l;
	private float a;
	private ArrayList<Square> squares;

	public Board(int n, float x, float y, float width, float height) {
		int i, j;
		this.n = n;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		l = width / 110f;
		a = 10 / 110f * width;

		squares = new ArrayList<Square>();
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				squares.add(new Square(((j + 1) * l + j * a)+x, ((i + 1) * l + i * a)+y, a));
			}
		}
	}

	public void draw(float[] mvpMatrix) {
		for(Square s : squares)
			s.draw(mvpMatrix);
	}

	public Square getSquare(float _x, float _y) {

		System.out.println("x = " + _x + "  y=" + _y);
		
		for(Square s : squares) {
			if(s.getX()>=_x && s.getX()<_x+a
					&& s.getY() >= _y && s.getY() < _y+a)
				return s;
		}
		
		return new Square(0, 0, 0);
	}

}
