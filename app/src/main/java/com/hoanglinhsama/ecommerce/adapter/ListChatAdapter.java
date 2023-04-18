package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.Interface.OnItemClickListener;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.activity.ChatActivity;
import com.hoanglinhsama.ecommerce.activity.ProductDetailActivity;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.model.User;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> {
    private Context context;
    private int layout;
    private List<User> listUser;

    public ListChatAdapter(Context context, int layout, List<User> listUser) {
        this.context = context;
        this.layout = layout;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public ListChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, null);
        return new ListChatAdapter.MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.MyViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.textViewNameUser.setText(user.getName());
        holder.setOnItemClickListener((view, position1, isLongClick) -> {
            if (!isLongClick) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idUser", String.valueOf(user.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewNameUser;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewNameUser = itemView.findViewById(R.id.text_view_name_user_item_list_chat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
