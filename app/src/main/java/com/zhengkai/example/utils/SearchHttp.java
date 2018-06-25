package com.zhengkai.example.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengkai.Bean.ChatMessage;
import com.zhengkai.Bean.JSONBean;
import com.zhengkai.example.widget.Config;
import com.zhengkai.example.widget.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Alienware on 2018-06-20.
 */

public class SearchHttp {


    /**
     * 发送搜索联想
     *
     *
     * @return：消息对象
     */


    public static ChatMessage sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();

        String gsonResult = doGet(message);

        Gson gson = new Gson();
//        获取从服务器接受到的JSON串
        Result result = null;

        if (gsonResult != null) {
            try {
//                解析JSON
                Type listType = new TypeToken<LinkedList<JSONBean>>(){}.getType();

                LinkedList<JSONBean> users = gson.fromJson(gsonResult, listType);

                chatMessage.setMessage(users.get(0).getQuestion());
                chatMessage.setMessage2(users.get(1).getQuestion());
                chatMessage.setMessage3(users.get(2).getQuestion());
                chatMessage.setMessage4(users.get(3).getQuestion());
                chatMessage.setMessage5(users.get(4).getQuestion());



            } catch (Exception e) {


            }
        }
        chatMessage.setData(new Date());
        chatMessage.setType(ChatMessage.Type.INCOUNT);
        return chatMessage;
    }

    /**
     * get请求
     *
     * @param message ：发送的话
     * @return：数据
     */
    public static String doGet(String message) {

        String result = "";
        String url = setParmat(message);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL urls = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urls
                    .openConnection();
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("GET");

            is = connection.getInputStream();
            baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            baos.flush();
            result = new String(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        result是一个完整的JSON串
        return result;

    }

    /**
     * 设置参数
     *
     * @param message : 信息
     * @return ： url
     */
    private static String setParmat(String message) {

        String url = "";
        try {
            url = Config.URL_KEY + URLEncoder.encode(message, "UTF-8").toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
