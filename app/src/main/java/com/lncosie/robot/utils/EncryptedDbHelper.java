package com.lncosie.robot.utils;

import android.text.TextUtils;
import android.util.Log;

import com.lncosie.toolkit.shell.Shell;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by qishui on 16/3/27.
 */
public class EncryptedDbHelper {
    public static SQLiteDatabase getEncrypedDb() {
        Shell.INSTANCE.post("chmod 777 /data/data/com.tencent.mm");
        Shell.INSTANCE.post("chmod 777 /data/data/com.tencent.mm/MicroMsg/");
        Shell.INSTANCE.post("chmod 777 " + ReadDbUtil.getUserPath());
        Shell.INSTANCE.post("chmod 666 " + ReadDbUtil.getUserPath() + "EnMicroMsg.db");
        Shell.INSTANCE.post("chmod 666 " + ReadDbUtil.getUserPath() + "EnMicroMsg.db-journal");

        Shell.INSTANCE.post("chmod 777 /data/data/com.tencent.mm/MicroMsg/systemInfo.cfg");
        HashMap sysInfoMap = loadHashMapFromFile("/data/data/com.tencent.mm/MicroMsg/systemInfo.cfg");
        if (sysInfoMap == null || sysInfoMap.size() == 0) {
            //Log.e(GlobalData.TAG, "读取systeminfo.cfg错误");
            return null;
        }
        String uin = sysInfoMap.get(1).toString();
        if (uin == null) {
            //Log.e(GlobalData.TAG, "找不到uin");
            return null;
        }

        Shell.INSTANCE.post("chmod 777 /data/data/com.tencent.mm/MicroMsg/CompatibleInfo.cfg");
        HashMap comInfo = loadHashMapFromFile("/data/data/com.tencent.mm/MicroMsg/CompatibleInfo.cfg");
        if (comInfo == null) {
            //Log.e(GlobalData.TAG, "找不到compatibleInfo");
            return null;
        }

        String imei = "";
        if (sysInfoMap.containsKey(258)) {
            imei = sysInfoMap.get(258).toString();
        }
        if (comInfo.containsKey(258)) {
            imei = comInfo.get(258).toString();
        }

        if (TextUtils.isEmpty(imei)) {
            //Log.e(GlobalData.TAG, "找不到imei");
            return null;
        }

        String key = md5(imei + uin).substring(0, 7);

        //Log.i(GlobalData.TAG, "imei " + imei);
        //Log.i(GlobalData.TAG, "key " + key);

        File file = new File(ReadDbUtil.getUserPath() + "EnMicroMsg.db");
        if (!file.exists()) {
            //Log.e(GlobalData.TAG, "enMicroMsg文件不存在");
            return null;
        }

        return SQLiteDatabase.openOrCreateDatabase(file, key, null, new SQLiteDatabaseHook() {
            @Override
            public void preKey(SQLiteDatabase sqLiteDatabase) {
            }

            @Override
            public void postKey(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.rawExecSQL("PRAGMA kdf_iter = 4000;");
                sqLiteDatabase.rawExecSQL("PRAGMA cipher_use_hmac = OFF;");
            }
        });
    }

    public static HashMap loadHashMapFromFile(String path) {
        try {
            ObjectInputStream objReader = new ObjectInputStream(new FileInputStream(path));
            Object hashmapObj = objReader.readObject();
            objReader.close();
            return (HashMap) hashmapObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5(String plainText) {
        return new String(Hex.encodeHex(DigestUtils.md5(plainText)));
    }

    public static List<String> getUsernameByNickName(String nickName) {
        SQLiteDatabase db = getEncrypedDb();
        if (db == null) {
            //Log.e(GlobalData.TAG, "无法打开加密数据库");
            return null;
        }
        Cursor cursor = db.rawQuery("select username from rcontact where verifyFlag = 0 and (conRemark = '" + nickName + "' or nickname = '" + nickName + "');", null);

        List<String> userNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            userNames.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return userNames;
    }

    public static String findUserFolder() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String latestUserFolder = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes("ls -l /data/data/com.tencent.mm/MicroMsg/ | grep ^d\n");
            dos.writeBytes("exit\n");
            dos.flush();
            Date latestModifiedDate = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] info = line.split(" +");
                if (info.length == 6 && info[5].length() == 32) {
                    Date modifiedDate = dateFormat.parse(info[3] + " " + info[4]);
                    if (latestUserFolder == null || modifiedDate.after(latestModifiedDate)) {
                        latestUserFolder = info[5];
                        latestModifiedDate = modifiedDate;
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latestUserFolder;
    }
}
