package com.yuncommunity.lizard.item;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oldfeel.base.BaseItem;
import com.oldfeel.utils.StringUtil;

import org.joda.time.LocalDate;
import org.joda.time.Years;

/**
 * Created by oldfeel on 16-10-17.
 */
@DatabaseTable
public class RecordItem extends BaseItem {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String images;
    @DatabaseField
    public String weight;
    @DatabaseField
    public String length;
    @DatabaseField
    public String peeling_component;
    @DatabaseField
    public String start_time;
    @DatabaseField
    public String stop_time;
    @DatabaseField
    public int lizard_id;
    public LizardItem lizard;

    public String getFirstImage() {
        if (StringUtil.isEmpty(images)) {
            return "";
        }
        if (images.contains(",")) {
            return images.split(",")[0];
        }
        return images;
    }

    public String getNameAndAge() {
        if (lizard == null) {
            return "未知";
        }
        return lizard.name + "(" + getAge(lizard.birthday) + ")";
    }

    public String getAge(String birthday) {
        if (StringUtil.isEmpty(birthday)) {
            return "";
        }
        String[] ymd = birthday.split("-");
        LocalDate birthdate = new LocalDate(Integer.valueOf(ymd[0]), Integer.valueOf(ymd[1]), Integer.valueOf(ymd[2]));
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthdate, now);
        return age.getYears() + "岁";
    }

    public String[] getImages() {
        if (!StringUtil.isEmpty(images)) {
            return images.split(",");
        }
        return null;
    }
}
