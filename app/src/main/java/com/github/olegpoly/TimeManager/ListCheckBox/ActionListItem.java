package com.github.olegpoly.TimeManager.ListCheckBox;

/**
 * Created by root on 09.02.15.
 */
public class ActionListItem {
    String userActivityName;
    Boolean isSelected;
    Long id;

    public ActionListItem() {
    }

    public ActionListItem(String userActivityName) {
        this.userActivityName = userActivityName;
        isSelected = false;
    }

    public ActionListItem(String userActivityName, Boolean isSelected) {
        this.userActivityName = userActivityName;
        this.isSelected = isSelected;
    }

    public ActionListItem(String userActivityName, Boolean isSelected, Long id) {
        this.userActivityName = userActivityName;
        this.isSelected = isSelected;
        this.id = id;
    }

    public String getUserActivityName() {
        return userActivityName;
    }

    public void setUserActivityName(String userActivityName) {
        this.userActivityName = userActivityName;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
