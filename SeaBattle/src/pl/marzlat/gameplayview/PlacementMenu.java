package pl.marzlat.gameplayview;

import java.util.ArrayList;

import pl.marzlat.model.Ship;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlacementMenu {

	private float width;
	private float height;
	private float x;
	private float y;
	private Paint color = new Paint();
	private Paint border = new Paint();
	
	private ArrayList<Square> squares = new ArrayList<>();
	private float xs;
	private float ys;
	
	private float horizontalOptionX;
	private float horizontalOptionY;
	private float horiznotalOptionXb;
	private float horizontalOptionYb;
	
	private float verticalOptionX;
	private float verticalOptionY;
	private float verticalOptionXb;
	private float verticalOptionYb;
	
	private int currentOrientation = Ship.HORIZONTAL;
	
	private float removeOptionX;
	private float removeOptionY;
	private float removeOptionXb;
	private float removeOptionYb;
	
	private boolean eraseMode = false;
	
	public PlacementMenu(float x, float y, float width, float height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		color.setColor(Color.WHITE);
		border.setColor(Color.BLACK);
		xs = x + 5.5f/10f*width;
		ys = y + 1/3f*height;
		setShipOnMenu(new Ship(4));
		
		verticalOptionX = x + 2/10f * width;
		verticalOptionY = y + 1/3f * height;
		verticalOptionXb = verticalOptionX+1/20f*width;
		verticalOptionYb = verticalOptionY+3/6f*height;
		
		horizontalOptionX = x + 3.5f/10f * width;
		horizontalOptionY = y + 1/3f * height;
		horiznotalOptionXb = horizontalOptionX+1.5f/10f*width;
		horizontalOptionYb = horizontalOptionY+1/6f*height;
		
		removeOptionX = x+1/20f*width;
		removeOptionY = y+1/3f*height;
		removeOptionXb = x+3/20f*width;
		removeOptionYb = y+2/3f*height;
	}

	public void draw(Canvas canvas)
	{
		float borderWidth = (float) Math.ceil(0.01f * 1/10*width);
		canvas.drawRect(x, y, x+width, y+height, border);
		canvas.drawRect(x + borderWidth, y + borderWidth, x + width - borderWidth, y
				+ height - borderWidth, color);
		drawShip(canvas);
		drawHorizontalOption(canvas);
		drawVerticalOption(canvas);
		drawEraseOption(canvas);
	}

	private void drawEraseOption(Canvas canvas) {
		Paint p = new Paint();
		Paint border = new Paint();
		p.setStrokeWidth((float) Math.ceil(0.01f * 4/10*width));
		p.setColor(Color.RED);
		canvas.drawLine(removeOptionX, removeOptionY, removeOptionXb, removeOptionYb, p);
		canvas.drawLine(removeOptionX, removeOptionYb, removeOptionXb, removeOptionY, p);
		if (isEraseMode())
		{
			border.setColor(Color.BLACK);
			border.setStrokeWidth((float) Math.ceil(0.01f * 4/10*width));
			canvas.drawLine(removeOptionX, removeOptionY, removeOptionXb, removeOptionY, border);
			canvas.drawLine(removeOptionX, removeOptionYb, removeOptionXb, removeOptionYb, border);
			canvas.drawLine(removeOptionX, removeOptionY, removeOptionX, removeOptionYb, border);
			canvas.drawLine(removeOptionXb, removeOptionYb, removeOptionXb, removeOptionY, border);
		}
	}

	private void drawVerticalOption(Canvas canvas) {
		Paint paint = new Paint();
		if (currentOrientation == Ship.VERTICAL)
		{
			paint.setColor(Color.GRAY);
		}
		else
		{
			paint.setColor(Color.BLACK);			
		}
		canvas.drawRect(verticalOptionX, verticalOptionY, verticalOptionXb,
				verticalOptionYb, paint);
	}

	private void drawHorizontalOption(Canvas canvas) {
		Paint paint = new Paint();
		if (currentOrientation == Ship.HORIZONTAL)
		{
			paint.setColor(Color.GRAY);
		}
		else
		{
			paint.setColor(Color.BLACK);			
		}
		canvas.drawRect(horizontalOptionX, horizontalOptionY, horiznotalOptionXb,
				horizontalOptionYb, paint);
	}

	private void drawShip(Canvas canvas) {
		for (Square s : squares)
		{
			Paint p = new Paint();
			p.setColor(Square.STRUCK);
			s.setColor(p);
			s.draw(canvas);
		}
	}
	
	public void setShipOnMenu(Ship ship)
	{
		int n;
		if (ship == null)
			n = 0;
		else
		n = ship.getSize();
		float a = 1/10f*width;
		int i;
		squares = new ArrayList<Square>();
		for (i = 0; i < n; i++) {
				squares.add(new Square(0, i, xs+(i*a), ys, a));
		}
	}
	
	public boolean clickOnHorizontal(float x, float y)
	{
		if (horizontalOptionX < x && x < horiznotalOptionXb && horizontalOptionY < y && y < horizontalOptionYb)
		{
			currentOrientation = Ship.HORIZONTAL;
			return true;
		}
		return false;
	}
	
	public boolean clickOnVertical(float x, float y)
	{
		if (verticalOptionX < x && x < verticalOptionXb && verticalOptionY < y && y < verticalOptionYb)
		{
			currentOrientation = Ship.VERTICAL;
			return true;
		}
		return false;
	}
	
	public boolean clickOnEraseOption(float x, float y)
	{
		if (removeOptionX < x && x < removeOptionXb && removeOptionY < y && y < removeOptionYb)
		{
			return true;
		}		
		return false;
	}
	
	public void removeShipFromMenu()
	{
		squares.clear();
	}

	public int getCurrentOrientation() {
		return currentOrientation;
	}

	public void setCurrentOrientation(int currentOrientation) {
		this.currentOrientation = currentOrientation;
	}

	public boolean isEraseMode() {
		return eraseMode;
	}

	public void setEraseMode(boolean eraseMode) {
		this.eraseMode = eraseMode;
	}
	
	
}
