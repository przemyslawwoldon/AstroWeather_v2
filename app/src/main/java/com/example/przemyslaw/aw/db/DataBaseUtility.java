package com.example.przemyslaw.aw.db;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Przemyslaw on 2017-06-12.
 */

public class DataBaseUtility {
    public static CityDao getCityDao(Context context, boolean readable) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "newAstro.db");

        Database db;
        if (readable)
            db = helper.getReadableDb();
        else
            db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        return daoSession.getCityDao();
    }

}