package com.smoke.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.layout.ContactConversationArrayAdapter;
import com.smoke.util.management.ConversationMessage;
import com.smoke.util.management.ConversationMessageStatus;
import com.smoke.util.management.PhoneContact;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;
import de.hdodenhof.circleimageview.CircleImageView;
import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.List;

public class groupActivity extends Activity implements OnMenuItemClickListener,OnMenuItemLongClickListener {

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    private DialogFragment mMenuDialogFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Environment.updateActivity(this);
        setContentView(R.layout.conversation_layout);

        PhoneContact currentContact = Environment.getAvailableContacts().get(getIntent().getExtras().getInt("conversationId"));
        initMenuFragment();

        final ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.conversation_actionbar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        CircleImageView actionBarLogo = (CircleImageView) getActionBar().getCustomView().findViewById(R.id.actionBarLogo);
        actionBarLogo.setImageResource(currentContact.getContactIcon());

        TextView actionBarTitle = (TextView) getActionBar().getCustomView().findViewById(R.id.actionBarTitle);
        actionBarTitle.setText(currentContact.getContactName());

        TextView actionBarStatus = (TextView) getActionBar().getCustomView().findViewById(R.id.actionBarDescription);
        actionBarStatus.setText(currentContact.getContactStatus());

        // Do the job
        DynamicListView conversationList = (DynamicListView) findViewById(R.id.contactConversationList);
        int listSplit = 200;
        List<ConversationMessage> messages;
        if (currentContact.getContactConversation().size() > listSplit)
            messages = new ArrayList<>(currentContact.getContactConversation().subList(currentContact.getContactConversation().size() - listSplit, currentContact.getContactConversation().size()));
        else
            messages = new ArrayList<>(currentContact.getContactConversation().subList(0, currentContact.getContactConversation().size()));
        ContactConversationArrayAdapter messagesAdapter = (new ContactConversationArrayAdapter(this, messages));
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
        animationAdapter.setAbsListView(conversationList);
        conversationList.setAdapter(animationAdapter);
        if(conversationList.getAdapter().getCount()>15) { conversationList.setSelection(conversationList.getAdapter().getCount()-1);}
        if(currentContact.getContactConversation().size()>listSplit)
        {
            // Add view (scroll 15 more)
            View header = View.inflate(this, R.layout.conversation_select_more, null);
            conversationList.addHeaderView(header);
        }

        startEmoji();
    }

    public void startEmoji()
    {
        final ImageView emojiButton = (ImageView) findViewById(R.id.emojiButton);
        final EditText emojiconEditText = (EditText) findViewById(R.id.conversationResponse);
        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(findViewById(R.id.conversationGiantHolder), this);

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, R.drawable.orca_emoji_category_people);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {

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
        popup.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

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
        popup.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiconEditText.dispatchKeyEvent(event);
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        emojiButton.setOnClickListener(new OnClickListener() {

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
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                mMenuDialogFragment.show(getFragmentManager(), "ContextMenuDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject("Exit"), seeContact = new MenuObject("See contact"), removeFriend = new MenuObject("Remove from friends"), search =
            new MenuObject("Search"), clearChat = new MenuObject("Clear chat"), mute = new MenuObject("Mute user"), ban = new MenuObject("Block user"),
            settings = new MenuObject("Settings");
        close.setResource(R.drawable.exit);
        seeContact.setResource(R.drawable.see_contact);
        removeFriend.setResource(R.drawable.remove_from_friends);
        search.setResource(R.drawable.search);
        clearChat.setResource(R.drawable.clear_chat);
        mute.setResource(R.drawable.mute_user);
        ban.setResource(R.drawable.block_user_new);
        settings.setResource(R.drawable.settings);

        menuObjects.add(close); menuObjects.add(seeContact); menuObjects.add(removeFriend); menuObjects.add(search); menuObjects.add(clearChat);
        menuObjects.add(mute); menuObjects.add(ban); menuObjects.add(settings);
        return menuObjects;
    }

    public void onResponseSendClick(View v)
    {
        PhoneContact currentContact = Environment.getAvailableContacts().get(getIntent().getExtras().getInt("conversationId"));
        View parent = (View)v.getParent().getParent();
        synchronized (currentContact.getContactConversation()) {
            if (parent != null) {
                EditText message = (EditText) parent.findViewById(R.id.conversationResponse);
                String rawMessage = message.getText().toString();
                if (rawMessage.length() > 0) {
                    DynamicListView conversationList = (DynamicListView) parent.findViewById(R.id.contactConversationList);
                    conversationList.insert(conversationList.getAdapter().getCount(), new ConversationMessage(0, rawMessage, true, ConversationMessageStatus.SENT));
                    conversationList.setSelection(conversationList.getAdapter().getCount() - 1); // position at last message
                    message.setText("");
                    /*if (message.hasFocus()) {
                        message.clearFocus();
                    }*/
                    currentContact.getContactConversation().add(new ConversationMessage(0, rawMessage, true, ConversationMessageStatus.SENT));
                }
            }
        }
    }
}
