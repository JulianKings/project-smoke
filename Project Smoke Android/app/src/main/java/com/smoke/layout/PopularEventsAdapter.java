package com.smoke.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.smoke.R;
import com.smoke.util.management.EventContact;
import com.smoke.util.management.PhoneContact;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.List;

public class PopularEventsAdapter extends BaseFlipAdapter<EventContact> {
    private final int PAGES = 3;
    private final Context context;
    private final List<EventContact> mList;

    public PopularEventsAdapter(Context context, List<EventContact> items, FlipSettings settings) {
        super(context, items, settings);
        this.context = context;
        this.mList = items;
    }


    @Override
    public View getPage(int position, View convertView,ViewGroup parent, EventContact leftFriend, EventContact rightFriend) {
        final EventsHolder holder;
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new EventsHolder();
            convertView = inflater.inflate(R.layout.events_box_list, parent, false);
            holder.leftAvatarHolder = (RelativeLayout) convertView.findViewById(R.id.leftImageHolder);
            holder.leftAvatarName = (TextView) convertView.findViewById(R.id.leftImageText);
            holder.rightAvatarHolder = (RelativeLayout) convertView.findViewById(R.id.rightImageHolder);
            holder.rightAvatarName = (TextView) convertView.findViewById(R.id.rightImageText);
            holder.infoPage = inflater.inflate(R.layout.events_box_list_more, parent, false);
            holder.eventName = (TextView) holder.infoPage.findViewById(R.id.eventName);
            holder.eventDescription = (TextView) holder.infoPage.findViewById(R.id.eventDescription);
            holder.eventNotificationsHolder = (RelativeLayout) holder.infoPage.findViewById(R.id.eventNotificationsCount);
            holder.eventNotifications = (TextView) holder.infoPage.findViewById(R.id.eventNotifications);
            holder.eventStatus = (ImageView) holder.infoPage.findViewById(R.id.eventStatusIcon);
            holder.eventMembers = (TextView) holder.infoPage.findViewById(R.id.eventMembers);
            holder.eventOpenProfile = (RelativeLayout) holder.infoPage.findViewById(R.id.eventOpenProfile);

            convertView.setTag(holder);
        } else {
            holder = (EventsHolder) convertView.getTag();
        }

        switch (position) {
            // Merged page with 2 friends
            case 1:
                holder.leftAvatarHolder.setBackground(context.getResources().getDrawable(leftFriend.getContactIcon()));
                holder.leftAvatarName.setText(leftFriend.getContactName());
                if (rightFriend != null) {
                    holder.rightAvatarHolder.setBackground(context.getResources().getDrawable(rightFriend.getContactIcon()));
                    holder.rightAvatarName.setText(rightFriend.getContactName());
                }
                break;
            default:
                fillHolder(holder, position == 0 ? leftFriend : rightFriend);
                holder.infoPage.setTag(holder);
                return holder.infoPage;
        }
        return convertView;
    }

    @Override
    public int getPagesCount() {
        return PAGES;
    }

    private void fillHolder(EventsHolder holder, EventContact contact) {
        if (contact == null)
            return;

        holder.infoPage.setBackgroundColor(context.getResources().getColor(R.color.gplus_color_1));
        holder.eventName.setText(contact.getContactName());
        holder.eventDescription.setText(contact.getContactStatus());

        if(contact.getContactPendingNotifications()<=0) { holder.eventNotificationsHolder.setVisibility(View.INVISIBLE); holder.eventOpenProfile.setVisibility(View.VISIBLE); } else {
            if (contact.getContactPendingNotifications() < 10)
            {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(14, 4, 14, 4);
                holder.eventNotifications.setLayoutParams(lp);
            }
            holder.eventNotifications.setText(contact.getContactPendingNotifications() + "");
        }



        switch (contact.getGroupType())
        {
            default:
            case OPEN: {
                holder.eventStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.open));
                break;
            }
            case LOCKED: {
                holder.eventStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.locked));
                break;
            }
            case PASSWORD: {
                holder.eventStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.password));
                break;
            }
        }

        holder.eventMembers.setText(contact.getGroupMembers().size()+"");
    }

    class EventsHolder {
        RelativeLayout leftAvatarHolder;
        TextView leftAvatarName;
        RelativeLayout rightAvatarHolder;
        TextView rightAvatarName;
        View infoPage;

        TextView eventName;
        TextView eventDescription;
        RelativeLayout eventNotificationsHolder;
        TextView eventNotifications;
        ImageView eventStatus;
        TextView eventMembers;
        RelativeLayout eventOpenProfile;
    }
}
