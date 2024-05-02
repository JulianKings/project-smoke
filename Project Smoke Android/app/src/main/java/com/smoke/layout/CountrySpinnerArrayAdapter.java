package com.smoke.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smoke.R;

import java.util.List;

public class CountrySpinnerArrayAdapter extends ArrayAdapter<String> {

    private List<String> objects;
    private Context context;

    public CountrySpinnerArrayAdapter(Context context, int resourceId,
                              List<String> objects) {
        super(context, resourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.simple_spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.spinnerText);
        label.setText(objects.get(position));

        return row;
    }

}
