package com.lncosie.robot.orm;

import java.sql.Time;

/**
 * Created by Administrator on 2016/4/25.
 */
public class TimeNow {
    public static String now(){
        Time time = new Time(System.currentTimeMillis());
        return time.toString();
    }
}
