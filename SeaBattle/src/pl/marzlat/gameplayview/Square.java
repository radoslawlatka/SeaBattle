package pl.marzlat.gameplayview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Square {

	public static int MISSED = Color.GRAY;
	public static int BUSY = Color.BLACK;
	public static int STRUCK = Color.RED;
	public static int FREE = Color.WHITE;
	public static int CLICKED = Color.BLUE;
	
	private int row;
	private int column;
	private float x;
	private float y;
	private float a;
	private Paint color;
	private Paint border;

	public Square(int row, int column, float x, float y, float a) {
		this.row = row;
		this.column = column;
		this.x = x;
		this.y = y;
		this.a = a;
		color = new Paint();
		border = new Paint();
		color.setColor(FREE);
		border.setColor(Color.BLACK);
	}

	public void draw(Canvas c) {
		float borderWidth = (float) Math.ceil(0.01f * a);
		c.drawRect(x, y, x + a, y + a, border);
		c.drawRect(x + borderWidth, y + borderWidth, x + a - borderWidth, y
				+ a - borderWidth, color);
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Paint getColor() {
		return color;
	}

	public void setColor(Paint color) {
		this.color = color;
	}

	public Paint getBorder() {
		return border;
	}

	public void setBorder(Paint border) {
		this.border = border;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getXb() {
		return x + a;
	}

	public float getYb() {
		return y + a;
	}

	
}
