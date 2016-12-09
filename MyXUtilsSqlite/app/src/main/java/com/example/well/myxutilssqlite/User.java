package com.example.well.myxutilssqlite;

import com.example.well.myxutilssqlite.sqlite.DbFiled;
import com.example.well.myxutilssqlite.sqlite.DbTable;

/**
 * Created by Well on 2016/12/9.
 */
@DbTable("tb_user")
public class User {
    @DbFiled("name")
    public String name;
    public String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
