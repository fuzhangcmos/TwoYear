package com.example.twoyears.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.twoyears.R;

public class ArcMenu extends ViewGroup implements OnClickListener {

	private static int l_cbutton = 0;
	private static int t_cbutton = 0;
	private int mRadius;
	/**
	 * 菜单的状态
	 */
	private Status mCurrentStatus = Status.CLOSE;
	/**
	 * 菜单的主按钮
	 */
	private View mCButton;
	
	private OnMenuItemClickListener mMenuItemClickListener;

	public enum Status
	{
		OPEN, CLOSE
	}
	
	/**
	 * 点击子菜单项的回调接口
	 */
	public interface OnMenuItemClickListener
	{
		void onClick(View view, int pos);
	}

	
	public void setOnMenuItemClickListener(
			OnMenuItemClickListener mMenuItemClickListener)
	{
		this.mMenuItemClickListener = mMenuItemClickListener;
	}
	
	public ArcMenu(Context context)
	{
		this(context, null);
	}

	public ArcMenu(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ArcMenu(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				100, getResources().getDisplayMetrics());
		// 获取自定义属性的值
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ArcMenu, defStyle, 0);
		mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
						getResources().getDisplayMetrics()));
		
		Log.e("TAG", " radius =  " + mRadius);

		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			// 测量child
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed)
		{
			layoutCButton(this.getContext());

			int count = getChildCount();
 
			int cl=0;
			int ct=0;
			for (int i = 0; i < count - 1; i++)
			{
				View child = getChildAt(i + 1);
				child.setVisibility(View.GONE);
				
				cl = l_cbutton + (int) (mRadius * Math.cos(2 * Math.PI / 5 * i + (Math.PI / 2 )));
			    ct = t_cbutton - (int) (mRadius * Math.sin(2 * Math.PI / 5 * i + (Math.PI / 2 )));
			     
		/*		if(i==0){
					 cl = l_cbutton - 0;
				     ct = t_cbutton -  (int) (mRadius * 1);
				}
				if(i==1){
					 cl = l_cbutton + (int) (mRadius * Math.cos(2 * Math.PI / 5 + (Math.PI / 2 )));
				     ct = t_cbutton - (int) (mRadius * Math.sin(2 * Math.PI / 5 + (Math.PI / 2 )));
				}
				if(i==2){
					 cl = l_cbutton + (int) (mRadius * Math.cos((2 * Math.PI / 5 )* 2 + (Math.PI / 2 )));
				     ct = t_cbutton - (int) (mRadius * Math.sin((2 * Math.PI / 5 )* 2 + (Math.PI / 2 )));
				}
				if(i==3){
					 cl = l_cbutton + (int) (mRadius * Math.cos((2 * Math.PI / 5 )* 3 + (Math.PI / 2 )));
				     ct = t_cbutton - (int) (mRadius * Math.sin((2 * Math.PI / 5) * 3 + (Math.PI / 2 )));
				}
				if(i==4){
					
					 cl = l_cbutton + (int) (mRadius * Math.cos((2 * Math.PI / 5) * 4 + (Math.PI / 2 )));
				     ct = t_cbutton - (int) (mRadius * Math.sin((2 * Math.PI / 5) * 4 + (Math.PI / 2 )));
				}*/
				
				int cWidth = child.getMeasuredWidth();
				int cHeight = child.getMeasuredHeight();
				child.layout(cl, ct, cl + cWidth, ct + cHeight);
			}
		}
	}

	private void layoutCButton(Context context) {
		mCButton = getChildAt(0);
		mCButton.setOnClickListener(this);
	
	    WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	    DisplayMetrics dm=new DisplayMetrics();
	    manager.getDefaultDisplay().getMetrics(dm);
	    float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0） 
	    int width2=(int)(dm.widthPixels * density + 0.5f); ;
	    int height2= (int)(dm.heightPixels * density + 0.5f); 
	 
	    //Log.d("width2", String.valueOf(width2));
	    //Log.d("height2", String.valueOf(height2));  
	    
		int width = mCButton.getMeasuredWidth();
		int height = mCButton.getMeasuredHeight();
		
		 l_cbutton = (width2+width)/2;
		 t_cbutton = (height2+height)/2-30;
	
		mCButton.layout(l_cbutton, t_cbutton, l_cbutton + width, t_cbutton + height);

	}
	

	@Override
	public void onClick(View v) {
		rotateCButton(v, 0f, 360f, 300);
		toggleMenu(500);
	}
	
	public void toggleMenu(int duration)
	{
		// 为menuItem添加平移动画和旋转动画
		int count = getChildCount();

		for (int i = 0; i < count - 1; i++)
		{
			final View childView = getChildAt(i + 1);
			childView.setVisibility(View.VISIBLE);
			int cl = l_cbutton + (int) (mRadius * Math.cos(2 * Math.PI / 5 * i + (Math.PI / 2 )));
			int ct = t_cbutton - (int) (mRadius * Math.sin(2 * Math.PI / 5 * i + (Math.PI / 2 )));

			int xflag = -1;
			int yflag = 1;
	
			AnimationSet animset = new AnimationSet(true);
			Animation tranAnim = null;

			// to open
			if (mCurrentStatus == Status.CLOSE)
			{
				tranAnim = new TranslateAnimation(xflag * (int) (mRadius * Math.cos(2 * Math.PI / 5 * i + (Math.PI / 2 ))),0, yflag * (int) (mRadius * Math.sin(2 * Math.PI / 5 * i + (Math.PI / 2 ))), 0);
				childView.setClickable(true);
				childView.setFocusable(true);

			} else
			// to close
			{
				tranAnim = new TranslateAnimation(0, xflag * (int) (mRadius * Math.cos(2 * Math.PI / 5 * i + (Math.PI / 2 ))), 0, yflag * (int) (mRadius * Math.sin(2 * Math.PI / 5 * i + (Math.PI / 2 ))));
				childView.setClickable(false);
				childView.setFocusable(false);
			}
			tranAnim.setFillAfter(true);
			tranAnim.setDuration(duration);
			tranAnim.setStartOffset((i * 200) / count);

			tranAnim.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationStart(Animation animation)
				{

				}

				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}

				@Override
				public void onAnimationEnd(Animation animation)
				{
					if (mCurrentStatus == Status.CLOSE)
					{
						childView.setVisibility(View.GONE);
					}
				}
			});
			// 旋转动画
			RotateAnimation rotateAnim = new RotateAnimation(0, 720,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnim.setDuration(duration);
			rotateAnim.setFillAfter(true);

			animset.addAnimation(rotateAnim);
			animset.addAnimation(tranAnim);
			childView.startAnimation(animset);

			final int pos = i + 1;
			childView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (mMenuItemClickListener != null)
						mMenuItemClickListener.onClick(childView, pos);
					menuItemAnim(pos - 1);
					changeStatus();
				}
			});
	}
	// 切换菜单状态
	changeStatus();
}

	private void menuItemAnim(int pos)
	{
		for (int i = 0; i < getChildCount() - 1; i++)
		{
			View childView = getChildAt(i + 1);
			if (i == pos)
			{
				childView.startAnimation(scaleBigAnim(300));
			} else
			{
				childView.startAnimation(scaleSmallAnim(300));
			}
			childView.setClickable(false);
			childView.setFocusable(false);
		}

	}
	
	private Animation scaleSmallAnim(int duration)
	{
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);
		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);
		return animationSet;
	}


	private Animation scaleBigAnim(int duration)
	{
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);

		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);

		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);
		return animationSet;
	}
	
	

	private void changeStatus()
	{
		mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN
				: Status.CLOSE);
	}

	public boolean isOpen()
	{
		return mCurrentStatus == Status.OPEN;
	}



	private void rotateCButton(View v, float start, float end, int duration)
	{
		RotateAnimation anim = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
}
