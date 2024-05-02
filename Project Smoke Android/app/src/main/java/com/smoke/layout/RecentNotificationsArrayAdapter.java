package com.smoke.layout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nhaarman.listviewanimations.util.Insertable;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.util.management.ConversationMessage;
import com.smoke.util.management.PhoneContact;

import java.util.List;

public class RecentNotificationsArrayAdapter extends ArrayAdapter<ConversationMessage> implements Insertable {
    private final Context context;
    private final List<ConversationMessage> mList;

    public RecentNotificationsArrayAdapter(Context context, List<ConversationMessage> values) {
        super(context, R.layout.recent_notifications_list_view, values);
        this.context = context;
        this.mList = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.recent_notifications_list_view, parent, false);

        RelativeLayout messageHolder = (RelativeLayout) rowView.findViewById(R.id.notificationHolder);
        ConversationMessage currentMessage = (mList.get(position));

        PhoneContact notificationOwner = Environment.getAvailableContacts().get(currentMessage.getConversationMessageOwner());

        TextView notificationContent = (TextView) rowView.findViewById(R.id.notificationText);
        notificationContent.setText(Html.fromHtml(currentMessage.getConversationMessage() + " <font color='#e1e1e1'>99:99xx</font>"));

        ImageView icon = (ImageView) rowView.findViewById(R.id.notificationIcon);
        icon.setImageResource(notificationOwner.getContactIcon());
        return rowView;
    }

    @Override
    public ConversationMessage getItem(final int position) {
        return mList.get(position);
    }

    /**
     * Returns the items.
     */
    public List<ConversationMessage> getItems() {
        return mList;
    }

    @Override
    public void add(int index, Object item) {
        if(item instanceof ConversationMessage) {
            mList.add(index, (ConversationMessage) item);
            notifyDataSetChanged();
        }
    }
}
