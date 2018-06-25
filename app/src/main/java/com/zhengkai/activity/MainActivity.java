package com.zhengkai.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengkai.Bean.JSONBean;
import com.zhengkai.alienware.pigproject.R;
import com.zhengkai.example.widget.SeedURL;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 测试类(这是一个没有用的类）
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private AutoCompleteTextView et;
    private Button btn;
    private Socket socket;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        et = (AutoCompleteTextView) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);

        String [] province=getResources().getStringArray(R.array.data);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,province );
        AutoCompleteTextView act=(AutoCompleteTextView) findViewById(R.id.AT);
        act.setAdapter(adapter);
//     socket没用


        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                 final String data = et.getText().toString();

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            OutputStream outputStream = socket.getOutputStream();
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
                            outputStream.write((socket.getLocalPort() + "//" + data + "//" + df.format(new Date())).getBytes("utf-8"));
                            outputStream.flush();

                        } catch (IOException e) {

                            e.printStackTrace();

                        }
                    }
                }).start();

                switch(v.getId()) {
                    case R.id.btn:
                        Handquestion();
                        break;
                    default:
                        break;
                }



            }
        });




    }



    //  hand传url值
private void Handquestion() {

        String getQuestion = et.getText().toString();

        ResfulRequest(getQuestion);

        }

    public void ResfulRequest(String getQuestion) {

        SeedURL seedURL=new SeedURL();

        String Url=seedURL.setUrl(getQuestion).toString();

       /* 请求URL写法
        String url = "http://39.105.124.151/search/术语";    */

        String tag = getQuestion;    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

/** 实例化StringRequest请求
 *  第一个参数选择Request.Method.GET即get请求方式
 *  第二个参数的url地址根据文档所给
 *  第三个参数Response.Listener 请求成功的回调
 *  第四个参数Response.ErrorListener 请求失败的回调
 */

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, Url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

//                        数组的解析

                        Type listType = new TypeToken<LinkedList<JSONBean>>(){}.getType();

                        Gson gson = new Gson();

                        LinkedList<JSONBean> users = gson.fromJson(response, listType);

                        ArrayList questionList = new ArrayList();



                        for (Iterator iterator = users.iterator(); iterator.hasNext();) {

                            JSONBean jsonBean = (JSONBean) iterator.next();

                            String getquestion=jsonBean.getQuestion();



                        }

//                        迭代器（Iterator）
 /*
                        JSONBean.HitsBeanX hits=jsonBean.getHits();
/*          hit拿到_hits JSONArray
*          JSON对象转字符串：
*        String listStr = gs.toJson(hit);
*
                        List<HomeHits> hit=hits.getHit();
                        String listStr = gs.toJson(hit);

                        //先转JsonObject
                        JsonObject jsonObject = new JsonParser().parse(listStr).getAsJsonObject();
                        //再转JsonArray 加上数据头
                        JsonArray jsonArray = jsonObject.getAsJsonArray("hits");

                        ArrayList<HomeHits> userBeanList = new ArrayList<>();

                        //循环遍历
                        for (JsonElement user : jsonArray) {
                            //通过反射 得到UserBean.class
                            HomeHits homeHits = gs.fromJson(user, new TypeToken<HomeHits>() {}.getType());
                            userBeanList.add(homeHits);
                        }

                        Log.v("TAG", String.valueOf(userBeanList));
*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //失败的响应
                Toast toast = Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT);
                toast.show();
                Log.e("TAG", error.getMessage(), error);
            }
        });
        //设置Tag标签
        stringRequest.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(stringRequest);
    }








}
