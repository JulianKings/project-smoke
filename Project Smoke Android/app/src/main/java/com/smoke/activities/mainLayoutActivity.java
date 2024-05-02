package com.smoke.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.layout.ContactConversationArrayAdapter;
import com.smoke.layout.GroupConversationArrayAdapter;
import com.smoke.services.basicControlService;
import com.smoke.util.animations.AnimationUtils;
import com.smoke.util.management.ConversationMessage;
import com.smoke.util.management.ConversationMessageStatus;
import com.smoke.util.management.GroupContact;
import com.smoke.util.management.PhoneContact;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class mainLayoutActivity extends FragmentActivity implements OnMenuItemClickListener,OnMenuItemLongClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean firstResumed = false;
    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if(position == 3)
        {
            // Search group
            Intent intent = new Intent(clickedView.getContext(), searchGroupActivity.class);
            clickedView.getContext().startActivity(intent);
        } else {
            Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Environment.updateLastKnownLocation(LocationServices.FusedLocationApi.getLastLocation(Environment.getGoogleApiClient()));
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public synchronized void buildGoogleApiClient()
    {
       /* Environment.updateGoogleApiClient(new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build());

        Environment.getGoogleApiClient().connect(); // Start the service

        Environment.updateLastKnownLocation(LocationServices.FusedLocationApi.getLastLocation(Environment.getGoogleApiClient()));

        // create location update request
        /*LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Update on low devices

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) new LocationUpdater());*/
    }

    private DialogFragment mMenuDialogFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        Environment.onInitialize(this);

        // Start service
        startService(new Intent(getBaseContext(), basicControlService.class));

        SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);

        Log.d("onCreate", "onCreate happened -> " + settings.contains("countryCode"));

        if(getIntent().hasExtra("countryCode") && getIntent().hasExtra("phoneNumber") && getIntent().hasExtra("userImage") && getIntent().hasExtra("userName"))
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("countryCode", getIntent().getExtras().getInt("countryCode"));
            editor.putString("phoneNumber", getIntent().getExtras().getString("phoneNumber"));
            editor.putString("userImage", getIntent().getExtras().getString("userImage"));
            editor.putString("userName", getIntent().getExtras().getString("userName"));
            editor.apply();
            editor.commit();
        }


        if (!settings.contains("countryCode") || !settings.contains("phoneNumber") || !settings.contains("userImage") || !settings.contains("userName")) {
            Intent intent = new Intent(this, welcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, 0);
            return;
        }

        if(!settings.getBoolean("appInitialized", false))
        {
            Intent intent = new Intent(this, selectAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, 0);
            return;
        }


        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
        Environment.updateActivity(this);
        setContentView(R.layout.main_layout);
        // Add status if missing
        if(!settings.contains("userStatus"))
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("userStatus", "Hi, I'm new :D");
            editor.apply();
            editor.commit();
        }

        reInstance();

        // Start google api
        buildGoogleApiClient();

        // Round menu
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(android.R.drawable.ic_input_add);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.drawable.new_conversation);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.safe_conversation);
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.search);
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        Resources r = getResources();
        float buttonSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        float buttonMarginLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());
        float buttonMarginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
        FloatingActionButton.LayoutParams buttonParams = new FloatingActionButton.LayoutParams(Math.round(buttonSize), Math.round(buttonSize));
        buttonParams.setMargins(Math.round(buttonMarginLeft), 0, 0, Math.round(buttonMarginBottom));
        float imageSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
        FloatingActionButton.LayoutParams imageParams = new FloatingActionButton.LayoutParams(Math.round(imageSize), Math.round(imageSize));
        imageParams.setMargins(2,2,2,2);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon, imageParams)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_LEFT)
                .setLayoutParams(buttonParams)

                .build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .setRadius(210)
                .setStartAngle(-90)
                .setEndAngle(0)
                .build();
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

        final List<MenuObject> menuObjects = new ArrayList<>();
        MenuObject close = new MenuObject("Exit"), contacts = new MenuObject("My contacts"), friends = new MenuObject("My friends"), searchGroups = new MenuObject("Search Groups"),
                betaOptions = new MenuObject("Beta Options"), settings = new MenuObject("Settings");
        close.setResource(R.drawable.exit);
        contacts.setResource(R.drawable.contacts);
        friends.setResource(R.drawable.see_contact);
        searchGroups.setResource(R.drawable.see_contact);
        betaOptions.setResource(R.drawable.gear);
        settings.setResource(R.drawable.settings);

        menuObjects.add(close); menuObjects.add(contacts); menuObjects.add(friends); menuObjects.add(searchGroups); menuObjects.add(betaOptions); menuObjects.add(settings);
        return menuObjects;
    }


    @Override
    public void onBackPressed(){
        moveTaskToBack(true); // Don't close the application
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!firstResumed)
        {
            firstResumed = true;
        }
        SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        Log.d("onResume", "onResume happened -> " + settings.getBoolean("appInitialized", false));

        Environment.onResume();

        if (!settings.contains("countryCode") || !settings.contains("phoneNumber") || !settings.contains("userImage") || !settings.contains("userName")) {
            Intent intent = new Intent(this, welcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, 0);
            return;
        }

        if(settings.getBoolean("appInitialized", false) == false)
        {
            Intent intent = new Intent(this, selectAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, 0);
            return;
        } else {

            if (firstResumed) {
                if (settings.contains("appInitialized") && settings.getBoolean("appInitialized", false)) {
                    reInstance();
                }
            }
        }

    }

    /*@Override
    public void onStop()
    {
        // ?
        Log.d("onStop", "onStop Happened");
        SharedPreferences myPrefs = this.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        myPrefs.edit().putBoolean("appInitialized", false).apply();
        myPrefs.edit().commit();
        super.onStop();
    }*/

    @Override
    public void onDestroy()
    {
        Log.d("onDestroy", "onDestroy Happened");
        SharedPreferences myPrefs = this.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        myPrefs.edit().putBoolean("appInitialized", false).apply();
        super.onDestroy();
    }

    public void reInstance()
    {
        // reinstance
        Environment.updateActivity(this);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("My conversations", chatLayoutChildActivity.class)
                .add("Discover groups", socialLayoutChildActivity.class)
                .add("Discover events", eventsLayoutChildActivity.class)
                .add("Me", meLayoutChildActivity.class)
                .add("BETA feedback", betaFeedbackLayoutChildActivity.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        if(SAVING_IMAGE)
        {
            SAVING_IMAGE = false;
            viewPager.setCurrentItem(3);
        }

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        initMenuFragment();

        if(!Environment.isNetworkRunning())
        {
            Environment.initializeNetwork();
        }
    }

    private boolean SAVING_IMAGE = false;
    private int RESULT_LOAD_IMAGE = 9;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userImage", cursor.getString(columnIndex));
                editor.apply();
                editor.commit();
                cursor.close();

                ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
                mViewPager.setCurrentItem(3);


            } else {
                /*Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();*/
            }
        } catch (Exception e) {
            /*Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();*/
        }


    }

    // Click actions
    public void crateNewCategory(View view)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        pDialog.setTitleText("Write the name of your category")
            .setCancelText("Cancel")
            .setConfirmText("Add category")
            .showCancelButton(true)
            .enableContentEditText(true)
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.cancel();
                }
            })
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                    String categoryName = sDialog.getContentEditText().getText().toString().replace(";", "");
                    if (categoryName.length() > 0) {
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
                        // Add status if missing
                        if (!settings.contains("userCategories")) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("userCategories", categoryName + ";");
                            editor.apply();
                            editor.commit();
                        } else {
                            String currentCategories = settings.getString("userCategories", "") + categoryName + ";";
                            if (!settings.getString("userCategories", "").contains(";" + categoryName + ";")) {
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("userCategories", currentCategories);
                                editor.apply();
                                editor.commit();
                            }
                        }
                        reInstance();
                    }
                }
            })
                .show();
    }

    public void joinGroup(View view)
    {

    }

    public void seeGroupProfile(View view)
    {

    }

    public void saveGroup(View view)
    {

    }

    public void joinEvent(View view)
    {

    }

    public void seeEventProfile(View view)
    {

    }

    public void saveEvent(View view)
    {

    }

    private AnimationUtils nameAnimation = null;
    private AnimationUtils descriptionAnimation = null;

    public void editName(View view)
    {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        RelativeLayout layoutHolder = (RelativeLayout)findViewById(R.id.nameEditHolder);
        TextView nameView = (TextView)findViewById(R.id.meName);
        EditText editName = (EditText) findViewById(R.id.meNameEdit);
        if(nameAnimation == null) {
            nameAnimation = new AnimationUtils(layoutHolder);
        }

        if(layoutHolder.getVisibility() == View.VISIBLE)
        {
            nameAnimation.collapseWidth();
            nameView.setVisibility(View.VISIBLE);
            nameView.setText(editName.getText());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("userName", editName.getText().toString());
            editor.apply();
            editor.commit();
        } else {
            nameAnimation.expandWidth();
            nameView.setVisibility(View.GONE);
        }
    }

    public void editDescription(View view)
    {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        RelativeLayout layoutHolder = (RelativeLayout)findViewById(R.id.statusEditHolder);
        TextView statusView = (TextView)findViewById(R.id.meDescription);
        EditText editStatus = (EditText) findViewById(R.id.meStatusEdit);

        if(descriptionAnimation == null) {
            descriptionAnimation = new AnimationUtils(layoutHolder);
        }

        if(layoutHolder.getVisibility() == View.VISIBLE)
        {
            statusView.setVisibility(View.VISIBLE);
            descriptionAnimation.collapseWidth();
            statusView.setText(editStatus.getText());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("userStatus", editStatus.getText().toString());
            editor.apply();
            editor.commit();
        } else {
            descriptionAnimation.expandWidth();
            statusView.setVisibility(View.GONE);
        }
    }

    public void selectNewImage(View view)
    {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
        SAVING_IMAGE = true;
    }

    public void onSendClick(View v)
    {
        View parent = (View)v.getParent();
        if (parent != null) {
            TextView idText = (TextView) parent.findViewById(R.id.contactId);
            int id = Integer.parseInt(idText.getText()+"");
            PhoneContact currentContact = Environment.getAvailableContacts().get(id);

            if(currentContact instanceof GroupContact || currentContact.getContactConversation() == null) {
                synchronized (((GroupContact) currentContact).getGroupConversations()) {
                    EditText message = (EditText) parent.findViewById(R.id.conversationAnswer);
                    String rawMessage = message.getText().toString();
                    if (rawMessage.length() > 0) {
                        DynamicListView conversationList = (DynamicListView) parent.findViewById(R.id.conversationsList);
                        conversationList.insert(conversationList.getAdapter().getCount(), new ConversationMessage(0, rawMessage, true, ConversationMessageStatus.SENT));
                        conversationList.setSelection(conversationList.getAdapter().getCount() - 1); // position at last message
                        message.setText("");
                    /*if (message.hasFocus()) {
                        message.clearFocus();
                    }*/
                        ((GroupContact) currentContact).getGroupConversations().add(new ConversationMessage(0, rawMessage, true, ConversationMessageStatus.SENT));
                        int listSplit = 200;
                        List<ConversationMessage> messages;
                        if (((GroupContact) currentContact).getGroupConversations().size() > listSplit)
                            messages = new ArrayList<>(((GroupContact) currentContact).getGroupConversations().subList(((GroupContact) currentContact).getGroupConversations().size() - listSplit, ((GroupContact) currentContact).getGroupConversations().size()));
                        else
                            messages = new ArrayList<>(((GroupContact) currentContact).getGroupConversations().subList(0, ((GroupContact) currentContact).getGroupConversations().size()));
                        GroupConversationArrayAdapter messagesAdapter = (new GroupConversationArrayAdapter(this, messages));
                        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
                        animationAdapter.setAbsListView(conversationList);
                        conversationList.setAdapter(animationAdapter);
                        conversationList.setSelection(conversationList.getAdapter().getCount() - 1);
                        if(((GroupContact) currentContact).getGroupConversations().size()>listSplit)
                        {
                            // Add view (scroll 15 more)
                            View header = View.inflate(this, R.layout.conversation_group_select_more, null);
                            conversationList.addHeaderView(header);
                        }
                    }
                }
            } else {
                synchronized (currentContact.getContactConversation()) {
                    EditText message = (EditText) parent.findViewById(R.id.conversationAnswer);
                    String rawMessage = message.getText().toString();
                    if (rawMessage.length() > 0) {
                        DynamicListView conversationList = (DynamicListView) parent.findViewById(R.id.conversationsList);
                        conversationList.insert(conversationList.getAdapter().getCount(), new ConversationMessage(0, rawMessage, true, ConversationMessageStatus.SENT));
                        message.setText("");
                    /*if (message.hasFocus()) {
                        message.clearFocus();
                    }*/
                        currentContact.getContactConversation().add(new ConversationMessage(0, rawMessage, true, ConversationMessageStatus.SENT)); List<ConversationMessage> messages;
                        int listSplit = 200;
                        if (currentContact.getContactConversation().size() > listSplit)
                            messages = new ArrayList<>(currentContact.getContactConversation().subList(currentContact.getContactConversation().size() - listSplit, currentContact.getContactConversation().size()));
                        else
                            messages = new ArrayList<>(currentContact.getContactConversation().subList(0, currentContact.getContactConversation().size()));
                        ContactConversationArrayAdapter messagesAdapter = (new ContactConversationArrayAdapter(this, messages));
                        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
                        animationAdapter.setAbsListView(conversationList);
                        conversationList.setAdapter(animationAdapter);
                        conversationList.setSelection(messagesAdapter.getCount() - 1); // position at last message
                        if(currentContact.getContactConversation().size()>listSplit)
                        {
                            // Add view (scroll 15 more)
                            View header = View.inflate(this, R.layout.conversation_select_more, null);
                            conversationList.addHeaderView(header);
                        }
                    }
                }
            }
        }
    }
}
