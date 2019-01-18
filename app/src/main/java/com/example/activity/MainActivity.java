package com.example.activity;

import android.media.MediaExtractor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.ChatMessageAdapter;
import com.example.bean.ChatMessage;
import com.example.bean.Result;
import com.example.myrobot.R;
import com.example.utils.HttpCallbackListener;
import com.example.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnSendRequest;
    private EditText etMsgContent;
    private ListView lvChatMessage;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> data;
    private TextView tvToTime;
    private TextView tvFromTime;
    private long lastTime;
    private long nowTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatMessage message = (ChatMessage) msg.obj;
            data.add(message);
            adapter.notifyDataSetChanged();
            lvChatMessage.setSelection(adapter.getCount()-1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new ChatMessage("很高兴为您服务，主人",new Date(), ChatMessage.Type.INCOMING));
        lastTime = System.currentTimeMillis();
    }

    private void initEvent() {
        /**
         * 当点击发送按钮时
         * 首先获取用户输入的信息，调用adapter.notifyDataSetChanged();方法将其显示到listview中
         * 再调用我们封装的sendOkHttpRequest()方法从接口请求返回的数据
         * 注意此时实现需要okhttp3.Callback接口中的onFailure()和onResponse()方法，分别表示请求失败和请求成功的情况
         * onFailure()和onResponse()方法中依然处于子线程中，如果需要更新界面需要调用runOnUiThread()方法
         * 或使用handler,我们这里选择使用handler
         */
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMsgContent.getText().toString().trim();
                if(TextUtils.isEmpty(message)) {
                    return;
                }
                ChatMessage toMessage = new ChatMessage(message,new Date(), ChatMessage.Type.OUTCOMING);
                data.add(toMessage);
//                nowTime = System.currentTimeMillis();
//                if(nowTime - lastTime > 5 * 1000) {
//                    tvToTime.setVisibility(View.VISIBLE);
//                } else {
//                    tvToTime.setVisibility(View.GONE);
//                }
//                lastTime = nowTime;
                adapter.notifyDataSetChanged();
                lvChatMessage.setSelection(adapter.getCount()-1);
                etMsgContent.setText("");
                HttpUtils.sendOkHttpRequest(message,new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {//请求过程中出现错误的回调
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"请求过程中出错了",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {//请求成功的回调
                        final String strResponse = parJson(response.body().string());//从返回的Json数据中解析出有用的数据
                        ChatMessage fromMessage = new ChatMessage(strResponse,new Date(),ChatMessage.Type.INCOMING);
                        Message message2 = new Message();
                        message2.obj = fromMessage;
                        handler.sendMessage(message2);
                    }
                });
            }
        });

        etMsgContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = etMsgContent.getText().toString().trim();
                if(TextUtils.isEmpty(content)) {
                    btnSendRequest.setBackgroundResource(R.drawable.btn_sended);
                } else {
                    btnSendRequest.setBackgroundResource(R.drawable.btn_sending);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        btnSendRequest = findViewById(R.id.btn_send_request);
        etMsgContent = findViewById(R.id.et_message_content);
        tvToTime = findViewById(R.id.tv_to_time);
        tvFromTime = findViewById(R.id.tv_from_time);
        lvChatMessage = findViewById(R.id.list_chat_msg);
        adapter = new ChatMessageAdapter(this,data);
        lvChatMessage.setAdapter(adapter);
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
    private String parJson(String responseData) {
        Gson gson = new Gson();
        String strResult = "";
        Result result = gson.fromJson(responseData,new TypeToken<Result>(){}.getType());
        strResult = result.getResults().get(0).getValues().getText();
        Log.d("xxx","返回的结果为：" + strResult);
        return strResult;
    }
}
