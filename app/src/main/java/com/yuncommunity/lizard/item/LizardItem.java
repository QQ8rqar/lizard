package com.yuncommunity.lizard.item;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oldfeel.base.BaseItem;

/**
 * Created by oldfeel on 16-10-17.
 */
@DatabaseTable
public class LizardItem extends BaseItem {
    @DatabaseField(id = true)
    public int id;
    public String name;
    public String birthday;
    public String sex;
}
