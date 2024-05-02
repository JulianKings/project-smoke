package com.smoke.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.layout.ContactsArrayAdapter;
import com.smoke.util.management.PhoneContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class chatLayoutChildActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());

        final Spinner categorySpinner = (Spinner) getView().findViewById(R.id.conversationCategory);
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("My conversations"); categories.add("My favorites");
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        // Add status if missing
        if (settings.contains("userCategories")) {
            String categoriez = settings.getString("userCategories", "");
            if(categoriez.contains(";"))
            {
                for(String s : categoriez.split(";"))
                    categories.add(s);
            }
        }
       // Collections.sort(categories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                final DynamicListView listView = (DynamicListView) getView().findViewById(R.id.contactsList);
                switch (category) {
                    case "My conversations": {
                        ContactsArrayAdapter contactsAdapter = (new ContactsArrayAdapter(getActivity(), Environment.getPhoneContacts()));
                        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(contactsAdapter);
                        animationAdapter.setAbsListView(listView);
                        listView.setAdapter(animationAdapter);
                        listView.setItemsCanFocus(true);
                    }
                        break;

                    case "My favorites": {
                        // TODO load favs
                        ContactsArrayAdapter contactsAdapter = (new ContactsArrayAdapter(getActivity(), new ArrayList<PhoneContact>()));
                        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(contactsAdapter);
                        animationAdapter.setAbsListView(listView);
                        listView.setAdapter(animationAdapter);
                        listView.setItemsCanFocus(true);
                    }
                        break;
                    default: {
                        // TODO load cat
                        ContactsArrayAdapter contactsAdapter = (new ContactsArrayAdapter(getActivity(), new ArrayList<PhoneContact>()));
                        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(contactsAdapter);
                        animationAdapter.setAbsListView(listView);
                        listView.setAdapter(animationAdapter);
                        listView.setItemsCanFocus(true);
                    }
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        final DynamicListView listView = (DynamicListView) getView().findViewById(R.id.contactsList);
        ContactsArrayAdapter contactsAdapter = (new ContactsArrayAdapter(this.getActivity(), Environment.getPhoneContacts()));
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(contactsAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
        listView.setItemsCanFocus(true);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getId() == listView.getId()) {
                    final int currentFirstVisibleItem = listView.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        // getSherlockActivity().getSupportActionBar().hide();
                        Environment.getCurrentActivity().getActionBar().hide();
                        //getSupportActionBar().hide();
                    } else if (currentFirstVisibleItem == mLastFirstVisibleItem) {
                        // getSherlockActivity().getSupportActionBar().show();
                        Environment.getCurrentActivity().getActionBar().show();
                        //getSupportActionBar().show();
                    }

                    mLastFirstVisibleItem = 0;
                }
            }
        });
    }
}