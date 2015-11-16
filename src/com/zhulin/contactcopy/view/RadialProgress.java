package com.zhulin.contactcopy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhulin.contactcopy.R;


public class RadialProgress extends View {
	
	private RectF mRadialScoreRect;
	/** 默认进度为0 */
	private int mCurrentValue = 0;
	
	private int mMaxValue = 100;
	
	private float mRadius = 0.0f;
	
	private int mDiameter = 200;
	
	private int mMaxSweepAngle = 360;
	
	private Paint mRadialWidgetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private int mBaseColor =Color.YELLOW;
	/** 最外圈环 */
	private int mBorderColor = Color.BLACK;
	/** 文字 */
	private int mCenterTextColor = Color.WHITE;
	
	private int mShadowColor = Color.BLACK;
	
	private int mScoreColorRange;
	
	private float mBorderStrokeThickness = 0.5f;
	
	private float mShadowRadius = 3f;
	
	/** 去掉%符号 ，默认是不去掉的 */
	private boolean isShowPercentText = true;
	/** 是否显示当前的进度比 ，默认是显示的 */
	private boolean isShowPercent = true;
	
	private float mCenterTextSize = 0.0f;
	
	private int readingValuePer = 0;
	
	private int mMinChangeValue = 0;
	
	private int mMaxChangeValue = mMaxValue;
	
	private String mFontName = null;
	
	public interface OnRadialViewValueChanged {
		public void onValueChanged(int value);
	}
	
	public RadialProgress(Context context) {
		super(context);
		initView(context);
	}
	
