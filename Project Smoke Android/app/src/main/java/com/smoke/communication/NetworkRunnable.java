package com.smoke.communication;


import android.util.Log;

import com.smoke.core.Environment;

public class NetworkRunnable implements Runnable {

    public void run() {
        if (Environment.getCommunication() == null) {
            Environment.setCommunication(new CommunicationController(10434));
            if (Environment.getCommunication().bootstrap() == false) {
            } else {
                Environment.getCommunication().initializeNetworking();
            }
            // w00t
        } else {
            // kill the thread
        }
    }
}
