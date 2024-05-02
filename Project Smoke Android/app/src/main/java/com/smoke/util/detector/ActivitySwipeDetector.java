package com.smoke.util.detector;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ActivitySwipeDetector implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private SwipeInterface activity;
    static final int MIN_DISTANCE = 60;
    private float downX, downY, upX, upY;

    public ActivitySwipeDetector(SwipeInterface activity){
        this.activity = activity;
    }

    public void onRightToLeftSwipe(View v){
        activity.rightToLeft(v);
    }

    public void onLeftToRightSwipe(View v){
        activity.leftToRight(v);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    if(deltaX < 0) { this.onLeftToRightSwipe(v); return true; }
                    if(deltaX > 0) { this.onRightToLeftSwipe(v); return true; }
                }
                else {
                    //Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }
            }
        }
        return false;
    }

}