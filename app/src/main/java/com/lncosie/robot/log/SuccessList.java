package com.lncosie.robot.log;

/**
 * Created by Administrator on 2016/4/21.
 */
@Table("SuccessList")
public class SuccessList {
    public SuccessList() {
        time = TimeNow.now();
    }

    @Id
    public Long id;
    public String user;
    public String time;
}
