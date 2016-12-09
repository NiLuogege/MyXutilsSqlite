package com.example.well.myxutilssqlite.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by Well on 2016/12/9.
 * 单例模式+工厂模式
 * <p>
 * 通过单例的工厂模式获得到数据库操作类BaseDao,并且创建数据库
 */

public class DaoManagerFactory {
    private static DaoManagerFactory daoManagerFactory = new DaoManagerFactory(new File(Environment.getExternalStorageDirectory(), "login.db"));
    private SQLiteDatabase mSqLiteDatabase;

    private DaoManagerFactory(File file) {
        String path = file.getAbsolutePath();
        openDatabase(path);

    }

    private void openDatabase(String path) {
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entryClass) {
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entryClass, mSqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T) baseDao;
    }

    public static DaoManagerFactory getInstance() {
        return daoManagerFactory;
    }

}
