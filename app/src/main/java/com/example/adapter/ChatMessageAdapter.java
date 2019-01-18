package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bean.ChatMessage;
import com.example.myrobot.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ChatMessage> mData;
    public ChatMessageAdapter(Context context,List<ChatMessage> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mData.get(position);
        if(chatMessage.getType() == ChatMessage.Type.INCOMING) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            if(getItemViewType(position) == 0) {
                convertView = mInflater.inflate(R.layout.item_from_layout,parent,false);
                holder.tvMessageDate = convertView.findViewById(R.id.tv_from_time);
                holder.tvMessageContent = convertView.findViewById(R.id.tv_from_msg_info);
            } else {
                convertView = mInflater.inflate(R.layout.item_to_layout,parent,false);
                holder.tvMessageDate = convertView.findViewById(R.id.tv_to_time);
                holder.tvMessageContent = convertView.findViewById(R.id.tv_to_msg_info);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChatMessage message = mData.get(position);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        holder.tvMessageDate.setText(format.format(message.getDate()));
        holder.tvMessageContent.setText(message.getContent());
        return convertView;
    }

    private class ViewHolder {
        TextView tvMessageDate;
        TextView tvMessageContent;
    }
}
