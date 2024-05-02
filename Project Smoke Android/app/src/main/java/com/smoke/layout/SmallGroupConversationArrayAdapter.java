package com.smoke.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nhaarman.listviewanimations.util.Insertable;
import com.smoke.R;
import com.smoke.util.management.ConversationMessage;

import java.util.List;

public class SmallGroupConversationArrayAdapter extends ArrayAdapter<ConversationMessage> implements Insertable {
    private final Context context;
    private final List<ConversationMessage> mList;

    public SmallGroupConversationArrayAdapter(Context context, List<ConversationMessage> values) {
        super(context, R.layout.conversation_list_view, values);
        this.context = context;
        this.mList = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.conversation_list_view, parent, false);

        RelativeLayout messageHolder = (RelativeLayout) rowView.findViewById(R.id.messageHolder);
        TextView conversationMessage = (TextView) rowView.findViewById(R.id.conversationMessage);
        ConversationMessage currentMessage = (mList.get(position));
        conversationMessage.setText(currentMessage.getConversationMessage());

        if(currentMessage.isMine())
        {
            messageHolder.setBackgroundResource(R.drawable.conversation_message_me);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(175, 0, 0, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            messageHolder.setLayoutParams(lp);
        }
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
