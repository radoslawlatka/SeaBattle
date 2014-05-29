package pl.marzlat.gameplayview;

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

class BoardView extends View {

	private Board mBoard;
	Paint p = new Paint();
	
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
		int width = displaymetrics.widthPixels;

		this.setBackgroundColor(Color.TRANSPARENT);

		mBoard = new Board(10, 10, 10, width - 20);
		Log.d("CanvasView.CanvasView", "width = " + width);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mBoard.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("click", event.getX() + " " + event.getY());
		Square s = mBoard.getSquaresOnPosition(event.getX(), event.getY());

		p.setColor(Color.BLUE);;

		if (s != null)
			s.setColor(p);

		invalidate();
		return super.onTouchEvent(event);
	}

}