package com.lncosie.robot.books;

import com.lncosie.robot.log.Id;
import com.lncosie.robot.log.Table;
import com.lncosie.robot.log.TimeNow;

/**
 * Created by Administrator on 2016/4/21.
 */
@Table("Robot")
public class Robot {
    public Robot(){
        time= TimeNow.now();
    }
    @Id
    public Long id;
    public String action;
    public String time;
    public String message;
}
