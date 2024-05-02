package com.smoke.util.management;

import java.util.List;

public class EventContact extends GroupContact {
    protected int eventDuration;
    public EventContact(int id, int icon, String name, String status, List<ConversationMessage> conversations, List<Integer> members, int duration, int notifications, String lastConnection, GroupType type) {
        super(id, icon, name, status, conversations, members, notifications, lastConnection, type);
        eventDuration = duration;
    }

    public int getEventDuration() { return eventDuration; }
}
