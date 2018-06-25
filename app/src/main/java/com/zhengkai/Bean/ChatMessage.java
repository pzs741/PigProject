package com.zhengkai.Bean;

/**
 * Created by Alienware on 2018-05-30.
 */

import java.util.ArrayList;
import java.util.Date;

/**
 * 聊天消息的实体类
 */
public class ChatMessage {

    private String name;// 姓名
    private String message;// 消息
    private String message1;// 消息
    private String message2;// 消息
    private String message3;// 消息
    private String message4;// 消息
    private String message5;// 消息
    private Type type;// 类型：0.发送者 1.接受者
    private Date data;// 时间
    private String msg;
    private ArrayList<String> list;


    public ChatMessage() {

    }

    public ChatMessage(String message, Type type, Date data) {
        super();
        this.message = message;
        this.type = type;
        this.data = data;

    }

    public ArrayList<String> getList(){
        return list;
    }

    public  void setList(ArrayList<String> list){
        this.list=list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


   public  String getMsg(){
        return this.msg;
    }


    public void  setMsg(String msg){
        this.msg=msg;

    }


    public String getMessage() {

        return message;
    }


    public void setMessage(String message) {

        this.message = message;
    }


    public String getMessage1() {

        return message1;
    }


    public void setMessage1(String message1) {

        this.message1 = message1;
    }
    public String getMessage2() {

        return message2;
    }


    public void setMessage2(String message2) {

        this.message2 = message2;
    }
    public String getMessage3() {

        return message3;
    }


    public void setMessage3(String message3) {

        this.message3 = message3;
    }

    public String getMessage4() {

        return message4;
    }


    public void setMessage4(String message4) {

        this.message4 = message4;
    }

    public String getMessage5() {

        return message5;
    }


    public void setMessage5(String message5) {

        this.message5 = message5;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public enum Type {
        INCOUNT, OUTCOUNT
    }
}