package com.yuncommunity.lizard.item;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oldfeel.base.BaseItem;

/**
 * Created by oldfeel on 16-10-17.
 */
@DatabaseTable
public class RecordItem extends BaseItem {
    @DatabaseField(id = true)
    public int id;
    @DatabaseField
    public String images;
    @DatabaseField
    public double weight;
    @DatabaseField
    public double length;
    @DatabaseField
    public String peeling_component;
    @DatabaseField
    public String start_time;
    @DatabaseField
    public String stop_time;
}
