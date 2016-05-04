package com.lncosie.robot.books;

import com.lncosie.robot.log.Id;
import com.lncosie.robot.log.TimeNow;
import com.lncosie.robot.log.View;

/**
 create view if not exists  c as
 select * from a where a.name not in (select name from b);
 */

@View(value = "TodoList",select = "select * from FetchList where FetchList.user not in (select user from SuccessList)")
public class TodoList {
    public TodoList() {
        time = TimeNow.now();
    }

    @Id
    public Long   id;
    public String user;
    public String time;
}
