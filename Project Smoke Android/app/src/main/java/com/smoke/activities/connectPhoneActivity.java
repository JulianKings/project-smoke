package com.smoke.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.smoke.R;
import com.smoke.core.Environment;

public class connectPhoneActivity extends Activity {
    private TextView loadingText;
    private ProgressBar currentProgress;
    private Button verifyByMessage;
    private Button verifyByCall;
    private Button verifyByApp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.updateActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.connect_phone);
        loadingText = (TextView) findViewById(R.id.loadingText);
        verifyByMessage = (Button) findViewById(R.id.verifyByMessage);
        verifyByCall = (Button) findViewById(R.id.verifyByCall);
        verifyByApp = (Button) findViewById(R.id.verifyByApp);
        currentProgress = (ProgressBar) findViewById(R.id.currentProgress);
        if(Environment.initializeNetwork())
        {
            loadingText.setText("Verify your number");
            verifyByMessage.setVisibility(View.VISIBLE);
            verifyByCall.setVisibility(View.VISIBLE);
            verifyByApp.setVisibility(View.VISIBLE);
        }
    }

    public void verifyByMessage(View view)
    {
        Intent intent = new Intent(this, verifyPhoneByMessage.class);
        Bundle b = new Bundle();
        b.putInt("countryCode", getIntent().getExtras().getInt("countryCode"));
        b.putString("phoneNumber", getIntent().getExtras().getString("phoneNumber"));
        intent.putExtras(b);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void verifyByCall(View view)
    {
        Intent intent = new Intent(this, verifyPhoneByCall.class);
        Bundle b = new Bundle();
        b.putInt("countryCode", getIntent().getExtras().getInt("countryCode"));
        b.putString("phoneNumber", getIntent().getExtras().getString("phoneNumber"));
        intent.putExtras(b);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void verifyByApp(View view)
    {
        Intent intent = new Intent(this, verifyPhoneByApp.class);
        Bundle b = new Bundle();
        b.putInt("countryCode", getIntent().getExtras().getInt("countryCode"));
        b.putString("phoneNumber", getIntent().getExtras().getString("phoneNumber"));
        intent.putExtras(b);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
