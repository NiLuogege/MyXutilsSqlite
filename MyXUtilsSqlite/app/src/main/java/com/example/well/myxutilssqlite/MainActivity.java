package com.example.well.myxutilssqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.well.myxutilssqlite.sqlite.DaoManagerFactory;

public class MainActivity extends AppCompatActivity {

    private UserDao mDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataHelper = DaoManagerFactory.getInstance().getDataHelper(UserDao.class, User.class);
    }


    public void insert(View view) {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("张三" + i);
            user.setPassword("000000" + i);

            mDataHelper.insert(user);
        }
    }

    public void delete(View view) {
        User user = new User();
        user.setName("张三2");
        mDataHelper.delete(user);
    }

    public void updata(View view) {
        User user = new User();
        user.setName("lalala");
        user.setPassword("1234566");

        User where = new User();
        where.setName("张三");

        mDataHelper.updata(user, where);
    }
}
