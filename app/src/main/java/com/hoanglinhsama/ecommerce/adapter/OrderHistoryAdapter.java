package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.Interface.OnItemClickListener;
import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.eventbus.UpdateStatusOrderEvent;
import com.hoanglinhsama.ecommerce.model.Order;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    private List<Order> listOrderHistory;
    private int layout;
    private Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public OrderHistoryAdapter(List<Order> listOrderHistory, int layout, Context context) {
        this.listOrderHistory = listOrderHistory;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, null);
        return new OrderHistoryAdapter.MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.MyViewHolder holder, int position) {
        Order order = listOrderHistory.get(position);
        holder.textViewStatusOrderHistory.setText(statusOrder(order.getStatus()));
        if (ApiUtils.currentUser.getType() == 2) // an address doi voi user
        {
            holder.linearLayoutAddress.setVisibility(View.INVISIBLE);
        } else { // admin
            holder.textViewAddressOrderHistory.setText(order.getAddress());
            // chi admin moi duoc bat su kien click doi voi moi item_order_history
            holder.setOnItemClickListener((view, position1, isLongClick) -> {
                if (!isLongClick) {
                    EventBus.getDefault().post(new UpdateStatusOrderEvent(order));
                }
            });
        }
        holder.textViewIdOrderHistory.setText(String.valueOf(order.getId()));
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = inputFormat.parse(order.getDate());
            holder.textViewDateOrderHistory.setText(outputFormat.format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewTotalPriceOrderHistory.setText(decimalFormat.format(Double.parseDouble(String.valueOf(order.getTotalPrice()))) + "₫");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.recyclerViewDetailOrderHistory.getContext(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(order.getListProductOrder().size());
        DetailOrderHistoryAdapter detailOrderHistoryAdapter = new DetailOrderHistoryAdapter(order.getListProductOrder(), R.layout.item_detail_order_history, context);
        holder.recyclerViewDetailOrderHistory.setLayoutManager(linearLayoutManager);
        holder.recyclerViewDetailOrderHistory.setAdapter(detailOrderHistoryAdapter);
        holder.recyclerViewDetailOrderHistory.setRecycledViewPool(viewPool);
        holder.recyclerViewDetailOrderHistory.addItemDecoration(new ItemDecoration(15));
    }

    @Override
    public int getItemCount() {
        return listOrderHistory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewIdOrderHistory;
        private TextView textViewDateOrderHistory;
        private TextView textViewTotalPriceOrderHistory;
        private TextView textViewAddressOrderHistory;
        private TextView textViewStatusOrderHistory;
        private RecyclerView recyclerViewDetailOrderHistory;
        private LinearLayout linearLayoutStatus;
        private LinearLayout linearLayoutAddress;
        private OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewIdOrderHistory = itemView.findViewById(R.id.text_view_id_order_history_screen);
            this.textViewDateOrderHistory = itemView.findViewById(R.id.text_view_date_order_history_screen);
            this.textViewTotalPriceOrderHistory = itemView.findViewById(R.id.text_view_total_price_order_history_screen);
            this.recyclerViewDetailOrderHistory = itemView.findViewById(R.id.recycler_view_detail_order_history_screen);
            this.textViewAddressOrderHistory = itemView.findViewById(R.id.text_view_address_order_history_screen);
            this.textViewStatusOrderHistory = itemView.findViewById(R.id.text_view_status_order_history_screen);
            this.linearLayoutAddress = itemView.findViewById(R.id.linear_layout_address_order_history_screen);
            this.linearLayoutStatus = itemView.findViewById(R.id.linear_layout_status_order_history_screen);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (ApiUtils.currentUser.getType() == 1) { // chi bat su kien click doi voi admin
                onItemClickListener.onClick(v, getAdapterPosition(), false);
            }
        }
    }

    public static String statusOrder(int status) {
        String resultStatus = "";
        switch (status) {
            case 0:
                resultStatus = "Đơn hàng đang được xử lý";
                break;
            case 1:
                resultStatus = "Đơn hàng đã được chấp nhận";
                break;
            case 2:
                resultStatus = "Đơn hàng đã được giao cho đơn vị vận chuyển";
                break;
            case 3:
                resultStatus = "Đơn hàng đã giao thành công";
                break;
            case 4:
                resultStatus = "Đơn hàng đã bị hủy";
                break;
        }
        return resultStatus;
    }
}
