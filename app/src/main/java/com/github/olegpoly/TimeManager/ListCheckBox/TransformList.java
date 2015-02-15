package com.github.olegpoly.TimeManager.ListCheckBox;

import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.ActionTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public class TransformList {
    static private Function<ActionDBEntry, UserActivityListItem> transforFunction =
            new Function<ActionDBEntry,UserActivityListItem>() {
                public UserActivityListItem apply(ActionDBEntry ua)
                {
                    return new UserActivityListItem(ua.getActivityName(), true, ua.getId());
                }
            };

    static public List<UserActivityListItem> transform(List<ActionDBEntry> ua) {
        return Lists.transform(ua, transforFunction);
    }

    static public List<UserActivityListItem> transform() {
        List<ActionDBEntry> ua = ActionTable.getAll();
        return transform(ua);
    }
}