	public RadialProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public RadialProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		Rect rect = new Rect(0, 0, mDiameter, mDiameter);
		mRadialScoreRect = new RectF(rect); 
		mScoreColorRange = context.getResources().getColor(R.color.croci_text_color);
		mCenterTextColor = mScoreColorRange;
		mBorderColor =  context.getResources().getColor(R.color.edit_text_color);
		mBaseColor = Color.WHITE;
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//����Ȧ
//		mRadialWidgetPaint.setStyle(Style.STROKE);
		
//		mRadialWidgetPaint.setStrokeWidth(mBorderStrokeThickness * getResources().getDisplayMetrics().density);
		mRadialWidgetPaint.setColor(mBorderColor);
		canvas.drawCircle(getWidth()/ 2, getHeight() / 2, mRadius-1, mRadialWidgetPaint);		
		mRadialWidgetPaint.setStyle(Style.FILL);
		//Draw the score radial
		if(mCurrentValue <= mMaxValue) {
			double sweepAngle = ((mCurrentValue * mMaxSweepAngle) / mMaxValue); //Calculate the arc span
			//Determine the color of the score radial from the given array of colors
			readingValuePer = (mCurrentValue * 100) /mMaxValue;
			//Set the color to the paint and draw the arc
			mRadialWidgetPaint.setColor(mScoreColorRange);						
			canvas.drawArc(mRadialScoreRect, 270, (float) sweepAngle, true, mRadialWidgetPaint);
			mRadialWidgetPaint.setShadowLayer((float) (mShadowRadius/2) * getResources().getDisplayMetrics().density, 0.0f, 0.0f, mShadowColor);  
			canvas.drawArc(mRadialScoreRect, 270, (float) sweepAngle, true, mRadialWidgetPaint);
			mRadialWidgetPaint.setShadowLayer(mShadowRadius, 0.0f, 0.0f, Color.TRANSPARENT);  
		} else 
			Log.e(this.getClass().getName(), "Current value " + String.valueOf(mCurrentValue) + " greater that maximum value " + String.valueOf(mMaxValue)); 
		//���м�ԭͼ
		mRadialWidgetPaint.setColor(mBaseColor);
		canvas.drawCircle(getWidth()/ 2, getHeight() / 2, (float) (mRadius * .8), mRadialWidgetPaint);
		mRadialWidgetPaint.setShadowLayer(mShadowRadius * getResources().getDisplayMetrics().density, 0.0f, 0.0f, mShadowColor);  
		canvas.drawCircle(getWidth()/ 2, getHeight() / 2, (float) (mRadius * .8), mRadialWidgetPaint);
		mRadialWidgetPaint.setShadowLayer(mShadowRadius, 0.0f, 0.0f, Color.TRANSPARENT);  
		//������ֵ���ı�
		mRadialWidgetPaint.setColor(mCenterTextColor);
		mRadialWidgetPaint.setTextSize(mCenterTextSize);		
		if(mFontName != null) 
			mRadialWidgetPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), mFontName));
		float textWidth = 0.0f;
		//Check if the user wants percentage value
		if(isShowPercent){
			if(isShowPercentText) {
				textWidth = mRadialWidgetPaint.measureText(String.valueOf(readingValuePer) + "%");
				canvas.drawText(String.valueOf(readingValuePer) + "%", (getWidth()/ 2) - (textWidth/2), (getHeight()/2) + mRadius/8, mRadialWidgetPaint);
			} else {
				textWidth = mRadialWidgetPaint.measureText(String.valueOf(mCurrentValue));
				canvas.drawText(String.valueOf(mCurrentValue), (getWidth()/ 2) - (textWidth/2), (getHeight()/2) + mRadius/8, mRadialWidgetPaint);
			}
		}
		invalidate();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//Determine the diameter and the radius based on device orientation
		if(w > h) {
			mDiameter = h;
			mRadius = mDiameter/2 - (getPaddingTop() + getPaddingBottom());
		} else {
			mDiameter = w;
			mRadius = mDiameter/2 - (getPaddingLeft() + getPaddingRight());
		}
		//Init the draw arc Rect object
		int left = (getWidth()/2) - (int) mRadius + getPaddingLeft();
		int right = (getWidth()/2) + (int) mRadius - getPaddingRight();
		int top = (getHeight()/2) - (int) mRadius + getPaddingTop();
		int bottom = (getHeight()/2) + (int) mRadius - getPaddingBottom();
		Rect rect = new Rect(left, top, right, bottom);
		mRadialScoreRect = new RectF(rect); 
		//Init the font size
		mCenterTextSize = mRadius/2;
	}
	
	/**
	 * @return the mCurrentValue
	 */
	public int getCurrentValue() {
		return mCurrentValue;
	}

	/**
	 * @param mCurrentValue the mCurrentValue to set
	 */
	public void setCurrentValue(int mCurrentValue) {
		this.mCurrentValue = mCurrentValue;
	}

	/**
	 * @return the mMaxValue
	 */
	public int getMaxValue() {
		return mMaxValue;
	}

	/**
	 * @param mMaxValue the mMaxValue to set
	 */
	public void setMaxValue(int mMaxValue) {
		this.mMaxValue = mMaxValue;
	}

	/**
	 * @return the mScoreColorRange
	 */
	public int getScoreColorRange() {
		return mScoreColorRange;
	}

	/**
	 * @param mScoreColorRange the mScoreColorRange to set
	 */
	public void setScoreColorRange(int mScoreColorRange) {
		this.mScoreColorRange = mScoreColorRange;
	}

	/**
	 * @return the mBaseColor
	 */
	public int getBaseColor() {
		return mBaseColor;
	}

	/**
	 * @param mBaseColor the mBaseColor to set
	 */
	public void setBaseColor(int mBaseColor) {
		this.mBaseColor = mBaseColor;
	}

	/**
	 * @return the mBorderColor
	 */
	public int getBorderColor() {
		return mBorderColor;
	}

	/**
	 * @param mBorderColor the mBorderColor to set
	 */
	public void setBorderColor(int mBorderColor) {
		this.mBorderColor = mBorderColor;
	}

	/**
	 * @return the mCenterTextColor
	 */
	public int getCenterTextColor() {
		return mCenterTextColor;
	}

	/**
	 * @param mCenterTextColor the mCenterTextColor to set
	 */
	public void setCenterTextColor(int mCenterTextColor) {
		this.mCenterTextColor = mCenterTextColor;
	}

	/**
	 * @return the mShadowColor
	 */
	public int getShadowColor() {
		return mShadowColor;
	}

	/**
	 * @param mShadowColor the mShadowColor to set
	 */
	public void setShadowColor(int mShadowColor) {
		this.mShadowColor = mShadowColor;
	}

	/**
	 * @return the mBorderStrokeThickness
	 */
	public float getBorderStrokeThickness() {
		return mBorderStrokeThickness;
	}

	/**
	 * @param mBorderStrokeThickness the mBorderStrokeThickness to set
	 */
	public void setBorderStrokeThickness(float mBorderStrokeThickness) {
		this.mBorderStrokeThickness = mBorderStrokeThickness;
	}

	/**
	 * @return the mShadowRadius
	 */
	public float getShadowRadius() {
		return mShadowRadius;
	}

	/**
	 * @param mShadowRadius the mShadowRadius to set
	 */
	public void setShadowRadius(float mShadowRadius) {
		this.mShadowRadius = mShadowRadius;
	}

	/**
	 * @return the isShowPercentText
	 */
	public boolean isShowPercentText() {
		return isShowPercentText;
	}

	/**
	 * @param isShowPercentText the isShowPercentText to set
	 */
	public void setShowPercentText(boolean isShowPercentText) {
		this.isShowPercentText = isShowPercentText;
	}

	/**
	 * @return the mCenterTextSize
	 */
	public float getCenterTextSize() {
		return mCenterTextSize;
	}

	/**
	 * @param mCenterTextSize the mCenterTextSize to set
	 */
	public void setCenterTextSize(float mCenterTextSize) {
		this.mCenterTextSize = mCenterTextSize;
	}

	/**
	 * @return the mMinChangeValue
	 */
	public int getMinChangeValue() {
		return mMinChangeValue;
	}

	/**
	 * @param mMinChangeValue the mMinChangeValue to set
	 */
	public void setMinChangeValue(int mMinChangeValue) {
		this.mMinChangeValue = mMinChangeValue;
	}

	/**
	 * @return the mMaxChangeValue
	 */
	public int getMaxChangeValue() {
		return mMaxChangeValue;
	}

	/**
	 * @param mMaxChangeValue the mMaxChangeValue to set
	 */
	public void setMaxChangeValue(int mMaxChangeValue) {
		this.mMaxChangeValue = mMaxChangeValue;
	}
	
	/**
	 * @param mFont
	 */
	public void setFontName(String mFont) {
		mFontName = mFont;
	}

	public boolean isShowPercent() {
		return isShowPercent;
	}

	public void setShowPercent(boolean isShowPercent) {
		this.isShowPercent = isShowPercent;
	}
	
	
}
