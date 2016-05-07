package com.lncosie.robot.log;

import com.lncosie.robot.orm.Id;
import com.lncosie.robot.orm.Table;
import com.lncosie.robot.orm.TimeNow;

/**
 * Created by Administrator on 2016/4/21.
 */
@Table("RedoUsers")
public class RedoUserList {
    public RedoUserList() {
        time = TimeNow.now();
    }

    @Id
    public Long id;
    public String user;
    public String time;
}
