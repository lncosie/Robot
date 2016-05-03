package com.lncosie.robot.log;

/**
 * Created by Administrator on 2016/4/21.
 */
@Table("Robot")
public class Robot {
    public Robot(){
        time=TimeNow.now();
    }
    @Id
    public Long id;
    public String action;
    public String time;
    public String message;
}
