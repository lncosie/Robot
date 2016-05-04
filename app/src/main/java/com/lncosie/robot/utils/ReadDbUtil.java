package com.lncosie.robot.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import com.lncosie.robot.bean.SnsItem;
import com.lncosie.robot.flow.Envirment;
import com.lncosie.toolkit.Logger;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qishui on 16/3/9.
 * 读取数据库
 */
public class ReadDbUtil {
    public static boolean readDataBase(Context context, Envirment state) throws IOException, InterruptedException {
        String dbFilePath = ReadDbUtil.getUserPath() + "SnsMicroMsg.db";

        ShellUtil.executeCmd("chmod 777 /data/data/com.tencent.mm");
        ShellUtil.executeCmd("chmod 777 /data/data/com.tencent.mm/MicroMsg/");
        ShellUtil.executeCmd("chmod 777 " + ReadDbUtil.getUserPath());
        ShellUtil.executeCmd("chmod 666 " + ReadDbUtil.getUserPath() + "SnsMicroMsg.db");
        ShellUtil.executeCmd("chmod 666 " + ReadDbUtil.getUserPath() + "SnsMicroMsg.db-shm");
        ShellUtil.executeCmd("chmod 666 " + ReadDbUtil.getUserPath() + "SnsMicroMsg.db-wal");
        ShellUtil.executeCmd("chmod 666 " + ReadDbUtil.getUserPath() + "SnsMicroMsg.db.ini");
        ShellUtil.executeCmd("chmod 666 " + ReadDbUtil.getUserPath() + "SnsMicroMsg.db-journal");

        File file = new File(dbFilePath);
        if (!file.exists()) {
            Logger.INSTANCE.loge("无法打开数据库");
            return false;
        }

        List<String> ids = EncryptedDbHelper.getUsernameByNickName(state.getUsernick());

        if (ids == null || ids.size() == 0) {
            Logger.INSTANCE.loge("无法打开数据库");
            return false;
        }

        String wxId = ids.get(0);

        state.setUserid(wxId);
        SQLiteDatabase snsDb = SQLiteDatabase.openOrCreateDatabase(file, null);
        Logger.INSTANCE.loge("用来查的微信号是" + wxId);
        Cursor cur = snsDb.rawQuery("select userName, content, type, head, createTime, stringSeq, attrBuf from snsInfo where userName='" + wxId + "' order by createTime desc;", null);
        List<SnsItem> snsItems = new ArrayList<SnsItem>();

        while (cur.moveToNext()) {
            String username = cur.getString(0);
            byte[] contentBytes = cur.getBlob(1);
            int createTime = cur.getInt(4);

            SnsItem item = new SnsItem();
            item.wxid = username;
            item.createTime = createTime;
            item.contentBase64 = Base64.encodeToString(contentBytes, Base64.DEFAULT);
            item.nickName = state.getUsernick();
            snsItems.add(item);
        }

        for (SnsItem thisItem : snsItems) {
            Logger.INSTANCE.log("name " + thisItem.wxid + " time " + thisItem.createTime);
        }
        cur.close();
        snsDb.close();
        state.setUpload(JSON.toJSONString(snsItems));
        return true;
    }

    public static String getUserPath() {
        return "/data/data/com.tencent.mm/MicroMsg/" + EncryptedDbHelper.findUserFolder() + "/";
    }

}
