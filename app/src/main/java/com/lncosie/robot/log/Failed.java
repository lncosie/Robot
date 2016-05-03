package com.lncosie.robot.log;

/**
 * Created by Administrator on 2016/4/21.
 */


@Table("Failed")
public class Failed {
    public Failed(){
        time=TimeNow.now();
    }
    @Id
    public Long id;
    public String  user;
    public String  time;
    public String  reason;
}
