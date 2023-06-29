package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.Interface.OnItemClickListener;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.activity.ProductDetailActivity;
import com.hoanglinhsama.ecommerce.eventbus.DeleteModifyProductEvent;
import com.hoanglinhsama.ecommerce.model.Product;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class ManageProductAdapter extends RecyclerView.Adapter<ManageProductAdapter.MyViewHolder> {
    private List<Product> listProduct;
    private int layout;
    private Context context;

    public ManageProductAdapter(List<Product> listProduct, int layout, Context context) {
        this.listProduct = listProduct;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public ManageProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, null);
        return new ManageProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageProductAdapter.MyViewHolder holder, int position) {
        Product product = listProduct.get(position);
        Picasso.get().load(product.getPicture()).into(holder.imageViewPictureProduct);
        holder.textViewNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); // tao mau dinh dang nnn.nnn.nnn
        holder.textViewPriceProduct.setText(decimalFormat.format(Double.parseDouble(String.valueOf(product.getPrice()))) + "₫");

        holder.setOnItemClickListener((view, position1, isLongClick) -> {
            if (!isLongClick) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("data", product);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else { // longClick
                EventBus.getDefault().post(new DeleteModifyProductEvent(product));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        private ImageView imageViewPictureProduct;
        private TextView textViewNameProduct;
        private TextView textViewPriceProduct;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPictureProduct = itemView.findViewById(R.id.image_view_picture_new_product);
            this.textViewNameProduct = itemView.findViewById(R.id.text_view_name_new_product);
            this.textViewPriceProduct = itemView.findViewById(R.id.text_view_price_new_product);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 0, getAdapterPosition(), "Sửa"); // tham so: groupId, itemId, vi tri item de context menu gan vao, title
            menu.add(0, 1, getAdapterPosition(), "Xóa");
        }

        @Override
        public boolean onLongClick(View v) {
            onItemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }
}
