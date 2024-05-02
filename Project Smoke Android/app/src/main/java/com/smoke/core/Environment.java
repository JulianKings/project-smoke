package com.smoke.core;
import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.smoke.R;
import com.smoke.communication.CommunicationController;
import com.smoke.communication.NetworkRunnable;
import com.smoke.util.management.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Environment  {
    private static CommunicationController mCommunicationController;
    private static Activity mCurrentActivity;
    private static GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;
    private static Runnable mNetworkRunnable;
    // TODO Prepare a ContactManager
    private static List<PhoneContact> phoneContacts;
    private static HashMap<Integer, PhoneContact> mAvailableContacts;
    private static HashMap<Integer, GroupList> mAvailableGroupLists;
    private static HashMap<Integer, EventList> mAvailableEventLists;

    public static final int PERMISSION_SEND_SMS = 1, PERMISSION_READ_SMS = 2, PERMISSION_RECEIVE_SMS = 3, PERMISSION_READ_PHONE_STATE = 4, PERMISSION_WRITE_EXTERNAL_STORAGE = 5, PERMISSION_CAMERA = 6, PERMISSION_READ_EXTERNAL_STORAGE = 7;

    public  static long START_UP_TIME = 0x00000000l;
    private static final byte NORMAL_TERMINATION = 0x01;
    private static final byte ABNORMAL_TERMINATION = 0x00;

    public static long traceNanoTime()
    {
        return System.nanoTime();
    }

    public static long traceMilliTime()
    {
        return System.currentTimeMillis();
    }

    public static CommunicationController getCommunication() { return Environment.mCommunicationController; }
    public static void setCommunication(CommunicationController communication) { mCommunicationController = communication; }

    public static boolean communicationEnabled()
    {
        if(mCommunicationController != null)
            return (mCommunicationController.SERVERCHANNEL != null);
        else
            return false;
    }

    public static GoogleApiClient getGoogleApiClient() { return mGoogleApiClient; }

    public static void updateGoogleApiClient(GoogleApiClient client) { mGoogleApiClient = client; }

    public static Location getLastKnownLocation() { return mLastLocation; }

    public static void updateLastKnownLocation(Location currentLocation) { mLastLocation = currentLocation; }

    public static Activity getCurrentActivity() { return mCurrentActivity; }

    public static HashMap<Integer, PhoneContact> getAvailableContacts() { return mAvailableContacts; }

    public static HashMap<Integer, GroupList> getAvailableGroupLists() { return mAvailableGroupLists; }

    public static HashMap<Integer, EventList> getAvailableEventLists() { return mAvailableEventLists; }

    public static List<PhoneContact> getPhoneContacts() { return phoneContacts; }

    public static void onInitialize(Activity startingActivity)
    {
        // App started
        mAvailableContacts = new HashMap<>();
        mAvailableGroupLists = new HashMap<>();
        mAvailableEventLists = new HashMap<>();

        List<ConversationMessage> conversation = new ArrayList<ConversationMessage>();
        conversation.add(new ConversationMessage(0, "Good evening dear :) ", true, ConversationMessageStatus.SEEN));
        conversation.add(new ConversationMessage(1, "Hey! Please give me a few minutes", false, ConversationMessageStatus.SEEN));
        conversation.add(new ConversationMessage(0, "No problem", true, ConversationMessageStatus.SEEN));
        conversation.add(new ConversationMessage(0, "Have you solved that problem from yesterday? It kept you quite busy", true, ConversationMessageStatus.SEEN));
        conversation.add(new ConversationMessage(1, "Yes I solved it at the end, it was so funny, thanks for sharing it :)", false, ConversationMessageStatus.SEEN));
        conversation.add(new ConversationMessage(0, "I'm glad you liked it :)", true, ConversationMessageStatus.SEEN));
        conversation.add(new ConversationMessage(0, " I'm going to eat something, see you later", true, ConversationMessageStatus.SEEN));
        conversation.add(new ConversationMessage(1, "Are you done?", false, ConversationMessageStatus.SEEN));

        List<ConversationMessage> conversation2 = new ArrayList<ConversationMessage>();
        conversation2.add(new ConversationMessage(2, "HAVE YOU SEEN IT???", false, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(0, "What?", true, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(2, "CHECK THE GAME OF THRONES GROUP!!!!", false, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(0, "Ok...", true, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(0, "OMFG IS THAT REAL?", true, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(2, "YES!", false, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(0, "This is the best day of my life", true, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(2, "Same here!", false, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(0, "Lets hang out later and celebrate it", true, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(2, "Ok see you later at the pub", false, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(0, "See you :)", true, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(2, "Can you bring your tablet too?", false, ConversationMessageStatus.SEEN));
        conversation2.add(new ConversationMessage(2, "Nicola wants to show you something ;)", false, ConversationMessageStatus.SEEN));

        List<ConversationMessage> conversation3 = new ArrayList<ConversationMessage>();
        conversation3.add(new ConversationMessage(3, "omg!", false, ConversationMessageStatus.SEEN));
        conversation3.add(new ConversationMessage(3, "omg!!", false, ConversationMessageStatus.SEEN));
        conversation3.add(new ConversationMessage(0, "true!!!!", true, ConversationMessageStatus.SEEN));

        List<ConversationMessage> conversation4 = new ArrayList<ConversationMessage>();
        conversation4.add(new ConversationMessage(4, "omg!", false, ConversationMessageStatus.SEEN));
        conversation4.add(new ConversationMessage(4, "omg!!", false, ConversationMessageStatus.SEEN));
        conversation4.add(new ConversationMessage(0, "true!!!!", true, ConversationMessageStatus.SEEN));

        List<ConversationMessage> conversation6 = new ArrayList<ConversationMessage>();
        conversation6.add(new ConversationMessage(5, "omg!", false, ConversationMessageStatus.SEEN));
        conversation6.add(new ConversationMessage(5, "omg!!", false, ConversationMessageStatus.SEEN));
        conversation6.add(new ConversationMessage(0, "true!!!!", true, ConversationMessageStatus.SEEN));

        List<ConversationMessage> conversation7 = new ArrayList<ConversationMessage>();
        conversation7.add(new ConversationMessage(6, "omg!", false, ConversationMessageStatus.SEEN));
        conversation7.add(new ConversationMessage(6, "omg!!", false, ConversationMessageStatus.SEEN));
        conversation7.add(new ConversationMessage(0, "true!!!!", true, ConversationMessageStatus.SEEN));

        List<ConversationMessage> conversation5 = new ArrayList<ConversationMessage>();
        conversation5.add(new ConversationMessage(20, "How can I help you?", false, ConversationMessageStatus.SEEN));
        conversation5.add(new ConversationMessage(0, "Remember me to wash the car", true, ConversationMessageStatus.SEEN));
        conversation5.add(new ConversationMessage(20, "When you want me to remember it?", false, ConversationMessageStatus.SEEN));
        conversation5.add(new ConversationMessage(0, "Tomorrow at 4pm", true, ConversationMessageStatus.SEEN));
        conversation5.add(new ConversationMessage(20, "Tomorrow at 4pm I will remember you to wash the car, is that correct?", false, ConversationMessageStatus.SEEN));
        conversation5.add(new ConversationMessage(0, "Yes", true, ConversationMessageStatus.SEEN));
        conversation5.add(new ConversationMessage(20, "Note saved successfully :)", false, ConversationMessageStatus.SEEN));

        // The hardcode is real
        phoneContacts = new ArrayList<>();
        PhoneContact contact1 = (new PhoneContact(1, R.drawable.marie, "Marie Curie", "Progress is neither swift nor easy", conversation, 7, "11:43 pm")),
        contact2 = (new PhoneContact(2, R.drawable.niggasteino, "Albert Einstein", "All is relative", conversation2, 13, "09:37 pm")),
        contact3 = (new PhoneContact(3, R.drawable.tesla, "Nicola Tesla", "A light to share with the world", conversation3, 3, "09:11 pm")),
        contact4 = (new PhoneContact(20, R.drawable.ic_launcher, "Tom", "Always at your service!", conversation5, 0, "01:07 am")),
        contact5 = (new PhoneContact(4, R.drawable.turing, "Alan Turing", "Machines surprise me with great frequency", conversation4, 0, "09:30 am")),
        contact6 = (new PhoneContact(5, R.drawable.sparrow, "Jack Sparrow", "Not all treasure is silver and gold, mate", conversation6, 0, "07:59 am")),
        contact7 = (new PhoneContact(6, R.drawable.matt, "The Doctor", "Geronimo!", conversation7, 0, "07:45 am"));

        phoneContacts.add(contact1); phoneContacts.add(contact2); phoneContacts.add(contact3); phoneContacts.add(contact4); phoneContacts.add(contact5);
        phoneContacts.add(contact6); phoneContacts.add(contact7);

        mAvailableContacts.put(1, contact1); mAvailableContacts.put(2, contact2); mAvailableContacts.put(3, contact3); mAvailableContacts.put(20, contact4);
        mAvailableContacts.put(4, contact5); mAvailableContacts.put(5, contact6); mAvailableContacts.put(6, contact7);

        if(Environment.getCommunication() == null)
        {
            Environment.initializeNetwork();
        }
    }

    public static void onResume()
    {
        if(Environment.getCommunication() == null)
        {
            if(!Environment.isNetworkRunning()) {
                Environment.initializeNetwork();
            }
        }
    }


    public static boolean initializeNetwork()
    {
        if(mNetworkRunnable == null) {
            mNetworkRunnable = new NetworkRunnable(); // We only need 1 thread lol
            (new Thread(mNetworkRunnable)).start(); // Dont start network on main thread gosh
            return true;
        } else {
            return true;
        }
    }

    public static boolean isNetworkRunning()
    {
        if(mCommunicationController == null)
            return false;
        else {
            try
            {
                return mCommunicationController.isActive();
            } catch (Exception ex)
            {
                writeLine(ex.toString());
                return false;
            }
        }
    }

    public static void updateActivity(Activity act) { mCurrentActivity = act; }

    public static void writeLine(String message)
    {
        Log.d("Project Smoke", message);
    }
}
