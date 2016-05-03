package com.lncosie.robot.bean;

/**
 * Created by qishui on 16/3/15.
 */
public class Record {

    /**
     * contentBase64 : ChQxMjIyNzEwMjMwMDc1MDM1MjU3NxIJc29kYXBhbmRhGAAgsvCDtwUqBua1i+ivlTIfDQAAAAAV
     AAAAABoAIgAqADIAOABIAFAAWABlAAAAADoKCgASABoAIgAqAELqAgoAEAEaACIAKt8CChQxMjIy
     NzEwMjMwMDkwOTk5ODI3NxACGgbmtYvor5UibGh0dHA6Ly9tbXNucy5xcGljLmNuL21tc25zL2tC
     Q3l6bmdnVTA4b2V1MWNwSmZxM0FpY0dZc2s4Z21PQTVoQVpoQ2JHNVZQRW9LR3pwaWFDbnROaWJ3
     ZWliQThFN003WlBTc0h6WGdwWEUvMCgBMm5odHRwOi8vbW1zbnMucXBpYy5jbi9tbXNucy9rQkN5
     em5nZ1UwOG9ldTFjcEpmcTNBaWNHWXNrOGdtT0E1aEFaaENiRzVWUEVvS0d6cGlhQ250Tmlid2Vp
     YkE4RTdNN1pQU3NIelhncFhFLzE1MDgBQABKAFIPDQAAcEQVAACgRB0AAAAAWgBgAGgAcAB6AIAB
     AJIBAJoBAKIBAKgBALIBALoBAMABAMgBANIBIDE4NGU5ZjY1NWYyN2Y5ODlhMTYzNDgzZGQxMjE0
     NjYySgBSAFoAYABoA3IAeiQIABIAGAAiACoAMgA6CAoAEgAaACIAQgBKBAgAEABSBAoAEgCAAQCK
     AQwKABAAGgAiACoAMgA=

     * createTime : 1457584178
     * nickName : 汽水
     * wxid : sodapanda
     */

    private String contentBase64;
    private int createTime;
    private String nickName;
    private String wxid;

    public void setContentBase64(String contentBase64) {
        this.contentBase64 = contentBase64;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getContentBase64() {
        return contentBase64;
    }

    public int getCreateTime() {
        return createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public String getWxid() {
        return wxid;
    }
}
