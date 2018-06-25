package com.zhengkai.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Alienware on 2018-05-31.
 */

public class HttpRuturn {

    /**
     * 发送消息到服务器
     *
     * @param message ：发送的消息
     * @return：消息对象
     */
    public static ChatMessage sendMessage(String message) {

        ChatMessage chatMessage = new ChatMessage();

        String gsonResult = doGet(message);

        Gson gson = new Gson();
        Result result = null;

        if (gsonResult != null) {
            try {
                JsonParser parser=new JsonParser();

                JsonArray jsonArray=parser.parse(gsonResult).getAsJsonArray();

                ArrayList<String> jsonlist=new ArrayList<>();


                Type listType = new TypeToken<LinkedList<JSONBean>>(){}.getType();

                LinkedList<JSONBean> users = gson.fromJson(gsonResult, listType);

                for (int i=0;i<users.size();i++) {
/*
                    String getquestion=users.get(i).getQuestion();
                    System.err.println(getquestion);*/
                    chatMessage.setMessage(users.get(0).getAnswer());

                }
/*
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {

                    JSONBean jsonBean = (JSONBean) iterator.next();

                    String getanswer=jsonBean.getAnswer();

                    jsonlist.add(getanswer);

                }
*/
            } catch (Exception e) {

                chatMessage.setMessage("网络差！请稍后再试");
            }
        }else {
            String error="对不起，你问题的回答彭圳深还没有做好";
            chatMessage.setMessage(error);

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
        System.out.println("------------url = " + url);
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
            url = Config.URL_KEY + URLEncoder.encode(message, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
