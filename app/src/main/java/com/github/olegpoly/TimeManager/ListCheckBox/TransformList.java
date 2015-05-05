package com.github.olegpoly.TimeManager.ListCheckBox;

import com.github.olegpoly.TimeManager.TImeManagerDataBase.Table.ActionTable;
import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Vector;

public class TransformList {
    static private Function<ActionDBEntry, ActionListItem> transformFunction =
            new Function<ActionDBEntry,ActionListItem>() {
                public ActionListItem apply(ActionDBEntry ua)
                {
                    return new ActionListItem(ua.getActivityName(), true, ua.getId());
                }
            };

    static public List<ActionListItem> transform(List<ActionDBEntry> ua) {
        return Lists.transform(ua, transformFunction);
    }

    static public List<ActionListItem> transform() {
        List<ActionDBEntry> ua = ActionTable.getAll();

        List<ActionListItem> list = new Vector<>();

        for (ActionListItem actionListItem : transform(ua)) {
            list.add(actionListItem);
        }
        
        return list;
    }
}
