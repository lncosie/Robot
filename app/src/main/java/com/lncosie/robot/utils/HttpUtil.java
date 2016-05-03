package com.lncosie.robot.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lncosie.robot.Config.Config;
import com.lncosie.robot.bean.LoopRsps;
import com.lncosie.robot.bean.UploadRsps;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qishui on 16/3/10.
 */
public class HttpUtil {
    public static OkHttpClient client = new OkHttpClient();
//    public static final String URL = "http://book.dev/";
//    public static final String URL = "http://10.10.105.168/";

    public static UploadRsps postData(String content, String robot_id, String user, String book_sn) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("robot_id", robot_id)
                .addFormDataPart("user", user)
                .addFormDataPart("book_sn", book_sn)
                .addFormDataPart("content", content).build();

        Request request = new Request.Builder()
                .url(Config.INSTANCE.getUrl() + "tools/up")
                .post(requestBody)
                .build();

        String resstr = request.toString();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String rspStr = response.body().string();
                UploadRsps rsps = JSON.parseObject(rspStr, UploadRsps.class);
                return rsps;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static LoopRsps loopMsg(String robot_id, String user, String up_code) {
        HttpUrl.Builder builder = HttpUrl.parse(Config.INSTANCE.getUrl() + "tools/interval").newBuilder();
        builder.addQueryParameter("robot_id", robot_id);
        builder.addQueryParameter("user", user);
        builder.addQueryParameter("up_code", up_code);

        String url = builder.build().toString();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String rsp = response.body().string();
                LoopRsps loopRsps = JSON.parseObject(rsp, LoopRsps.class);
                return loopRsps;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int heartBeatGet(String robot_id) {
        HttpUrl.Builder builder = HttpUrl.parse(Config.INSTANCE.getUrl() + "tools/alive").newBuilder();
        builder.addQueryParameter("robot_id", robot_id);
        String url = builder.build().toString();
        Request request = new Request.Builder().url(url).build();
        int heartBeat = 10;
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String rsp = response.body().string();
                JSONObject object = new JSONObject(rsp);
                object.getString("interval");
               // new JSON(rsp);
            }

        } catch (IOException e) {
             e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  heartBeat;
    }

}
