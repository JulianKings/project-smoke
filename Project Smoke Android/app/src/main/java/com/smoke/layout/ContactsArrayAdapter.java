package com.smoke.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.smoke.R;
import com.smoke.activities.conversationActivity;
import com.smoke.activities.groupConversationActivity;
import com.smoke.core.Environment;
import com.smoke.util.animations.AnimationUtils;
import com.smoke.util.management.ConversationMessage;
import com.smoke.util.management.GroupContact;
import com.smoke.util.management.PhoneContact;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.List;

public class ContactsArrayAdapter extends ArrayAdapter<PhoneContact> {
    private final Context context;
    private final List<PhoneContact> values;

    public ContactsArrayAdapter(Context context, List<PhoneContact> values) {
        super(context, R.layout.contacts_list_view, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            View rowView = inflater.inflate(R.layout.contacts_list_view, parent, false);
            PhoneContact currentContact = (values.get(position));
            if(!Environment.getAvailableContacts().containsKey(currentContact.getContactId()))
                Environment.getAvailableContacts().put(currentContact.getContactId(), currentContact);

            RelativeLayout contactHolder = (RelativeLayout) rowView.findViewById(R.id.contactHolder);
            if(position % 2 != 0)
                contactHolder.setBackgroundColor(Color.parseColor("#EBEBEB"));
            contactHolder.setOnClickListener(new OnContactTouch());

            RelativeLayout conversationHolder = (RelativeLayout) rowView.findViewById(R.id.conversationHolder);
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

            TextView name = (TextView) rowView.findViewById(R.id.contactName);
            name.setText(currentContact.getContactName());

            TextView description = (TextView) rowView.findViewById(R.id.contactDescription);
            description.setText(currentContact.getContactStatus());

            ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
            icon.setImageResource(currentContact.getContactIcon());

            RelativeLayout expandableLayout = (RelativeLayout) rowView.findViewById(R.id.conversationHolder);
            currentContact.setContactSmallConversationAnimator(new AnimationUtils(expandableLayout));

            RelativeLayout notifications = (RelativeLayout) rowView.findViewById(R.id.contactNotificationsHolder);
            if(currentContact.getContactPendingNotifications()<=0) { notifications.setVisibility(View.GONE); } else {
                TextView notificationsText = (TextView) rowView.findViewById(R.id.contactNotifications);
                if (currentContact.getContactPendingNotifications() < 10)
                {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(15, 2, 15, 2);
                    notificationsText.setLayoutParams(lp);
                }
                notificationsText.setText(currentContact.getContactPendingNotifications() + "");
            }

            TextView lastSeen = (TextView) rowView.findViewById(R.id.contactLastSeen);
            lastSeen.setText(currentContact.getContactLastConnection());

            DynamicListView conversationList = (DynamicListView) rowView.findViewById(R.id.conversationsList);
            if(currentContact instanceof GroupContact || currentContact.getContactConversation() == null)
            {
                int listSplit = 200;
                List<ConversationMessage> messages;
                if (((GroupContact) currentContact).getGroupConversations().size() > listSplit)
                    messages = new ArrayList<>(((GroupContact) currentContact).getGroupConversations().subList(((GroupContact) currentContact).getGroupConversations().size() - listSplit, ((GroupContact) currentContact).getGroupConversations().size()));
                else
                    messages = new ArrayList<>(((GroupContact) currentContact).getGroupConversations().subList(0, ((GroupContact) currentContact).getGroupConversations().size()));
                SmallGroupConversationArrayAdapter messagesAdapter = (new SmallGroupConversationArrayAdapter(context, messages));
                AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
                animationAdapter.setAbsListView(conversationList);
                conversationList.setAdapter(animationAdapter);
                // enable scrolling inside
                conversationList.setOnTouchListener(new ListView.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }

                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });
                conversationList.setSelection(messagesAdapter.getCount() - 1); // position at last message
            } else {
                int listSplit = 200;
                List<ConversationMessage> messages;
                if (currentContact.getContactConversation().size() > listSplit)
                    messages = new ArrayList<>(currentContact.getContactConversation().subList(currentContact.getContactConversation().size() - listSplit, currentContact.getContactConversation().size()));
                else
                    messages = new ArrayList<>(currentContact.getContactConversation().subList(0, currentContact.getContactConversation().size()));
                SmallConversationArrayAdapter messagesAdapter = (new SmallConversationArrayAdapter(context, messages));
                AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
                animationAdapter.setAbsListView(conversationList);
                conversationList.setAdapter(animationAdapter);
                // enable scrolling inside
                conversationList.setOnTouchListener(new ListView.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }

                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });
                conversationList.setSelection(messagesAdapter.getCount() - 1); // position at last message
            }
            startEmoji(rowView);
            return rowView;
        } else {
            PhoneContact currentContact = (values.get(position));
            DynamicListView conversationList = (DynamicListView) convertView.findViewById(R.id.conversationsList);
            if(currentContact instanceof GroupContact || currentContact.getContactConversation() == null)
            {
                int listSplit = 200;
                List<ConversationMessage> messages;
                if (((GroupContact) currentContact).getGroupConversations().size() > listSplit)
                    messages = new ArrayList<>(((GroupContact) currentContact).getGroupConversations().subList(((GroupContact) currentContact).getGroupConversations().size() - listSplit, ((GroupContact) currentContact).getGroupConversations().size()));
                else
                    messages = new ArrayList<>(((GroupContact) currentContact).getGroupConversations().subList(0, ((GroupContact) currentContact).getGroupConversations().size()));
                SmallGroupConversationArrayAdapter messagesAdapter = (new SmallGroupConversationArrayAdapter(context, messages));
                AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
                animationAdapter.setAbsListView(conversationList);
                conversationList.setAdapter(animationAdapter);
                // enable scrolling inside
                conversationList.setOnTouchListener(new ListView.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }

                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });
                conversationList.setSelection(messagesAdapter.getCount() - 1); // position at last message
            } else {
                int listSplit = 200;
                List<ConversationMessage> messages;
                if (currentContact.getContactConversation().size() > listSplit)
                    messages = new ArrayList<>(currentContact.getContactConversation().subList(currentContact.getContactConversation().size() - listSplit, currentContact.getContactConversation().size()));
                else
                    messages = new ArrayList<>(currentContact.getContactConversation().subList(0, currentContact.getContactConversation().size()));
                SmallConversationArrayAdapter messagesAdapter = (new SmallConversationArrayAdapter(context, messages));
                AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
                animationAdapter.setAbsListView(conversationList);
                conversationList.setAdapter(animationAdapter);
                // enable scrolling inside
                conversationList.setOnTouchListener(new ListView.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow previous listView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }

                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });
                conversationList.setSelection(messagesAdapter.getCount() - 1); // position at last message
            }
            return convertView;
        }
    }



    public void startEmoji(View baseView)
    {
        final ImageView emojiButton = (ImageView) baseView.findViewById(R.id.emojiMiniButton);
        final EditText emojiconEditText = (EditText) baseView.findViewById(R.id.conversationAnswer);
        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(((Activity)context).findViewById(R.id.conversationSuperHolder), context);

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, R.drawable.orca_emoji_category_people);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if(popup.isShowing())
                    popup.dismiss();
            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojiconEditText == null || emojicon == null) {
                    return;
                }

                int start = emojiconEditText.getSelectionStart();
                int end = emojiconEditText.getSelectionEnd();
                if (start < 0) {
                    emojiconEditText.append(emojicon.getEmoji());
                } else {
                    emojiconEditText.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiconEditText.dispatchKeyEvent(event);
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        emojiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if(!popup.isShowing()){

                    //If keyboard is visible, simply show the emoji popup
                    if(popup.isKeyBoardOpen()){
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else{
                        emojiconEditText.setFocusableInTouchMode(true);
                        emojiconEditText.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else{
                    popup.dismiss();
                }
            }
        });
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }
}
