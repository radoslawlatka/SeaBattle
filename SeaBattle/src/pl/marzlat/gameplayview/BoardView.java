package pl.marzlat.gameplayview;

import pl.marzlat.model.Area;
import pl.marzlat.model.Field;
import pl.marzlat.model.Ship;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {

	private Board mBoard;
	private PlacementMenu menu;
	private boolean blocked = false;
	private boolean placementMenuDrawing = true;
	private int clickedX = -1;
	private int clickedY = -1;
	private boolean eraseModeClicked = false;
		
	public BoardView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init(context);
	}

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	    init(context);
	}
	public BoardView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		float boardWidth = displaymetrics.widthPixels-20;

		this.setBackgroundColor(Color.TRANSPARENT);

		mBoard = new Board(10, 10, 10, boardWidth);
		menu = new PlacementMenu(10, mBoard.getY()+mBoard.getWidth(), boardWidth, 3f/10*(boardWidth));
		Log.d("BoardView.init", "width = " + getWidth() + " height = " + getHeight());
		Log.d("BoardView.init", "x = " + getX() + "y = " + getY());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mBoard.draw(canvas);
		if (isPlacementMenuDrawing())
			menu.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (blocked)
		{
			
		}
		else
		{
			eraseModeClicked = menu.clickOnEraseOption(event.getX(), event.getY());
			Log.d("BoardView.eraseModeClicked ", eraseModeClicked+"");
			
			Square s = mBoard.getSquaresOnPosition(event.getX(), event.getY());
			
			if (s != null && (isEraseMode() || s.getColor().getColor() == Square.FREE))
			{
				clickedX = s.getColumn();
				clickedY = s.getRow();
				Log.d("click", s.getColumn() + " " + s.getRow()+ " " + event.getX() + " " + event.getY());
			}
			else
			{
				clickedX = -1;
				clickedY = -1;
				if (isPlacementMenuDrawing())
				{
					if (menu.clickOnHorizontal(event.getX(), event.getY()))
					{
						Log.d("BoardView", "New orientation - horizontal");
					}
					else if (menu.clickOnVertical(event.getX(), event.getY()))
					{
						Log.d("BoardView", "New orientation - vertical");					
					}
				}
			}
			invalidate();
		}
		return super.onTouchEvent(event);
	}
	
	public void setBoardAndDraw(Area area)
	{
		
		showAreaOnBoard(area);
		Log.d("BoardView", "setBoardAndDraw: Created new Board");
		invalidate();
	}

	private void showAreaOnBoard(Area area)
    {
    	int i, j;
    	Paint p = new Paint();
        for (i = 0; i < Area.SIZE; i++) {
            for (j = 0; j < Area.SIZE; j++) {
                Field field = area.getField(j, i);
                Square square = mBoard.getSquare(j, i);
                if (field.getState() == Field.BUSY)
                {	
                	p.setColor(Square.BUSY);
                	square.setColor(new Paint(p));
                }
            	else if (field.getState() == Field.STRUCK)
            	{
                	p.setColor(Square.STRUCK);
                	square.setColor(new Paint(p));
            	}
                else if (field.getState() == Field.MISSED)
                {
                	p.setColor(Square.MISSED);
                	square.setColor(new Paint(p));
                }
            	else
            	{
                	p.setColor(Square.FREE);
                	square.setColor(new Paint(p));
            	}
            }
        }
    }
	
	public void setCurrentShip(Ship ship)
	{
		menu.setShipOnMenu(ship);
		invalidate();
	}

	public int getShipOrientation()
	{
		return menu.getCurrentOrientation();
	}
	
	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public int getClickedX() {
		return clickedX;
	}

	public int getClickedY() {
		return clickedY;
	}

	public boolean isPlacementMenuDrawing() {
		return placementMenuDrawing;
	}

	public void setPlacementMenuDrawing(boolean placementMenuDrawing) {
		this.placementMenuDrawing = placementMenuDrawing;
	}

	public boolean isEraseModeClicked() {
		return eraseModeClicked;
	}

	public void setEraseModeClicked(boolean eraseModeClicked) {
		this.eraseModeClicked = eraseModeClicked;
	}

	public boolean isEraseMode() {
		return menu.isEraseMode();
	}

	public void setEraseMode(boolean eraseMode) {
		menu.setEraseMode(eraseMode);
		invalidate();
	}
	
	

}