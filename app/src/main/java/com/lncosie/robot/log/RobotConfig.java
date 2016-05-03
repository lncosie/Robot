package com.lncosie.robot.log;

/**
 * Created by guazi on 2016/4/27.
 */
@Table("RobotConfig")
public class RobotConfig {
    @Id
    public Long id;
    public String name;
    public String value;
}
