package com.example.well.myxutilssqlite.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Well on 2016/12/9.
 * 真正的数据库操作类(但是还是抽象的,创建表的操作还是通过继承该类让程序员自己去操作)
 */

public abstract class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase mSQLiteDatabase;
    private boolean isInit = false;
    private String tableName;
    private Class<T> entityClass;
    private Map<String, Field> cacheMap;

    public synchronized void init(Class<T> entiry, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.mSQLiteDatabase = sqLiteDatabase;
            this.entityClass = entiry;
            this.tableName = entityClass.getAnnotation(DbTable.class).value();
            sqLiteDatabase.execSQL(createDataNase());

            cacheMap = new HashMap<>();
            initCacheMap();

        }
    }

    protected void initCacheMap() {
        String sql = "select * from " + this.tableName + " limit 1,0";
        Cursor cursor = null;
        try {
            cursor = this.mSQLiteDatabase.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();
            Field[] columnFields = entityClass.getFields();
            for (Field field : columnFields) {
                field.setAccessible(true);
            }

            for (String columnName : columnNames) {
                for (Field columnField : columnFields) {
                    String filedName;
                    String columnFieldName = columnField.getName();
                    DbFiled annotation = columnField.getAnnotation(DbFiled.class);
                    if (annotation != null) {
                        filedName = annotation.value();
                    } else {
                        filedName = columnFieldName;
                    }

                    if (columnName.equals(filedName)) {
                        cacheMap.put(columnName, columnField);
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

    }


    /**
     * 增
     *
     * @param entity
     * @return
     */
    public Long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues contentValues = getContentValues(map);
        long insert = mSQLiteDatabase.insert(tableName, null, contentValues);
        return -insert;
    }

    /**
     * 删除
     *
     * @param entity
     * @return
     */
    @Override
    public int delete(T entity) {
        Condition condition = new Condition(getValues(entity));
        int delete = mSQLiteDatabase.delete(tableName, condition.whereClause, condition.whereArgs);
        return delete;
    }

    /**
     * 改
     *
     * @param entiry
     * @param where
     * @return
     */
    public int updata(T entiry, T where) {
        Map<String, String> map = getValues(entiry);
        Condition condition = new Condition(getValues(where));
        int result = mSQLiteDatabase.update(tableName, getContentValues(map), condition.whereClause, condition.whereArgs);
        return result;


    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    /**
     * 返回 要插入的键对应的值的集合
     *
     * @param entity
     * @return
     */
    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()) {
            Field colmunToFiled = iterator.next();
            String cacheKey = null;
            String cacheVuale = null;
            DbFiled annotation = colmunToFiled.getAnnotation(DbFiled.class);
            if (annotation != null) {
                cacheKey = annotation.value();

            } else {
                cacheKey = colmunToFiled.getName();
            }

            try {
                if (null == colmunToFiled.get(entity)) {//获得字段的值
                    continue;
                }
                cacheVuale = colmunToFiled.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            result.put(cacheKey, cacheVuale);
        }

        return result;
    }


    class Condition {
        private String whereClause;
        private String[] whereArgs;

        public Condition(Map<String, String> map) {
            ArrayList list = new ArrayList();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(" 1=1 ");//防止后面报错
            Set<String> keys = map.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                if (value != null) {
                    stringBuffer.append(" and " + key + " =? ");
                    list.add(value);
                }
            }
            this.whereClause = stringBuffer.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }
    }

    public abstract String createDataNase();

}
