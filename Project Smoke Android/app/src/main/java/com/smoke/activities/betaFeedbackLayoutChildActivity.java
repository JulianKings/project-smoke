package com.smoke.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.smoke.R;

public class betaFeedbackLayoutChildActivity extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View currentView =  inflater.inflate(R.layout.beta_feedback_layout, container, false);
        return currentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());
    }
}
