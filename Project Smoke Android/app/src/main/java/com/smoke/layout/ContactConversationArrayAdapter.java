package com.smoke.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.util.Insertable;
import com.smoke.R;
import com.smoke.util.management.ConversationMessage;

import java.util.List;

public class ContactConversationArrayAdapter extends ArrayAdapter<ConversationMessage> implements Insertable {
    private final Context context;
    private final List<ConversationMessage> mList;

    public ContactConversationArrayAdapter(Context context, List<ConversationMessage> values) {
        super(context, R.layout.conversation_list_view, values);
        this.context = context;
        this.mList = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ConversationMessage currentMessage = (mList.get(position));

        if(currentMessage.isMine())
        {
            View rowView = inflater.inflate(R.layout.conversation_list_view_right, parent, false);

            RelativeLayout messageHolder = (RelativeLayout) rowView.findViewById(R.id.messageHolder);
            TextView conversationMessage = (TextView) rowView.findViewById(R.id.conversationMessage);
            conversationMessage.setText(currentMessage.getConversationMessage());

            return rowView;
        } else {
            View rowView = inflater.inflate(R.layout.conversation_list_view, parent, false);

            RelativeLayout messageHolder = (RelativeLayout) rowView.findViewById(R.id.messageHolder);
            TextView conversationMessage = (TextView) rowView.findViewById(R.id.conversationMessage);
            conversationMessage.setText(currentMessage.getConversationMessage());

            return rowView;
        }
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
