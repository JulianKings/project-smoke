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
import com.smoke.util.management.ConversationMessage;
import com.smoke.util.management.ConversationMessageStatus;
import com.smoke.util.management.GroupContact;
import com.smoke.util.management.GroupList;
import com.smoke.util.management.GroupType;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.List;

public class GroupListArrayAdapter extends ArrayAdapter<GroupList> implements Insertable {
    private final Context context;
    private final List<GroupList> mList;

    public GroupListArrayAdapter(Context context, List<GroupList> values) {
        super(context, R.layout.group_list, values);
        this.context = context;
        this.mList = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            View rowView = inflater.inflate(R.layout.group_list, parent, false);
            GroupList groupList = (mList.get(position));

            RelativeLayout titleHolder = (RelativeLayout) rowView.findViewById(R.id.groupTitleHolder);
            titleHolder.setOnClickListener(new OnGroupBarTouch());
            titleHolder.setBackgroundColor(groupList.getListBackgroundColor());

            TextView titleText = (TextView) rowView.findViewById(R.id.groupBarTitle);
            titleText.setText(groupList.getListName());

            final ListView groups = (ListView) rowView.findViewById(R.id.groups);

            groupList.setAnimationUtil(AnimationUtils.forHeight(groups, 380));

            List<ConversationMessage> conversation = new ArrayList<ConversationMessage>();
            conversation.add(new ConversationMessage(1, "hello nigga ", true, ConversationMessageStatus.RECEIVED));
            conversation.add(new ConversationMessage(2, "hello nigga1111", false, ConversationMessageStatus.RECEIVED));

            List<ConversationMessage> conversation2 = new ArrayList<ConversationMessage>();
            conversation2.add(new ConversationMessage(1, "hello nigga ", true, ConversationMessageStatus.RECEIVED));
            conversation2.add(new ConversationMessage(2, "hello nigga!!!", false, ConversationMessageStatus.RECEIVED));

            List<GroupContact> popularGroups = new ArrayList<GroupContact>();
            popularGroups.add(new GroupContact(3, R.drawable.cat, "Catz", "❤❤❤", conversation, new ArrayList<Integer>(), 3, "07:45 am", GroupType.OPEN));
            popularGroups.add(new GroupContact(4, R.drawable.mario, "Mario fans !!", "Everyone favorite plumber", conversation2, new ArrayList<Integer>(), 4, "07:45 am", GroupType.OPEN));
            popularGroups.add(new GroupContact(5, R.drawable.winter, "The definitive game of thrones group", "Discuss about the series here ;D", conversation, new ArrayList<Integer>(), 15, "07:45 am", GroupType.LOCKED));
            popularGroups.add(new GroupContact(6, R.drawable.penguins, "Penguins are so lovely ❤❤", "Little thingies we love ❤", conversation2, new ArrayList<Integer>(), 9, "07:45 am", GroupType.PASSWORD));
            popularGroups.add(new GroupContact(7, R.drawable.cat, "Einstein", "My nigga", conversation, new ArrayList<Integer>(), 0, "07:45 am", GroupType.OPEN));
            popularGroups.add(new GroupContact(8, R.drawable.cat, "Einstein's Clone", "My nigga", conversation2, new ArrayList<Integer>(), 0, "07:45 am", GroupType.OPEN));
            popularGroups.add(new GroupContact(9, R.drawable.cat, "Einstein", "My nigga", conversation, new ArrayList<Integer>(), 0, "07:45 am", GroupType.OPEN));
            popularGroups.add(new GroupContact(10, R.drawable.cat, "Einstein's Clone", "My nigga", conversation2, new ArrayList<Integer>(), 0, "07:45 am", GroupType.OPEN));

            FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
            groups.setAdapter(new PopularGroupsAdapter(context, popularGroups, settings));


            View magicClick = rowView.findViewById(R.id.magicClick);
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
                    groups.onTouchEvent(event);
                    if(action == MotionEvent.ACTION_DOWN)
                        return false;
                    else
                        return true;
                }
            });

            // Remove if exist (reset)
            if(!Environment.getAvailableGroupLists().containsKey(groupList.getListId()))
                Environment.getAvailableGroupLists().remove(groupList.getListId());

            Environment.getAvailableGroupLists().put(groupList.getListId(), groupList);

            TextView id = (TextView) rowView.findViewById(R.id.groupListId);
            id.setText(groupList.getListId() + "");

            return rowView;
        } else {
            return convertView;
        }
    }

    @Override
    public GroupList getItem(final int position) {
        return mList.get(position);
    }

    /**
     * Returns the items.
     */
    public List<GroupList> getItems() {
        return mList;
    }

    @Override
    public void add(int index, Object item) {
        if (item instanceof GroupList) {
            mList.add(index, (GroupList) item);
            notifyDataSetChanged();
        }
    }
}