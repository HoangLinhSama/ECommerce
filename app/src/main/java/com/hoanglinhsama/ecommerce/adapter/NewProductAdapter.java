package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.hoanglinhsama.ecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {
    private List<Product> listNewProduct;
    private int layout;
    private Context context;

    public NewProductAdapter(List<Product> listProduct, int layout, Context context) {
        this.listNewProduct = listProduct;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public NewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewProductAdapter.ViewHolder holder, int position) {
        Product product = listNewProduct.get(position);
        Picasso.get().load(product.getPicture()).into(holder.imageViewPictureProduct);
        holder.textViewNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); // tao mau dinh dang nnn.nnn.nnn
        holder.textViewPriceProduct.setText(decimalFormat.format(Double.parseDouble(product.getPrice())) + "₫");

        holder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNewProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageViewPictureProduct;
        private TextView textViewNameProduct;
        private TextView textViewPriceProduct;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPictureProduct = itemView.findViewById(R.id.image_view_picture_new_product);
            this.textViewNameProduct = itemView.findViewById(R.id.text_view_name_new_product);
            this.textViewPriceProduct = itemView.findViewById(R.id.text_view_price_new_product);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
