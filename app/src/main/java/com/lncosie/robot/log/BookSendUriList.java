package com.lncosie.robot.log;

import com.lncosie.robot.orm.Id;
import com.lncosie.robot.orm.Table;
import com.lncosie.robot.orm.TimeNow;

/**
 * Created by Administrator on 2016/4/21.
 */


@Table("BookSendUriList")
public class BookSendUriList {
    public BookSendUriList(){
        time= TimeNow.now();
    }
    @Id
    public Long id;
    public String  time;
    public String  user;
    public String  userid;
    public String  upcode;
    public String  uri;
}
