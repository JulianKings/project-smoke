package com.smoke.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.smoke.R;
import com.smoke.util.management.GroupContact;
import com.smoke.util.management.GroupType;
import com.smoke.util.management.PhoneContact;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import org.w3c.dom.Text;

import java.util.List;

public class PopularGroupsAdapter extends BaseFlipAdapter<GroupContact> {
    private final int PAGES = 3;
    private final Context context;
    private final List<GroupContact> mList;

    public PopularGroupsAdapter(Context context, List<GroupContact> items, FlipSettings settings) {
        super(context, items, settings);
        this.context = context;
        this.mList = items;
    }


    @Override
    public View getPage(int position, View convertView,ViewGroup parent, GroupContact leftFriend, GroupContact rightFriend) {
        final GroupsHolder holder;
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new GroupsHolder();
            convertView = inflater.inflate(R.layout.groups_box_list, parent, false);
            holder.leftAvatarHolder = (RelativeLayout) convertView.findViewById(R.id.leftImageHolder);
            holder.leftAvatarName = (TextView) convertView.findViewById(R.id.leftImageText);
            holder.rightAvatarHolder = (RelativeLayout) convertView.findViewById(R.id.rightImageHolder);
            holder.rightAvatarName = (TextView) convertView.findViewById(R.id.rightImageText);
            holder.infoPage = inflater.inflate(R.layout.groups_box_list_more, parent, false);
            holder.groupName = (TextView) holder.infoPage.findViewById(R.id.groupName);
            holder.groupDescription = (TextView) holder.infoPage.findViewById(R.id.groupDescription);
            holder.groupNotificationsHolder = (RelativeLayout) holder.infoPage.findViewById(R.id.groupNotificationsCount);
            holder.groupNotifications = (TextView) holder.infoPage.findViewById(R.id.groupNotifications);
            holder.groupStatus = (ImageView) holder.infoPage.findViewById(R.id.groupStatusIcon);
            holder.groupMembers = (TextView) holder.infoPage.findViewById(R.id.groupMembers);
            holder.groupOpenProfile = (RelativeLayout) holder.infoPage.findViewById(R.id.groupOpenProfile);

            convertView.setTag(holder);
        } else {
            holder = (GroupsHolder) convertView.getTag();
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

    private void fillHolder(GroupsHolder holder, GroupContact contact) {
        if (contact == null)
            return;

        holder.infoPage.setBackground(context.getResources().getDrawable(contact.getContactIcon()));
        holder.groupName.setText(contact.getContactName());
        holder.groupDescription.setText(contact.getContactStatus());

        if(contact.getContactPendingNotifications()<=0) { holder.groupNotificationsHolder.setVisibility(View.INVISIBLE); holder.groupOpenProfile.setVisibility(View.VISIBLE); } else {
            if (contact.getContactPendingNotifications() < 10)
            {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(14, 4, 14, 4);
                holder.groupNotifications.setLayoutParams(lp);
            }
            holder.groupNotifications.setText(contact.getContactPendingNotifications() + "");
        }

        switch (contact.getGroupType())
        {
            default:
            case OPEN: {
                holder.groupStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.open));
                break;
            }
            case LOCKED: {
                holder.groupStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.locked));
                break;
            }
            case PASSWORD: {
                holder.groupStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.password));
                break;
            }
        }

        holder.groupMembers.setText(contact.getGroupMembers().size()+"");
    }

    class GroupsHolder {
        RelativeLayout leftAvatarHolder;
        TextView leftAvatarName;
        RelativeLayout rightAvatarHolder;
        TextView rightAvatarName;
        View infoPage;

        TextView groupName;
        TextView groupDescription;
        RelativeLayout groupNotificationsHolder;
        TextView groupNotifications;
        ImageView groupStatus;
        TextView groupMembers;
        RelativeLayout groupOpenProfile;
    }
}
