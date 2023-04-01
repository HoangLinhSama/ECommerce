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
import com.hoanglinhsama.ecommerce.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private int layout;
    private List<Cart> listCart;

    public CartAdapter(Context context, int layout, List<Cart> listCart) {
        this.context = context;
        this.layout = layout;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, null);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        Picasso.get().load(cart.getPictureProduct()).into(holder.imageViewPictureCart);
        holder.textViewNameCart.setText(cart.getNameProduct());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPriceCart.setText(decimalFormat.format(Double.parseDouble(String.valueOf(cart.getTotalPrice() / cart.getQuantity()))) + "₫");
        holder.textViewQuantityCart.setText(String.valueOf(cart.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPictureCart;
        private TextView textViewNameCart;
        private TextView textViewPriceCart;
        private TextView textViewQuantityCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPictureCart = itemView.findViewById(R.id.image_view_picture_cart);
            this.textViewNameCart = itemView.findViewById(R.id.text_view_name_cart);
            this.textViewPriceCart = itemView.findViewById(R.id.text_view_price_cart);
            this.textViewQuantityCart = itemView.findViewById(R.id.text_view_quantity_cart);
        }
    }
}
