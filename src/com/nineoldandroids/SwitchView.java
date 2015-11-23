package com.nineoldandroids;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SwitchView extends ViewGroup {
	public final static int STATE_LEFT_ON_TOP = 101;
	public final static int STATE_RIGHT_ON_TOP = 102;
	private int currentState = STATE_LEFT_ON_TOP;
	private View childLeft;
	private View childRight;
	private boolean isInit = false;// 是否测量过了
	private boolean isInit2 = false;// 是否布局过了
	// 缩放动画
	private ObjectAnimator smallToBig;
	private ObjectAnimator bigToSmall;
	// 位移动画，L代表childLeft使用，R代表childRight使用
	private ObjectAnimator leftToRightL;
	private ObjectAnimator leftToRightR;
	private ObjectAnimator rightToLeftL;
	private ObjectAnimator rightToLeftR;
	private AnimatorSet animatorSet;
	private int leftChildW;
	private int leftChildH;
	private int rightChildW;
	private int rightChildH;
	private boolean isAniming = false;// 记录是否正在进行动画，防止暴力点击
	private OnStateChangeListener mStateChangeListener;// 回调状态变化

	public interface OnStateChangeListener {
		void onStateChange(int state);
	}

	public SwitchView(Context context) {
		this(context, null);
	}

	public SwitchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setChildrenDrawingOrderEnabled(true);// 开启有序绘制child
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 初始化，只调用一次
		if (!isInit) {
			childLeft = getChildAt(0);
			childRight = getChildAt(1);
			childLeft.setOnClickListener(leftClickListener);
			childRight.setOnClickListener(rightClickListener);
			measureChild(childLeft, widthMeasureSpec, heightMeasureSpec);
			measureChild(childRight, widthMeasureSpec, heightMeasureSpec);
			// 记录childLeft和childRight的宽高
			leftChildW = childLeft.getMeasuredWidth();
			leftChildH = childLeft.getMeasuredHeight();
			rightChildW = childRight.getMeasuredWidth();
			rightChildH = childRight.getMeasuredHeight();
			// 初始化动画
			smallToBig = ObjectAnimator.ofFloat(null, "scaleY", new float[] { 0.8f, 1 });
			bigToSmall = ObjectAnimator.ofFloat(null, "scaleY", new float[] { 1, 0.8f });
			leftToRightL = ObjectAnimator.ofFloat(childLeft, "translationX", new float[] { 0, leftChildW / 2 });
			rightToLeftL = ObjectAnimator.ofFloat(childLeft, "translationX", new float[] { 0 });
			leftToRightR = ObjectAnimator.ofFloat(childRight, "translationX", new float[] { 0, rightChildW / 2 });
			rightToLeftR = ObjectAnimator.ofFloat(childRight, "translationX", new float[] { 0 });
			animatorSet = new AnimatorSet();
			animatorSet.addListener(mAnimatorListener);
			isInit = true;
		}
		// 宽度为两个child宽度相加
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(leftChildW + rightChildW, MeasureSpec.EXACTLY);
		// 高度为两个child中较高的那个
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(leftChildH > rightChildH ? leftChildH : rightChildH,
				MeasureSpec.EXACTLY);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	AnimatorListener mAnimatorListener = new AnimatorListener() {
		@Override
		public void onAnimationStart(Animator animation) {
			isAniming = true;
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			requestLayout();// 改变了绘制顺序
			isAniming = false;
			// AnimatorSet好像没有clear、reset之类的方法，只能这样了
			animatorSet = null;
			animatorSet = new AnimatorSet();
			animatorSet.addListener(mAnimatorListener);
		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}
	};

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		switch (currentState) {
		case STATE_LEFT_ON_TOP:
			// childLeft在上，需要先draw childRight再draw childLeft
			// 后draw的会覆盖先draw的，这样childLeft才会在上层
			if (i == 0) {
				return 1;
			} else {
				return 0;
			}
		default:
			return i;
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// int praentLeft = getLeft();
		// int praentTop = getTop();
		// int praentRight = getRight();
		// int praentBottom = getBottom();
		// Log.e("l-t-r-b", praentLeft + "-" + praentTop + "-" + praentRight +
		// "-" + praentBottom);
		switch (currentState) {
		// 两个child的移动是动画，并不需要在这里进行特别处理，注意动画的参数就行了
		case STATE_LEFT_ON_TOP:
		case STATE_RIGHT_ON_TOP:
			childLeft.layout(0, 0, leftChildW, leftChildH);
			childRight.layout(leftChildW - rightChildW / 2, 0, leftChildW + rightChildW / 2, rightChildH);
			break;
		}
		// 这里是初始时的状态判断并初始化显示
		if (!isInit2) {
			if (currentState == STATE_LEFT_ON_TOP) {
				// 只需要把childRight变小
				bigToSmall.setTarget(childRight);
				bigToSmall.start();
			} else {
				// 把childLeft变小并且两个都向右移
				bigToSmall.setTarget(childLeft);
				animatorSet = null;
				animatorSet = new AnimatorSet();
				animatorSet.addListener(mAnimatorListener);
				animatorSet.playTogether(bigToSmall, leftToRightL, leftToRightR);
				animatorSet.start();
			}
			isInit2 = true;
		}
	}

	// 本来想设置点击底层的child才会切换状态
	// 但看着自己粗大的手指，还是点击两个child都能切换吧
	OnClickListener leftClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			changeState();
		}
	};
	OnClickListener rightClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			changeState();
		}
	};

	public void changeState() {
		if (!isAniming) {
			if (currentState == STATE_RIGHT_ON_TOP) {
				currentState = STATE_LEFT_ON_TOP;
				if (mStateChangeListener != null) {
					mStateChangeListener.onStateChange(currentState);
				}
				smallToBig.setTarget(childLeft);
				bigToSmall.setTarget(childRight);
				animatorSet.playTogether(smallToBig, bigToSmall, rightToLeftL, rightToLeftR);
				animatorSet.start();
			} else {
				currentState = STATE_RIGHT_ON_TOP;
				if (mStateChangeListener != null) {
					mStateChangeListener.onStateChange(currentState);
				}
				smallToBig.setTarget(childRight);
				bigToSmall.setTarget(childLeft);
				animatorSet.playTogether(smallToBig, bigToSmall, leftToRightL, leftToRightR);
				animatorSet.start();
			}
		}
	}

	/**
	 * 获取当前状态
	 * 
	 * @return SwitchView.STATE_LEFT_IN_TOP or SwitchView.STATE_RIGHT_IN_TOP
	 */
	public int getState() {
		return currentState;
	}

	/**
	 * 设置当前状态，设置初始状态请用setState
	 * 
	 * @param state
	 *            SwitchView.STATE_LEFT_IN_TOP or SwitchView.STATE_RIGHT_IN_TOP
	 */
	public void setState(int state) {
		if (state != currentState) {
			changeState();
		}
	}

	/**
	 * 设置初始状态，不要用setState来设置初始状态
	 * 
	 * @param state
	 *            SwitchView.STATE_LEFT_IN_TOP or SwitchView.STATE_RIGHT_IN_TOP
	 */
	public void setInitState(int state) {
		currentState = state;
	}

	/**
	 * 设置状态变化时的回调
	 * 
	 * @param l
	 */
	public void setOnStateChangeListener(OnStateChangeListener l) {
		mStateChangeListener = l;
	}
}
