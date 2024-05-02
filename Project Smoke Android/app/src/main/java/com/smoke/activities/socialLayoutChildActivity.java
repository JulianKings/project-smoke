package com.smoke.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.layout.ContactsArrayAdapter;
import com.smoke.layout.GroupListArrayAdapter;
import com.smoke.layout.PopularGroupsAdapter;
import com.smoke.util.management.ConversationMessage;
import com.smoke.util.management.GroupList;
import com.smoke.util.management.PhoneContact;
import com.yalantis.contextmenu.lib.Utils;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.List;

public class socialLayoutChildActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View currentView =  inflater.inflate(R.layout.social_layout, container, false);
        return currentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());

        final ListView listView = (ListView) getView().findViewById(R.id.groupsList);

        List<GroupList> groupLists = new ArrayList<>();
        groupLists.add(new GroupList(1, "Most popular groups", "#BD043E", null));
        groupLists.add(new GroupList(2, "Recommended groups", "#05097C", null));
        groupLists.add(new GroupList(3, "Your groups", "#039A30", null));

        GroupListArrayAdapter contactsAdapter = (new GroupListArrayAdapter(this.getActivity(), groupLists));
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
