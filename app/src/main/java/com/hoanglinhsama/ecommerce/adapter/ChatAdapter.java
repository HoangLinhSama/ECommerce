package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessage> listChatMessage; // listChatMessage chua cac tin nhan cua ca nguoi gui va nguoi nhan nhan tin qua lai
    private String sendId; // id cua nguoi gui de xac dinh trong listMessage, dau la message cua nguoi gui, dau la message cua nguoi nhan
    private static final int TYPE_SEND = 1; // message thuoc loai cua nguoi gui
    private static final int TYPE_RECEIVE = 2; // message thuoc loai cua nguoi nhan

    public ChatAdapter(Context context, List<ChatMessage> listChatMessage, String sendId) {
        this.context = context;
        this.listChatMessage = listChatMessage;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == TYPE_SEND) {
            convertView = layoutInflater.inflate(R.layout.item_send_chat, null);
            return new SendMessageViewHolder(convertView);
        } else {
            convertView = layoutInflater.inflate(R.layout.item_receive_chat, null);
            return new ReceiveMessageViewHolder(convertView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = listChatMessage.get(position);
        if (holder instanceof SendMessageViewHolder) {
            SendMessageViewHolder sendMessageViewHolder = (SendMessageViewHolder) holder;
            sendMessageViewHolder.textViewContentMessageSend.setText(chatMessage.getContentMessage());
            sendMessageViewHolder.textViewDateTimeSend.setText(chatMessage.getDateTime());
        } else {
            ReceiveMessageViewHolder receiveMessageViewHolder = (ReceiveMessageViewHolder) holder;
            receiveMessageViewHolder.textViewContentMessageReceive.setText(chatMessage.getContentMessage());
            receiveMessageViewHolder.textViewDateTimeReceive.setText(chatMessage.getDateTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listChatMessage.get(position).getSendId().equals(sendId)) { // nguoi gui
            return TYPE_SEND;
        } else {
            return TYPE_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return listChatMessage.size();
    }

    public class SendMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContentMessageSend;
        private TextView textViewDateTimeSend;

        public SendMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewContentMessageSend = itemView.findViewById(R.id.text_view_content_message_send);
            this.textViewDateTimeSend = itemView.findViewById(R.id.text_view_date_time_send);
        }
    }

    public class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContentMessageReceive;
        private TextView textViewDateTimeReceive;

        public ReceiveMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewContentMessageReceive = itemView.findViewById(R.id.text_view_content_message_receive);
            this.textViewDateTimeReceive = itemView.findViewById(R.id.text_view_date_time_receive);
        }
    }
}
