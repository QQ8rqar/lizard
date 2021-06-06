package com.yuncommunity.lizard.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yuncommunity.lizard.conf.Constant;
import com.yuncommunity.lizard.item.RecordItem;

import java.sql.SQLException;

/**
 * Created by oldfeel on 16-10-17.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static DBHelper dbHelper;
    private Context context;
    private Dao<RecordItem, Integer> recordDao;

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            try {
                dbHelper.recordDao = dbHelper.getDao(RecordItem.class);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, RecordItem.class, true);
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
}
