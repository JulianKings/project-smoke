package com.smoke.layout;

import android.graphics.Color;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;
import com.smoke.R;
import com.smoke.core.Environment;
import com.smoke.util.management.GroupList;
import com.smoke.util.management.PhoneContact;

public class OnGroupBarTouch implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        View parent = (View)v.getParent().getParent();
        if(parent != null) {
            TextView idText = (TextView) parent.findViewById(R.id.groupListId);
            int id = Integer.parseInt(idText.getText() + "");
            GroupList currentGroupList = Environment.getAvailableGroupLists().get(id);
            if (!(parent.findViewById(R.id.groups).getVisibility() == View.VISIBLE) && currentGroupList.getAnimationUtil() != null &&
                    !(parent.findViewById(R.id.magicClick).getVisibility() == View.VISIBLE)) {
                currentGroupList.getAnimationUtil().expandToHeight(parent.findViewById(R.id.magicClick));
            } else if (currentGroupList.getAnimationUtil() != null) {
                currentGroupList.getAnimationUtil().collapseToHeight(parent.findViewById(R.id.magicClick));
            }
        }
    }
}
