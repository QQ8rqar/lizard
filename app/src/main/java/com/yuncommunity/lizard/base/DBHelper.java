package com.yuncommunity.lizard.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yuncommunity.lizard.conf.Constant;
import com.yuncommunity.lizard.item.LizardItem;
import com.yuncommunity.lizard.item.PeelingComponentItem;
import com.yuncommunity.lizard.item.RecordItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oldfeel on 16-10-17.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static DBHelper dbHelper;
    private Context context;
    private Dao<RecordItem, Integer> recordDao;
    private Dao<LizardItem, Integer> lizardDao;

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            try {
                dbHelper.recordDao = dbHelper.getDao(RecordItem.class);
                dbHelper.lizardDao = dbHelper.getDao(LizardItem.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dbHelper;
    }

    public DBHelper(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, RecordItem.class);
            TableUtils.createTable(connectionSource, LizardItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, RecordItem.class, true);
            TableUtils.dropTable(connectionSource, LizardItem.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    public void createOrUpdate(RecordItem item) {
        try {
            recordDao.createOrUpdate(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LizardItem> getLizardList() {
        try {
            return lizardDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public LizardItem createLizard(String name, String sex, String birthday) {
        LizardItem item = new LizardItem();
        item.birthday = birthday;
        item.name = name;
        item.sex = sex;
        try {
            return lizardDao.createIfNotExists(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public void delete(LizardItem lizardItem) {
        try {
            lizardDao.delete(lizardItem);
            DeleteBuilder<RecordItem, Integer> db = recordDao.deleteBuilder();
            db.where().eq("lizard_id", lizardItem.id);
            db.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RecordItem> getRecordList(LizardItem lizardItem, PeelingComponentItem peelingItem, int page) throws SQLException {
        QueryBuilder<RecordItem, Integer> qb = recordDao.queryBuilder();
        if (lizardItem != null && !lizardItem.name.equals("全部")) {
            qb.where().eq("lizard_id", lizardItem.id);
        }
        if (peelingItem != null && !peelingItem.name.equals("全部")) {
            qb.where().eq("peeling_component", peelingItem.name);
        }
        qb.offset(page * Constant.PAGE_SIZE);
        qb.limit(Constant.PAGE_SIZE);
        qb.orderBy("start_time", false);
        List<RecordItem> list = qb.query();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).lizard = lizardDao.queryForId(list.get(i).lizard_id);
        }
        return list;
    }

    public void delete(RecordItem item) {
        try {
            recordDao.delete(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
