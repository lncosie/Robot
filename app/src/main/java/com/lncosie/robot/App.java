package com.lncosie.robot;

import android.app.Application;
import android.os.StrictMode;

import com.lncosie.robot.log.Orm;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by qishui on 16/4/16.
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SQLiteDatabase.loadLibs(this);
        Orm.OrmInit(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
