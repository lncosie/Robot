package com.lncosie.robot.utils;

import android.util.Log;

import com.lncosie.robot.Config.Config;

import java.util.Timer;

/**
 * Created by lncosie on 2016/5/2.
 */
public class HeartBeat extends Thread {
    private int mHeartBeatTime = 10;
    private Timer mTimer = new Timer();
    private boolean isAlive = true;

    public void stop_beat(){
        isAlive=false;
    }
    @Override
    public void run() {
        // super.run();
        //Log.i("thread time", "mywxId3: " + GlobalData.MY_WX_ID);
        while (isAlive) {
            //Log.i("thread time", "mywxId3: " + GlobalData.MY_WX_ID);
            //mHeartBeatTime = HttpUtil.heartBeatGet(Config.MY_WX_ID);
            //   mHeartBeatTime = 2;
            //Log.i("thread time", "mywxId: " + GlobalData.MY_WX_ID);
            try {
                sleep(mHeartBeatTime * 1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();

            }
        }

    }

}

