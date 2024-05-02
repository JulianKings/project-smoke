package com.smoke.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.layout.CountrySpinnerArrayAdapter;

import java.util.*;

public class registerPhoneActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner countrySpinner;
    private EditText countryCode;
    private Map<String,Locale> countryLocales;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.updateActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
        Transition ts = new Slide();  //Slide(); //Explode();

        ts.setDuration(150);
        ts.excludeTarget(android.R.id.statusBarBackground, true);
        ts.excludeTarget(android.R.id.navigationBarBackground, true);
        ts.excludeTarget(R.id.layoutBackground, false);
        ts.excludeTarget(R.id.layoutChildBackgrund, true);
        ts.excludeChildren(R.id.layoutChildBackgrund, true);

        getWindow().setEnterTransition(ts);
        getWindow().setExitTransition(ts);

        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(colorDrawable);

        setContentView(R.layout.register_phone);


        countryLocales = new HashMap<>();
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
                countryLocales.put(country,locale);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> arrayAdapter = new CountrySpinnerArrayAdapter(this,
                R.layout.simple_spinner_item, countries);
        countrySpinner.setAdapter(arrayAdapter);
        countrySpinner.setOnItemSelectedListener(this);

        countryCode = (EditText) findViewById(R.id.regionNumber);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String country = parent.getItemAtPosition(pos).toString();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        int code = phoneUtil.getCountryCodeForRegion(((Locale)countryLocales.get(country)).getCountry());
        countryCode.setText(code+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClickVerifyPhone(View view)
    {
        EditText phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        // Perform action on click
        Intent intent = new Intent(this, connectPhoneActivity.class);
        Bundle b = new Bundle();
        b.putInt("countryCode", Integer.parseInt(countryCode.getText().toString()));
        b.putString("phoneNumber", (phoneNumber.getText().toString().replace(" ", "")));
        intent.putExtras(b);
        startActivity(intent);
    }
}
