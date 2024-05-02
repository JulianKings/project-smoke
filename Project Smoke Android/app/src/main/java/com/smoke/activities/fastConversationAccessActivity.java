package com.smoke.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.layout.ContactsArrayAdapter;
import com.smoke.layout.FastContactsArrayAdapter;
import com.smoke.util.detector.ActivitySwipeDetector;
import com.smoke.util.detector.SwipeInterface;

public class fastConversationAccessActivity extends Activity implements SwipeInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.updateActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
        Transition slideUp = new Slide(Gravity.TOP);  //Slide(); //Explode();
        Transition slideDown = new Slide(Gravity.BOTTOM);  //Slide(); //Explode();

        slideUp.setDuration(100);
        slideUp.excludeTarget(android.R.id.statusBarBackground, true);
        slideUp.excludeTarget(android.R.id.navigationBarBackground, true);
        slideUp.excludeTarget(R.id.fastConversationHolder, false);
        slideUp.excludeTarget(R.id.littleFastConversationHolder, true);
        slideUp.excludeChildren(R.id.littleFastConversationHolder, true);

        slideDown.setDuration(100);
        slideDown.excludeTarget(android.R.id.statusBarBackground, true);
        slideDown.excludeTarget(android.R.id.navigationBarBackground, true);
        slideDown.excludeTarget(R.id.fastConversationHolder, false);
        slideDown.excludeTarget(R.id.littleFastConversationHolder, true);
        slideDown.excludeChildren(R.id.littleFastConversationHolder, true);

        getWindow().setEnterTransition(slideDown);
        getWindow().setExitTransition(slideUp);

        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(colorDrawable);

        setContentView(R.layout.fast_conversation_access);

        ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
        RelativeLayout swipe_layout = (RelativeLayout) findViewById(R.id.fastConversationHolder);
        swipe_layout.setOnTouchListener(swipe);

        final DynamicListView listView = (DynamicListView) findViewById(R.id.contactsList);
        FastContactsArrayAdapter contactsAdapter = (new FastContactsArrayAdapter(this, Environment.getPhoneContacts()));
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(contactsAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
        listView.setItemsCanFocus(true);
    }

    @Override
    public void leftToRight(View v) {
    }

    @Override
    public void rightToLeft(View v) {
        finish();
    }
}