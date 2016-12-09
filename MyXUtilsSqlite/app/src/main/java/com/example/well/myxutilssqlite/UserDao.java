package com.example.well.myxutilssqlite;

import com.example.well.myxutilssqlite.sqlite.BaseDao;

/**
 * Created by Well on 2016/12/9.
 * 创建具体的表
 */

public class UserDao extends BaseDao {
    @Override
    public String createDataNase() {
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }
}
