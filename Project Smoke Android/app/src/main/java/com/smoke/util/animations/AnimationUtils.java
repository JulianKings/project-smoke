package com.smoke.util.animations;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

public class AnimationUtils {
    private int measuredHeight = -1;
    private int measuredWidth = -1;
    private final View currentLayout;

    public AnimationUtils(View layout)
    {
        currentLayout = layout;
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        currentLayout.measure(widthSpec, heightSpec);
        measuredHeight = currentLayout.getMeasuredHeight();
        measuredWidth = currentLayout.getMeasuredWidth();
    }

    public void setHeight(int height)
    {
        final float scale = currentLayout.getContext().getResources().getDisplayMetrics().density;
        measuredHeight = (int) (height * scale + 0.5f);
    }

    public void setWidth(int width)
    {
        final float scale = currentLayout.getContext().getResources().getDisplayMetrics().density;
        measuredWidth = (int) (width * scale + 0.5f);
    }

    public static AnimationUtils forHeight(View layout, int height)
    {
        AnimationUtils utils = new AnimationUtils(layout);
        utils.setHeight(height);
        return utils;
    }

    public static AnimationUtils forWidth(View layout, int width)
    {
        AnimationUtils utils = new AnimationUtils(layout);
        utils.setWidth(width);
        return utils;
    }

    public void expandHeight() {
        //set Visible
        currentLayout.setVisibility(View.VISIBLE);

        ValueAnimator mAnimator = slideAnimatorHeight(0, measuredHeight, currentLayout);
        mAnimator.start();
    }

    public void expandToHeight(final View viewToShow) {
        //set Visible
        currentLayout.setVisibility(View.VISIBLE);

        ValueAnimator mAnimator = slideAnimatorHeight(0, measuredHeight, currentLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)  {  }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                viewToShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {  }

            @Override
            public void onAnimationRepeat(Animator animation) {  }
        });
        mAnimator.start();
    }


    public void collapseHeight() {
        ValueAnimator mAnimator = slideAnimatorHeight(measuredHeight, 0, currentLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)  {  }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                currentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {  }

            @Override
            public void onAnimationRepeat(Animator animation) {  }
        });
        mAnimator.start();
    }

    public void collapseToHeight(final View viewToShow) {
        viewToShow.setVisibility(View.GONE);

        ValueAnimator mAnimator = slideAnimatorHeight(measuredHeight, 0, currentLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)  {  }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                currentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {  }

            @Override
            public void onAnimationRepeat(Animator animation) {  }
        });
        mAnimator.start();
    }

    public void expandWidth() {
        //set Visible
        currentLayout.setVisibility(View.VISIBLE);

        ValueAnimator mAnimator = slideAnimatorWidth(0, measuredWidth, currentLayout);
        mAnimator.start();
    }

    public void expandToWidth(final View viewToShow) {
        //set Visible
        currentLayout.setVisibility(View.VISIBLE);

        ValueAnimator mAnimator = slideAnimatorWidth(0, measuredWidth, currentLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)  {  }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                viewToShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {  }

            @Override
            public void onAnimationRepeat(Animator animation) {  }
        });
        mAnimator.start();
    }


    public void collapseWidth() {
        ValueAnimator mAnimator = slideAnimatorWidth(measuredWidth, 0, currentLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)  {  }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                currentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {  }

            @Override
            public void onAnimationRepeat(Animator animation) {  }
        });
        mAnimator.start();
    }

    public void collapseToWidth(final View viewToShow) {
        viewToShow.setVisibility(View.GONE);

        ValueAnimator mAnimator = slideAnimatorWidth(measuredWidth, 0, currentLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)  {  }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                currentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {  }

            @Override
            public void onAnimationRepeat(Animator animation) {  }
        });
        mAnimator.start();
    }

    private static ValueAnimator slideAnimatorHeight(int start, int end, final View currentLayoutz) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = currentLayoutz.getLayoutParams();
                layoutParams.height = value;
                currentLayoutz.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private static ValueAnimator slideAnimatorWidth(int start, int end, final View currentLayoutz) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = currentLayoutz.getLayoutParams();
                layoutParams.width = value;
                currentLayoutz.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
