package com.ListCheckBox;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

import TImeManagerDataBase.Table.UserActivitiesTable;
import TImeManagerDataBase.TableEntry.UserActivityDBTableEntry;

public class TransformList {
    static private Function<UserActivityDBTableEntry, UserActivityListItem> transforFunction =
            new Function<UserActivityDBTableEntry,UserActivityListItem>() {
                public UserActivityListItem apply(UserActivityDBTableEntry ua)
                {
                    return new UserActivityListItem(ua.getActivityName(), true, ua.getId());
                }
            };

    static public List<UserActivityListItem> transform(List<UserActivityDBTableEntry> ua) {
        return Lists.transform(ua, transforFunction);
    }

    static public List<UserActivityListItem> transform() {
        List<UserActivityDBTableEntry> ua = UserActivitiesTable.getAll();
        return transform(ua);
    }
}
