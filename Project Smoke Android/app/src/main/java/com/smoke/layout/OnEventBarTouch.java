package com.smoke.layout;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.util.management.EventList;
import com.smoke.util.management.GroupList;

public class OnEventBarTouch implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        View parent = (View)v.getParent().getParent();
        if(parent != null) {
            TextView idText = (TextView) parent.findViewById(R.id.eventListId);
            int id = Integer.parseInt(idText.getText() + "");
            EventList currentGroupList = Environment.getAvailableEventLists().get(id);
            if (!(parent.findViewById(R.id.events).getVisibility() == View.VISIBLE) && currentGroupList.getAnimationUtil() != null &&
                    !(parent.findViewById(R.id.superClick).getVisibility() == View.VISIBLE)) {
                currentGroupList.getAnimationUtil().expandToHeight(parent.findViewById(R.id.superClick));
            } else if (currentGroupList.getAnimationUtil() != null) {
                currentGroupList.getAnimationUtil().collapseToHeight(parent.findViewById(R.id.superClick));
            }
        }
    }
}
