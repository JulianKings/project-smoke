package com.smoke.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.glomadrian.codeinputlib.CodeInput;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.util.math.Randomizer;

public class verifyPhoneByApp extends Activity {
    final int REQUEST_OPEN_MESSAGES = 3;
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

        setContentView(R.layout.verify_by_app);

        securityCode = Randomizer.nextInt(1000,9999);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, securityCode + "");
        sendIntent.setType("text/plain");
        startActivityForResult(Intent.createChooser(sendIntent, "Choose a contact to send the code (send it to yourself if possible)"), REQUEST_OPEN_MESSAGES);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == REQUEST_OPEN_MESSAGES && resultCode == RESULT_OK) {
                Button continueToApp = (Button) findViewById(R.id.continueToApp);
                continueToApp.setEnabled(true); // :)
            } else {
                Button continueToApp = (Button) findViewById(R.id.continueToApp);
                continueToApp.setEnabled(false); // :(
                /*Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();*/
            }
        } catch (Exception e) {
            Button continueToApp = (Button) findViewById(R.id.continueToApp);
            continueToApp.setEnabled(false); // :(
            /*Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();*/
        }


    }
}
