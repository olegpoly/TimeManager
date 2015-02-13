package com.github.TimeMager.ListCheckBox;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

import com.github.TimeMager.TImeManagerDataBase.Table.UserActivitiesTable;
import com.github.TimeMager.TImeManagerDataBase.TableEntry.UserActivityDBEntry;

public class TransformList {
    static private Function<UserActivityDBEntry, UserActivityListItem> transforFunction =
            new Function<UserActivityDBEntry,UserActivityListItem>() {
                public UserActivityListItem apply(UserActivityDBEntry ua)
                {
                    return new UserActivityListItem(ua.getActivityName(), true, ua.getId());
                }
            };

    static public List<UserActivityListItem> transform(List<UserActivityDBEntry> ua) {
        return Lists.transform(ua, transforFunction);
    }

    static public List<UserActivityListItem> transform() {
        List<UserActivityDBEntry> ua = UserActivitiesTable.getAll();
        return transform(ua);
    }
}
