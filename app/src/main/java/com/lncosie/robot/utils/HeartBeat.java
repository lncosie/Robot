package com.lncosie.robot.utils;

import com.lncosie.robot.Config.RobotConfig;

/**
 * Created by lncosie on 2016/5/2.
 */
public class HeartBeat extends Thread {
    private int mHeartBeatTime = 10;
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
            mHeartBeatTime = HttpUtil.heartBeatGet(RobotConfig.INSTANCE.getWxid());
            mHeartBeatTime = 2;
            //Log.i("thread time", "mywxId: " + GlobalData.MY_WX_ID);
            try {
                sleep(mHeartBeatTime * 1000);
            } catch (InterruptedException e) {
            }
        }

    }

}

