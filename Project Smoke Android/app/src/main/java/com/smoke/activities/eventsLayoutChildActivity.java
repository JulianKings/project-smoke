package com.smoke.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.layout.EventListArrayAdapter;
import com.smoke.layout.GroupListArrayAdapter;
import com.smoke.util.management.EventList;
import com.smoke.util.management.GroupList;

import java.util.ArrayList;
import java.util.List;

public class eventsLayoutChildActivity extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View currentView =  inflater.inflate(R.layout.events_layout, container, false);
        return currentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());

        final ListView listView = (ListView) getView().findViewById(R.id.eventsList);

        List<EventList> groupLists = new ArrayList<>();
        groupLists.add(new EventList(1, "Most popular events", "#BD043E", null));
        groupLists.add(new EventList(2, "Recommended events", "#05097C", null));
        groupLists.add(new EventList(3, "Your events", "#039A30", null));

        EventListArrayAdapter contactsAdapter = (new EventListArrayAdapter(this.getActivity(), groupLists));
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(contactsAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
        listView.setItemsCanFocus(true);

        FrameLayout holder = (FrameLayout) getView().findViewById(R.id.magicSuperClick);
        holder.setOnTouchListener(new FrameLayout.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(Environment.getCurrentActivity() instanceof mainLayoutActivity) {
                    ViewPager viewPager = (ViewPager) Environment.getCurrentActivity().findViewById(R.id.viewpager);
                    SmartTabLayout viewPagerTab = (SmartTabLayout) Environment.getCurrentActivity().findViewById(R.id.viewpagertab);
                    //Toast.makeText(getActivity(), "MOO" + event.toString(), Toast.LENGTH_SHORT).show();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow previous listView to intercept touch events.
                            viewPager.requestDisallowInterceptTouchEvent(true);
                            viewPagerTab.requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow previous listView to intercept touch events.
                            viewPager.requestDisallowInterceptTouchEvent(false);
                            viewPagerTab.requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                //listView.onTouchEvent(event);
                if(action == MotionEvent.ACTION_DOWN)
                    return false;
                else
                    return true;
            }
        });


    }
}
