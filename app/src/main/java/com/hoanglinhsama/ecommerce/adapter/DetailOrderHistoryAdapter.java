package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.model.ProductOrder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class DetailOrderHistoryAdapter extends RecyclerView.Adapter<DetailOrderHistoryAdapter.MyViewHolder> {
    private List<ProductOrder> listProductOrder;
    private int layout;
    private Context context;

    public DetailOrderHistoryAdapter(List<ProductOrder> listProductOrder, int layout, Context context) {
        this.listProductOrder = listProductOrder;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailOrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, null);
        return new DetailOrderHistoryAdapter.MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailOrderHistoryAdapter.MyViewHolder holder, int position) {
        ProductOrder productOrder = listProductOrder.get(position);
        holder.textViewNameDetailOrderHistory.setText(productOrder.getNameProduct());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPriceDetailOrderHistory.setText(decimalFormat.format(Double.parseDouble(String.valueOf(productOrder.getPrice()))) + "₫");
        Picasso.get().load(productOrder.getPictureProduct()).into(holder.imageViewDetailOrderHistory);
        holder.textViewQuantityDetailOrderHistory.setText(String.valueOf(productOrder.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return listProductOrder.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameDetailOrderHistory;
        private TextView textViewPriceDetailOrderHistory;
        private ImageView imageViewDetailOrderHistory;
        private TextView textViewQuantityDetailOrderHistory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewDetailOrderHistory = itemView.findViewById(R.id.image_view_detail_order_history_screen);
            this.textViewNameDetailOrderHistory = itemView.findViewById(R.id.text_view_name_detail_order_history_screen);
            this.textViewPriceDetailOrderHistory = itemView.findViewById(R.id.text_view_price_detail_order_history_screen);
            this.textViewQuantityDetailOrderHistory = itemView.findViewById(R.id.text_view_quantity_detail_order_history);
        }
    }
}
