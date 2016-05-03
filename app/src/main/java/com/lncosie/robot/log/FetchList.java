package com.lncosie.robot.log;

/**
 * Created by Administrator on 2016/4/21.
 */
@Table("FetchList")
public class FetchList {
    public FetchList() {
        time = TimeNow.now();
    }

    @Id
    public Long id;
    public String user;
    public String time;
}
