package com.smoke.util.management;

import android.graphics.Color;
import com.smoke.util.animations.AnimationUtils;

import java.util.List;

public class GroupList {
    private int listId;
    private String listName;
    private int listBackgroundColor;
    private List<GroupContact> listIntegrants;
    private AnimationUtils smartAnimator;

    public GroupList(int id, String name, String color, List<GroupContact> listIntegrants)
    {
        this.listId = id;
        this.listName = name;
        this.listBackgroundColor = Color.parseColor(color);
        this.listIntegrants = listIntegrants;
    }

    public int getListId() { return listId; }

    public String getListName() { return listName; }

    public int getListBackgroundColor() { return listBackgroundColor; }

    public List<GroupContact> getListGroups() { return listIntegrants; }

    public AnimationUtils getAnimationUtil() { return smartAnimator; }

    public void setAnimationUtil(AnimationUtils anim) { this.smartAnimator = anim; }
}
