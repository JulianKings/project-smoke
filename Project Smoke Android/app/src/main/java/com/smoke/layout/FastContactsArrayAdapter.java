package com.smoke.layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smoke.R;
import com.smoke.activities.conversationActivity;
import com.smoke.activities.groupConversationActivity;
import com.smoke.core.Environment;
import com.smoke.util.management.GroupContact;
import com.smoke.util.management.PhoneContact;

import java.util.List;

public class FastContactsArrayAdapter extends ArrayAdapter<PhoneContact> {
    private final Context context;
    private final List<PhoneContact> values;

    public FastContactsArrayAdapter(Context context, List<PhoneContact> values) {
        super(context, R.layout.fast_contacts_list_view, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            View rowView = inflater.inflate(R.layout.fast_contacts_list_view, parent, false);
            PhoneContact currentContact = (values.get(position));
            if(!Environment.getAvailableContacts().containsKey(currentContact.getContactId()))
                Environment.getAvailableContacts().put(currentContact.getContactId(), currentContact);

            RelativeLayout conversationHolder = (RelativeLayout) rowView.findViewById(R.id.contactHolder);
            conversationHolder.setOnClickListener(new RelativeLayout.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView idText = (TextView) v.findViewById(R.id.contactId);
                    int id = Integer.parseInt(idText.getText() + "");

                    PhoneContact contact = Environment.getAvailableContacts().get(id);

                    if (contact instanceof GroupContact) {
                        Intent intent = new Intent(v.getContext(), groupConversationActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("conversationId", id);
                        intent.putExtras(b);
                        v.getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(v.getContext(), conversationActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("conversationId", id);
                        intent.putExtras(b);
                        v.getContext().startActivity(intent);
                    }
                }
            });

            TextView id = (TextView) rowView.findViewById(R.id.contactId);
            id.setText(currentContact.getContactId() + "");

            ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
            icon.setImageResource(currentContact.getContactIcon());

            return rowView;
        } else {
            return convertView;
        }
    }
}