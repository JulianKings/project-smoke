package com.smoke.util.management;

import java.util.List;

public class GroupContact extends PhoneContact {
    private List<ConversationMessage> groupConversations;
    private List<Integer> groupMembers;
    private GroupType groupType;

    public GroupContact(int id, int icon, String name, String status, List<ConversationMessage> conversations, List<Integer> members, int notifications, String lastConnection, GroupType type) {
        super(id, icon, name, status, null, notifications, lastConnection);
        groupConversations = conversations;
        groupMembers = members;
        groupType = type;
    }

    public List<ConversationMessage> getGroupConversations()
    {
        return groupConversations;
    }

    public List<Integer> getGroupMembers() { return this.groupMembers; }

    public GroupType getGroupType() { return this.groupType;}
}
