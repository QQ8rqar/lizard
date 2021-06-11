package com.yuncommunity.lizard.item;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oldfeel.base.BaseItem;

/**
 * Created by oldfeel on 16-10-17.
 */
@DatabaseTable
public class LizardItem extends BaseItem {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String birthday;
    @DatabaseField
    public String sex;
}
