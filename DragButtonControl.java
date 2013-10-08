package com.iamplus.musicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class DragButtonControl extends RelativeLayout implements OnTouchListener {

	public interface DragButtonControlActionListener {

		public void onDragLeft(int value);
		public void onDragRight(int value);
		public void onDragUp(int value);
		public void onDragDown(int value);
		public void onClicked();
		public void onDragStarted(boolean xAxis);
		public void onDragEnded(boolean xAxis);
	}

	private Context mContext;
	private static int THRESHOLD = 20;
	private static int initX;
	private static int initY;
	private boolean xAxisLocked = false;
	private boolean yAxisLocked = false;
	private ImageButton button;
	private ViewGroup root;
	private int xDelta;
	private int yDelta;
	private int startX = 0;
	private int startY = 0;
	private int parentHeight = 0;
	private int parentWidth = 0;
	private int dragValue = 0;
	private EdragAxis mDragAxis;
	private DragButtonControlActionListener mListener;

	enum EdragAxis{
		EdragAxis_X,
		EdragAxis_Y,
		EdragAxis_XY
	}
	public DragButtonControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initControls();
	}

	private void initControls() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		root = (ViewGroup) inflater.inflate(
				R.layout.drag_button_layout, this, true);
		button = (ImageButton) root.findViewById(R.id.image_button);
		button.setOnTouchListener(this);

		mDragAxis = EdragAxis.EdragAxis_XY;
	}

	public void setOnDragButtonControlActionListener(DragButtonControlActionListener list) {
		mListener = list;
	}


	public void setDragAxis(EdragAxis axis) {
		mDragAxis = axis;
		if(mDragAxis == EdragAxis.EdragAxis_X) {
			xAxisLocked = false;
		}else if(mDragAxis == EdragAxis.EdragAxis_Y) {
			yAxisLocked = false;
		}else if(mDragAxis == EdragAxis.EdragAxis_XY) {
			xAxisLocked = false;
			yAxisLocked = false;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		parentWidth = (int) (MeasureSpec.getSize(widthMeasureSpec));
		parentHeight = (int) (MeasureSpec.getSize(heightMeasureSpec));
		startX = (parentWidth - button.getWidth() )/2;
		startY = (parentHeight  - button.getHeight()) /2;
	}

	public void setDragButtonImage(int resID) {
		button.setImageResource(resID);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		final int X = (int) event.getRawX();
		final int Y = (int) event.getRawY();

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: {

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					button.getLayoutParams());
			layoutParams.leftMargin = startX;
			layoutParams.topMargin = startY;
			view.setLayoutParams(layoutParams);
			xDelta = X - layoutParams.leftMargin;
			yDelta = Y - layoutParams.topMargin;
			initX = X;
			initY = Y;
			break;
		}
		case MotionEvent.ACTION_UP:{

			centerButtonWithAnimation();
			dragValue = 0;

			if(mListener != null) {
				if((xAxisLocked || yAxisLocked)) {
					if(xAxisLocked)
						mListener.onDragEnded(true);
					else
						mListener.onDragEnded(false);
				}
				else
					mListener.onClicked();
			}
			setDragAxis(mDragAxis);
			break;
		}
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_CANCEL:{
			centerButtonWithAnimation();
			dragValue = 0;
			if(mListener != null) {
				if(xAxisLocked)
					mListener.onDragEnded(true);
				else
					mListener.onDragEnded(false);
			}
			setDragAxis(mDragAxis);
		}
		break;
		case MotionEvent.ACTION_MOVE: {
			int diffY = Y - initY;
			int diffX = X - initX;

			if(diffX > 0 ) {

				if (diffX > THRESHOLD && !yAxisLocked) {
					// move right
					if(!xAxisLocked) {
						xAxisLocked = true;
						if(mListener != null) {
							mListener.onDragStarted(true);
						}
					}
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					if(layoutParams.leftMargin < parentWidth - view.getWidth()) {
						layoutParams.leftMargin = X - xDelta;
						view.setLayoutParams(layoutParams);

						int value = (X -initX) / (view.getWidth() / 2);
						if( value > dragValue && mListener != null) {
							mListener.onDragRight(value);
							Log.d("Drag control", "Right------");
							dragValue++;
						}
					}
					break;
				}
			}else {
				if ( diffX < -THRESHOLD && !yAxisLocked) {
					// move left

					if(!xAxisLocked) {
						xAxisLocked = true;
						if(mListener != null) {
							mListener.onDragStarted(true);
						}
					}
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					if(layoutParams.leftMargin >0) {
						layoutParams.leftMargin = X - xDelta;
						view.setLayoutParams(layoutParams);

						int value = (initX - X)  / (view.getWidth() / 2);
						if( value > dragValue && mListener != null) {
							mListener.onDragLeft(value);
							Log.d("Drag control", "Left------");
							dragValue++;
						}
					}
					break;
				}
			}

			if(diffY > 0) {

				if (diffY > THRESHOLD && !xAxisLocked) {
					// move down
					if(!yAxisLocked) {
						yAxisLocked = true;
						if(mListener != null) {
							mListener.onDragStarted(false);
						}
					}
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					
					if(layoutParams.topMargin < parentHeight - view.getHeight()) {
						layoutParams.topMargin = Y - yDelta;
						view.setLayoutParams(layoutParams);

						int value = (Y - initY)  / (view.getHeight()/ 2);
						if( value > dragValue && mListener != null) {
							mListener.onDragDown(1);
							Log.d("Drag control", "Down------");
							dragValue++;
						}
					}
					break;
				}
			}else {
				if (diffY < -THRESHOLD && !xAxisLocked) {
					// move up
					if(!yAxisLocked) {
						yAxisLocked = true;
						if(mListener != null) {
							mListener.onDragStarted(false);
						}
					}
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					if(layoutParams.topMargin > 0) {
						layoutParams.topMargin = Y - yDelta;
						view.setLayoutParams(layoutParams);
						int value = (initY - Y)  / (view.getHeight() / 2);
						if( value > dragValue && mListener != null) {
							mListener.onDragUp(1);
							Log.d("Drag control", "Up------");
							dragValue++;
						}
					}
					break;
				}
			}
			break;
		}
		}
		root.invalidate();
		return true;
	}

	private void centerButtonWithAnimation() {

		Animation a = new Animation() {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
			int oriX = layoutParams.leftMargin;
			int oriY = layoutParams.topMargin;

			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {

				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						button.getLayoutParams());
				layoutParams.leftMargin = oriX - (int)(( oriX - startX) * interpolatedTime); 
				layoutParams.topMargin =  oriY - (int)((oriY - startY) * interpolatedTime); ;
				button.setLayoutParams(layoutParams);
			}
		};
		a.setDuration(350);
		button.startAnimation(a);
	}
}
