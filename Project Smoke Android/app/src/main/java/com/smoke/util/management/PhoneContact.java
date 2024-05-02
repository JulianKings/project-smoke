package com.smoke.util.management;

import com.smoke.util.animations.AnimationUtils;

import java.util.List;

public class PhoneContact {
    private int contactId;
    private int contactIcon;
    private String contactName;
    private String contactStatus;
    private boolean contactSmallConversation;
    private AnimationUtils contactSmallConversationAnimator;
    private List<ConversationMessage> contactConversation;
    private int contactPendingNotifications;
    private String contactLastConnection;

    public PhoneContact(int id, int icon, String name, String status, List<ConversationMessage> conversations, int notifications, String lastConnection)
    {
        contactId = id;
        contactIcon = icon;
        contactName = name;
        contactStatus = status;
        contactSmallConversation = false;
        contactSmallConversationAnimator = null;
        contactConversation = conversations;
        contactPendingNotifications = notifications;
        contactLastConnection = lastConnection;
    }

    public int getContactId() { return contactId; }

    public int getContactIcon() { return contactIcon; }

    public String getContactName() { return contactName; }

    public String getContactStatus()
    {
        return contactStatus;
    }

    public boolean getContactSmallConversation()
    {
        return contactSmallConversation;
    }

    public List<ConversationMessage> getContactConversation() { return contactConversation; }

    public AnimationUtils getContactSmallConversationAnimator() { return contactSmallConversationAnimator; }

    public int getContactPendingNotifications() { return contactPendingNotifications; }

    public String getContactLastConnection() { return contactLastConnection; }

    public void setContactSmallConversationAnimator(AnimationUtils util) { this.contactSmallConversationAnimator = util; }

    public void setContactSmallConversation(boolean smallConversation)
    {
        this.contactSmallConversation = smallConversation;
    }

    public void updateContactLastConnection(String lastConnection) { this.contactLastConnection = lastConnection; }
}
