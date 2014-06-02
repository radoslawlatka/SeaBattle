package pl.marzlat.gameplayview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class PlacementMenu extends View {

	private int width;
	private int height;
	private float x;
	private float y;
	Paint p = new Paint(Color.WHITE);
	
	public PlacementMenu(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init(context);
	}

	public PlacementMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	    init(context);
	}
	public PlacementMenu(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
		x = 0;
		y = 0;
		height = (int) (3*width/10f);
		this.setBackgroundColor(Color.WHITE);
		Log.d("PlacementMenu.init", "width = " + width + " height = " + height);

		Log.d("PlacementMenu.init", "x = " + getX() + " height = " + getY());
}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		canvas.drawRect(x, y, x+width, y+height, p);
	}
	
}
