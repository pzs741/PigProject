package com.zhengkai.example.adapter;

/**
 * Created by Alienware on 2018-05-30.
 */

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhengkai.Bean.ChatMessage;
import com.zhengkai.alienware.pigproject.R;
import com.zhengkai.example.utils.DateUtils;
import com.zhengkai.example.utils.HttpRuturn;
import com.zhengkai.example.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zhengkai.Bean.ChatMessage.Type.OUTCOUNT;


/**
 * 聊天信息适配器
 *
 */

public class ChatMessageAdapter extends BaseAdapter {

    private String msg;
    private ChatMessage chatMessage = null;
    private  String message;
    private List<ChatMessage> list;
    private ArrayList<String> getlist;

    public ChatMessageAdapter(List<ChatMessage> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.isEmpty() ? 0 : list.size();
    }



    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = list.get(position);
        // 如果是接收消息：0，发送消息：1
        if (chatMessage.getType() == ChatMessage.Type.INCOUNT) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*获取服务器传过来的值*/
        final ChatMessage chatMessage = list.get(position);

        if (convertView == null) {
            ViewHolder viewHolder = null;

            // 通过ItemType加载不同的布局
            if (getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_left, null);
                viewHolder = new ViewHolder();
                viewHolder.chat_time = (TextView) convertView
                        .findViewById(R.id.chat_left_time);
                    viewHolder.chat_message = (TextView) convertView
                            .findViewById(R.id.chat_left_message1);
               viewHolder.chat_message2 = (TextView) convertView
                        .findViewById(R.id.chat_left_message2);
                 viewHolder.chat_message3 = (TextView) convertView
                        .findViewById(R.id.chat_left_message3);
                viewHolder.chat_message4 = (TextView) convertView
                        .findViewById(R.id.chat_left_message4);
                viewHolder.chat_message5 = (TextView) convertView
                        .findViewById(R.id.chat_left_message5);

            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_right, null);
                viewHolder = new ViewHolder();
                viewHolder.chat_time = (TextView) convertView
                        .findViewById(R.id.chat_right_time);
                viewHolder.chat_message = (TextView) convertView
                        .findViewById(R.id.chat_right_message1);
               viewHolder.chat_message2 = (TextView) convertView
                        .findViewById(R.id.chat_right_message2);
               viewHolder.chat_message3 = (TextView) convertView
                        .findViewById(R.id.chat_right_message3);
                viewHolder.chat_message4 = (TextView) convertView
                        .findViewById(R.id.chat_right_message4);
                viewHolder.chat_message5 = (TextView) convertView
                        .findViewById(R.id.chat_right_message5);


            }
            convertView.setTag(viewHolder);
        }


//         设置数据
        ViewHolder vh = (ViewHolder) convertView.getTag();


        vh.chat_time.setText(DateUtils.dateToString(chatMessage.getData()));

        vh.chat_message.setText(Html.fromHtml(chatMessage.getMessage()));
        vh.chat_message2.setText(chatMessage.getMessage2());
        vh.chat_message3.setText(chatMessage.getMessage3());
        vh.chat_message4.setText(chatMessage.getMessage4());
        vh.chat_message5.setText(chatMessage.getMessage5());


     //*点击左边才监听*//*
        if (chatMessage.getType() == ChatMessage.Type.INCOUNT) {

              //* 监听Textview*//*
            vh.chat_message.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    msg=chatMessage.getMessage();

//            调用chat()返回点击后的值
                    chat();
                }
            });
            vh.chat_message2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    msg=chatMessage.getMessage2();
//            调用chat()返回点击后的值
                    chat();
                }
            });
            vh.chat_message3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    msg=chatMessage.getMessage3();
//            调用chat()返回点击后的值
                    chat();
                }
            });
            vh.chat_message4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    msg=chatMessage.getMessage4();
//            调用chat()返回点击后的值
                    chat();
                }
            });
            vh.chat_message5.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    msg=chatMessage.getMessage5();
//            调用chat()返回点击后的值
                    chat();
                }
            });

        }

        return convertView;
    }

   private void chat() {

        // 3.发送你的消息，去服务器端，返回数据
        new Thread() {
            public void run() {

                ChatMessage chat = HttpRuturn.sendMessage(msg);
                Message message = new Message();
                message.what = 0x1;
                message.obj = chat;
                handler.sendMessage(message);

            }
        }.start();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    chatMessage = (ChatMessage) msg.obj;
                }

                // 添加数据到list中，更新数据
                list.add(chatMessage);
                notifyDataSetChanged();
            }
        }
    };


    /**
     * 内部类：只寻找一次控件
     *
     * @author zengtao 2015年5月6日 下午2:27:57
     */
    private class ViewHolder {
        private TextView chat_time, chat_message1,chat_message2,chat_message3,chat_message4,chat_message5,chat_message;

    }
}