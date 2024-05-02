package com.smoke.layout;

import android.view.View;
import android.widget.TextView;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.util.management.PhoneContact;

public class OnContactTouch implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        TextView idText = (TextView) v.findViewById(R.id.contactId);
        int id = Integer.parseInt(idText.getText()+"");
        PhoneContact currentContact = Environment.getAvailableContacts().get(id);
        currentContact.setContactSmallConversation(v.findViewById(R.id.conversationHolder).getVisibility() == View.VISIBLE);
        if(!currentContact.getContactSmallConversation() && currentContact.getContactSmallConversationAnimator() != null) {
            currentContact.getContactSmallConversationAnimator().expandHeight();
        } else if(currentContact.getContactSmallConversationAnimator() != null) {
            currentContact.getContactSmallConversationAnimator().collapseHeight();
        }
    }
}
