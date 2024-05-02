package com.smoke.layout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nhaarman.listviewanimations.util.Insertable;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.smoke.R;
import com.smoke.activities.mainLayoutActivity;
import com.smoke.core.Environment;
import com.smoke.util.animations.AnimationUtils;
import com.smoke.util.management.*;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.List;

public class EventListArrayAdapter extends ArrayAdapter<EventList> implements Insertable {
    private final Context context;
    private final List<EventList> mList;

    public EventListArrayAdapter(Context context, List<EventList> values) {
        super(context, R.layout.events_list, values);
        this.context = context;
        this.mList = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            View rowView = inflater.inflate(R.layout.events_list, parent, false);
            EventList eventList = (mList.get(position));

            RelativeLayout titleHolder = (RelativeLayout) rowView.findViewById(R.id.eventTitleHolder);
            titleHolder.setOnClickListener(new OnEventBarTouch());
            titleHolder.setBackgroundColor(eventList.getListBackgroundColor());

            TextView titleText = (TextView) rowView.findViewById(R.id.eventBarTitle);
            titleText.setText(eventList.getListName());

            final ListView eventsList = (ListView) rowView.findViewById(R.id.events);

            eventList.setAnimationUtil(AnimationUtils.forHeight(eventsList, 380));

            List<ConversationMessage> conversation = new ArrayList<ConversationMessage>();
            conversation.add(new ConversationMessage(1, "hello nigga ", true, ConversationMessageStatus.RECEIVED));
            conversation.add(new ConversationMessage(2, "hello nigga1111", false, ConversationMessageStatus.RECEIVED));

            List<ConversationMessage> conversation2 = new ArrayList<ConversationMessage>();
            conversation2.add(new ConversationMessage(1, "hello nigga ", true, ConversationMessageStatus.RECEIVED));
            conversation2.add(new ConversationMessage(2, "hello nigga!!!", false, ConversationMessageStatus.RECEIVED));

            List<EventContact> events = new ArrayList<EventContact>();
            events.add(new EventContact(3, R.drawable.cat, "Einstein", "My nigga", conversation, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));
            events.add(new EventContact(4, R.drawable.cat, "Einstein's Clone", "My nigga", conversation2, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));
            events.add(new EventContact(5, R.drawable.cat, "Einstein", "My nigga", conversation, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));
            events.add(new EventContact(6, R.drawable.cat, "Einstein's Clone", "My nigga", conversation2, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));
            events.add(new EventContact(7, R.drawable.cat, "Einstein", "My nigga", conversation, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));
            events.add(new EventContact(8, R.drawable.cat, "Einstein's Clone", "My nigga", conversation2, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));
            events.add(new EventContact(9, R.drawable.cat, "Einstein", "My nigga", conversation, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));
            events.add(new EventContact(10, R.drawable.cat, "Einstein's Clone", "My nigga", conversation2, new ArrayList<Integer>(), 60, 0, "07:45", GroupType.OPEN));

            FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
            eventsList.setAdapter(new PopularEventsAdapter(context, events, settings));


            View magicClick = rowView.findViewById(R.id.superClick);
            magicClick.setOnTouchListener(new ListView.OnTouchListener() {
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
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow previous listView to intercept touch events.
                                viewPager.requestDisallowInterceptTouchEvent(false);
                                viewPagerTab.requestDisallowInterceptTouchEvent(false);
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    eventsList.onTouchEvent(event);
                    if(action == MotionEvent.ACTION_DOWN)
                        return false;
                    else
                        return true;
                }
            });

            // update if exist
            if(Environment.getAvailableEventLists().containsKey(eventList.getListId()))
                Environment.getAvailableEventLists().remove(eventList.getListId());

            Environment.getAvailableEventLists().put(eventList.getListId(), eventList);

            TextView id = (TextView) rowView.findViewById(R.id.eventListId);
            id.setText(eventList.getListId() + "");

            return rowView;
        } else {
            return convertView;
        }
    }

    @Override
    public EventList getItem(final int position) {
        return mList.get(position);
    }

    /**
     * Returns the items.
     */
    public List<EventList> getItems() {
        return mList;
    }

    @Override
    public void add(int index, Object item) {
        if (item instanceof EventList) {
            mList.add(index, (EventList) item);
            notifyDataSetChanged();
        }
    }
}