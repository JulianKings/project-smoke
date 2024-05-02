package com.smoke.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.smoke.R;
import com.smoke.activities.verifyPhoneByMessage;
import com.smoke.core.Environment;

public class IncomingSmsBroadcastReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent != null && SMS_RECEIVED.equals(intent.getAction())) {
            final SmsMessage smsMessage = extractSmsMessage(intent);
            processMessage(context, smsMessage);
        }

    }

    private SmsMessage extractSmsMessage(final Intent intent) {

        final Bundle pudsBundle = intent.getExtras();
        final Object[] pdus = (Object[]) pudsBundle.get("pdus");
        final SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);

        return smsMessage;

    }

    private void processMessage(final Context context, final SmsMessage smsMessage) {
        // Do something interesting here
        if(Environment.getCurrentActivity() instanceof verifyPhoneByMessage)
        {
            verifyPhoneByMessage currentActivity = (verifyPhoneByMessage) Environment.getCurrentActivity();

            EditText verificationInput = (EditText) currentActivity.findViewById(R.id.verificationCode);
            if(Integer.parseInt(smsMessage.getDisplayMessageBody()) == currentActivity.getSecurityCode()) {
                verificationInput.setText(smsMessage.getDisplayMessageBody());
                Button continueToApp = (Button) currentActivity.findViewById(R.id.continueToApp);
                continueToApp.setEnabled(true); // :)
            }
        }
    }
}
