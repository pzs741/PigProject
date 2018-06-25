package com.zhengkai.activity;

/**
 * Created by Alienware on 2018-05-30.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhengkai.Bean.ChatMessage;
import com.zhengkai.Bean.JSONBean;
import com.zhengkai.alienware.pigproject.R;
import com.zhengkai.example.adapter.ChatMessageAdapter;
import com.zhengkai.example.utils.HttpRuturn;
import com.zhengkai.example.utils.HttpUtils;
import com.zhengkai.example.utils.SearchHttp;
import com.zhengkai.example.widget.SeedURL;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class ChatActivity extends AppCompatActivity {
    private String msg;
    private List<ChatMessage> list;
    private ListView chat_listview;
    private AutoCompleteTextView chat_input;
    private Button chat_send;
    private ChatMessageAdapter chatAdapter;
    private ChatMessage chatMessage = null;
    private ArrayList<String> getlist;
    public static RequestQueue sRequestQueue;
    private String TAG = "RxJava";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);



        final String [] data=getResources().getStringArray(R.array.data);

         final AutoCompleteTextView act=(AutoCompleteTextView) findViewById(R.id.chat_input_message);

        RxTextView.textChanges(act)
                .debounce(10, TimeUnit.MILLISECONDS)
                .skip(1)/*去掉第一次选中*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final CharSequence charSequence) {

                      new Thread() {
                            public void run() {
                                ChatMessage chat = SearchHttp.sendMessage(charSequence.toString());
                                System.err.println(chat.getMessage2());
                                Message message = new Message();
                                message.what = 0x1;
                                message.obj = chat;
                                handler2.sendMessage(message);

                            }
                        }.start();

                    }


                    @SuppressLint("HandlerLeak") Handler handler2 = new Handler() {

                        public void handleMessage(android.os.Message msg) {
                            if (msg.what == 0x1) {
                                if (msg.obj != null) {
                                    chatMessage = (ChatMessage) msg.obj;
                                }

                                String[] strs={chatMessage.getMessage(),chatMessage.getMessage2(),chatMessage.getMessage3(),chatMessage.getMessage4(),chatMessage.getMessage5()};
                                 ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,strs );
                                act.setAdapter(adapter);
                            }

                        }
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应" );

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });

                act.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        Searchchat();
                    }}
                    );

        initView();
        initListener();
        initData();
    }



    // 1.初始试图
    private void initView() {
        // 1.初始化
        chat_listview = (ListView) findViewById(R.id.chat_listview);
        chat_input = (AutoCompleteTextView) findViewById(R.id.chat_input_message);
        chat_send = (Button) findViewById(R.id.chat_send);
    }

    // 2.设置监听事件
    private void initListener() {
        chat_send.setOnClickListener(onClickListener);

    }

    // 3.初始化数据
    private void initData() {
        list = new ArrayList<ChatMessage>();

        list.add(new ChatMessage("您好！我是有问必答的智能客服，点击进入您要的问题，或者直接输入问题。", ChatMessage.Type.INCOUNT, new Date()));
        chatAdapter = new ChatMessageAdapter(list);
        chat_listview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

    }

/*点击搜索建议直接发送返回答案*/
    public void Searchchat() {
        ChatMessage sendChatMessage = new ChatMessage();
        final String send_message = chat_input.getText().toString().trim();

        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(ChatActivity.this, "对不起，您还未发送任何消息",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        sendChatMessage.setMessage(send_message);
        sendChatMessage.setData(new Date());
        sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
        list.add(sendChatMessage);
        chatAdapter.notifyDataSetChanged();
        chat_input.setText("");

        new Thread() {
            public void run() {
                ChatMessage chat = HttpRuturn.sendMessage(send_message);
                Message message = new Message();
                message.what = 0x1;
                message.obj = chat;
                Searchhandler.sendMessage(message);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler Searchhandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    chatMessage = (ChatMessage) msg.obj;
                }
                // 添加数据到list中，更新数据

                list.add(chatMessage);

                chatAdapter.notifyDataSetChanged();
            }

        }
    };

    // 4.发送消息聊天
    public void chat() {

        // 2.自己输入的内容也是一条记录，记录刷新
        ChatMessage sendChatMessage = new ChatMessage();

//       1.判断是否输入内容
        final String send_message = chat_input.getText().toString().trim();

        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(ChatActivity.this, "对不起，您还未发送任何消息",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        sendChatMessage.setMessage(send_message);
        sendChatMessage.setData(new Date());
        sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
        list.add(sendChatMessage);
        chatAdapter.notifyDataSetChanged();
        chat_input.setText("");

       /* list.add(new ChatMessage("您要问的是不是:", ChatMessage.Type.INCOUNT, new Date()));*/
        // 3.发送你的消息，去服务器端，返回数据
//        线程中

        new Thread() {
            public void run() {
                ChatMessage chat = HttpUtils.sendMessage(send_message);
                Message message = new Message();
                message.what = 0x1;
                message.obj = chat;
                handler.sendMessage(message);
            }
        }.start();
    }

//    线程外
     @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    chatMessage = (ChatMessage) msg.obj;
                }
                // 添加数据到list中，更新数据

                list.add(chatMessage);

                chatAdapter.notifyDataSetChanged();
            }

        }
    };




    // 点击事件监听
    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chat_send:
                    chat();
                    break;
            }
        }
    };
}