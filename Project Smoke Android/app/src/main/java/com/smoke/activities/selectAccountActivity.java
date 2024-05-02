package com.smoke.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.smoke.R;

import com.smoke.core.Environment;
import com.smoke.layout.RecentNotificationsArrayAdapter;
import com.smoke.util.management.ConversationMessage;
import com.smoke.util.management.ConversationMessageStatus;

import java.util.ArrayList;
import java.util.List;

public class selectAccountActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.updateActivity(this);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.select_account);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);

        if (!settings.contains("countryCode") || !settings.contains("phoneNumber") || !settings.contains("userImage") || !settings.contains("userName")) {
            // NOPE
            return;
        }

        TextView profileSneakyName = (TextView) findViewById(R.id.profileSneakyName);
        profileSneakyName.setText(settings.getString("userName", "Mr. proper"));
        TextView profileName = (TextView) findViewById(R.id.profileName);
        profileName.setText(settings.getString("userName", "Mr. proper"));

        ImageView profileIcon = (ImageView) findViewById(R.id.profileIcon);
        if(settings.getString("userImage", "none").equals("none"))
        {
            profileIcon.setImageResource(R.drawable.no_photo);
        } else {
            profileIcon.setImageBitmap(BitmapFactory.decodeFile(settings.getString("userImage", "none")));
        }

        // Do the job
        DynamicListView conversationList = (DynamicListView) findViewById(R.id.recentNotificationsList);
        List<ConversationMessage> messages = new ArrayList<>();
        messages.add(new ConversationMessage(1, "This is a potato", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(2, "This is another potato", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(1, "This is the definitive potato", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(3, "This is a very long potato but I won't tell you how long is it, the only way you know how long is the potato is by reverse-engineering the time-space-potato continuum...", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(3, "This is another potato", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(4, "This is the definitive potato", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(5, "This is a potato", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(1, "This is another potato", false, ConversationMessageStatus.RECEIVED));
        messages.add(new ConversationMessage(1, "This is the definitive potato", false, ConversationMessageStatus.RECEIVED));
        RecentNotificationsArrayAdapter messagesAdapter = (new RecentNotificationsArrayAdapter(this, messages));
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(messagesAdapter);
        animationAdapter.setAbsListView(conversationList);
        conversationList.setAdapter(animationAdapter);


    }

    public void continueToMainActivity(View view)
    {
        // Continue to main activity
        SharedPreferences settings = getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        // Add status if missing
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("appInitialized", true).apply();

        Intent intent = new Intent(this, mainLayoutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, 0);
    }
}
