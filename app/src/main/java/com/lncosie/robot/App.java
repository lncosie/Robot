package com.lncosie.robot;

import android.app.Application;
import android.os.StrictMode;

import com.lncosie.robot.orm.Orm;
import com.lncosie.toolkit.Logger;
import com.lncosie.toolkit.shell.Shell;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by qishui on 16/4/16.
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Shell.INSTANCE.open();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SQLiteDatabase.loadLibs(this);
        Logger.INSTANCE.exEnable();
        Orm.OrmInit(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
