package com.smoke.util.management;

public class ConversationMessage {
    private Integer conversationOwner;
    private String conversationMessage;
    private boolean conversationIsMine;
    private ConversationMessageStatus conversationMessageStatus;

    public ConversationMessage(int owner, String message, boolean me, ConversationMessageStatus status)
    {
        this.conversationOwner = owner;
        this.conversationMessage = message;
        this.conversationIsMine = me;
        this.conversationMessageStatus = status;
    }

    public Integer getConversationMessageOwner() { return conversationOwner; }

    public String getConversationMessage()
    {
        return conversationMessage;
    }

    public boolean isMine()
    {
        return conversationIsMine;
    }

    public ConversationMessageStatus getConversationMessageStatus() { return conversationMessageStatus; }

    public void setConversationMessageStatus(ConversationMessageStatus update) { this.conversationMessageStatus = update; }
}
