package com.example.well.myxutilssqlite.sqlite;

/**
 * Created by Well on 2016/12/9.
 * 定义增删改查
 */

public interface IBaseDao<T> {
    Long insert(T entity);

    int delete(T entity);

    int updata(T entiry, T where);


}
