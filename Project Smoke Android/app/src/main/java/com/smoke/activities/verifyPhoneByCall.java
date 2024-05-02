package com.smoke.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.github.glomadrian.codeinputlib.CodeInput;
import com.smoke.R;
import com.smoke.core.Environment;

public class verifyPhoneByCall extends Activity {
    private int securityCode = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.updateActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition ts = new Slide();  //Slide(); //Explode();

        ts.setDuration(1000);
        ts.excludeTarget(android.R.id.statusBarBackground, true);
        ts.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(ts);
        getWindow().setExitTransition(ts);

        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(colorDrawable);

        setContentView(R.layout.verify_by_call);

        securityCode = 1234;
    }


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
}
