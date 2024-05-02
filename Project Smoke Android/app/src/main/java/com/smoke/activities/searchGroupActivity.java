package com.smoke.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.skyfishjy.library.RippleBackground;
import com.smoke.R;
import com.smoke.core.Environment;

import com.google.android.gms.maps.*;

public class searchGroupActivity extends Activity implements OnMapReadyCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.updateActivity(this);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.search_group);

        getActionBar().setTitle("Search a group");

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        if(Environment.getLastKnownLocation() != null) {
            LatLng myPosition = new LatLng(Environment.getLastKnownLocation().getLatitude(), Environment.getLastKnownLocation().getLongitude());
            float zoomSize = (float) 17;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, zoomSize));

           /* map.addMarker(new MarkerOptions()
                    .title("Me")
                    .snippet("O mai gud")
                    .position(myPosition));*/
        }
    }
}
