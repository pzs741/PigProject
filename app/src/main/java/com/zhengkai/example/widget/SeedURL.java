package com.zhengkai.example.widget;

/**
 * Created by Alienware on 2018-05-23.
 */

public class SeedURL {


    public StringBuffer setUrl(String question){

        StringBuffer url=new StringBuffer("http://39.105.124.151/search/");

        url.append(question);

        return url;


    }

}
