package com.smoke.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class basicControlService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent rootIntent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        // unregister listeners
        // ...

        // cleanup
        Log.d("onTaskRemoved", "onTaskRemoved happened");
        SharedPreferences myPreferences = this.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = myPreferences.edit();
        preferencesEditor.putBoolean("appInitialized", false).apply();

        stopSelf();
    }
}
