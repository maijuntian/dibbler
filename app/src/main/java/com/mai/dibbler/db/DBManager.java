package com.mai.dibbler.db;

import com.mai.dibbler.MyApplication;

/**
 * Created by maijuntian on 2018/6/27.
 */
public class DBManager {

    private static DBManager instance;

    DaoSession mDaoSession;

    private DBManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(MyApplication.INSTANCE.getApplicationContext(), "dibbler", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    public VideoDao getVideoDao() {
        return mDaoSession.getVideoDao();
    }
}
