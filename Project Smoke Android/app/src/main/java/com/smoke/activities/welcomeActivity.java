package com.smoke.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.smoke.R;
import com.smoke.activities.mainLayoutActivity;
import com.smoke.activities.registerPhoneActivity;
import com.smoke.core.Environment;

public class welcomeActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
        Transition ts = new Slide();  //Slide(); //Explode();

        //ts.setStartDelay(2000);
        ts.setDuration(50);
        ts.excludeTarget(android.R.id.statusBarBackground, true);
        ts.excludeTarget(android.R.id.navigationBarBackground, true);
        ts.excludeTarget(R.id.layoutBackground, false);
        ts.excludeTarget(R.id.layoutChildBackground, true);
        ts.excludeChildren(R.id.layoutChildBackground, true);

        /*
        If you have set an enter transition for the second activity,
        the transition is also activated when the activity starts.
         */

        //getWindow().setEnterTransition(ts);
        //getWindow().setExitTransition(ts);
        ColorDrawable colorDrawable = new ColorDrawable( Color.TRANSPARENT );
        getWindow().setBackgroundDrawable( colorDrawable );


        setContentView(R.layout.main);
    }

    public void launchSecondActivity() {
        Intent intent = new Intent(this, registerPhoneActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

    public void onClickTransition(View view)
    {
        // Perform action on click
        launchSecondActivity();
    }
}
