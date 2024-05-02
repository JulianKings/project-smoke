package com.smoke.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.github.glomadrian.codeinputlib.CodeInput;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.util.math.Randomizer;

public class verifyPhoneByMessage extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private int securityCode = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.updateActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
        Transition ts = new Slide();  //Slide(); //Explode();

        ts.setDuration(1000);
        ts.excludeTarget(android.R.id.statusBarBackground, true);
        ts.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(ts);
        getWindow().setExitTransition(ts);

        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(colorDrawable);

        setContentView(R.layout.verify_by_message);

        securityCode = Randomizer.nextInt(1000, 9999);

        Environment.writeLine("Permission -> " + (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, Environment.PERMISSION_READ_PHONE_STATE);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, Environment.PERMISSION_SEND_SMS);
            } else {
                sendSMS("+" + getIntent().getExtras().getInt("countryCode") + getIntent().getExtras().getString("phoneNumber"), securityCode + "");
            }
        }
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true); // Don't close the application D:
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Environment.PERMISSION_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, Environment.PERMISSION_SEND_SMS);
                    } else {
                        sendSMS("+" + getIntent().getExtras().getInt("countryCode") + getIntent().getExtras().getString("phoneNumber"), securityCode + "");
                    }
                }
                break;

            case Environment.PERMISSION_SEND_SMS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendSMS("+" + getIntent().getExtras().getInt("countryCode") + getIntent().getExtras().getString("phoneNumber"), securityCode + "");
                }
                break;

            default:
                break;
        }
    }

    public int getSecurityCode() { return securityCode; }

    public void continueToApp(View view)
    {
        CodeInput verificationInput = (CodeInput) findViewById(R.id.verificationCode);
        if(verificationInput != null && verificationInput.getCode() != null) {
            String code = verificationInput.getCode()[0] + "" +
                    verificationInput.getCode()[1] + "" +
                    verificationInput.getCode()[2] + "" +
                    verificationInput.getCode()[3] + "";
            if (code.equals(securityCode+"")) {
                // Perform action on click
                Intent intent = new Intent(this, selectImageAndNameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle b = new Bundle();
                b.putInt("countryCode", getIntent().getExtras().getInt("countryCode"));
                b.putString("phoneNumber", getIntent().getExtras().getString("phoneNumber"));
                intent.putExtras(b);
                startActivityForResult(intent, 0);
            }
        }
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
