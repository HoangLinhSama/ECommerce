package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { // extends RecyclerView.ViewHolder de co the su dung duoc nhieu loai ViewHolder o day
    private Context context;
    private List<Product> listProduct;
    private static final int VIEW_TYPE_DATA = 0; // neu item co du lieu
    private static final int VIEW_TYPE_LOADING = 1; // neu item null

    public ProductDetailAdapter(Context context, List<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = null;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == VIEW_TYPE_DATA) {
            convertView = layoutInflater.inflate(R.layout.item_product_detail, null);
            return new MyViewHolder(convertView);
        } else {
            convertView = layoutInflater.inflate(R.layout.item_loading, null);
            return new LoadingViewHolder(convertView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Product product = listProduct.get(position);
            Picasso.get().load(product.getPicture()).into(myViewHolder.imageViewPictureProduct);
            myViewHolder.textViewNameProduct.setText(product.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.textViewPriceProduct.setText(decimalFormat.format(Double.parseDouble(product.getPrice())) + "₫");
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listProduct.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    /**
     * ViewHolder dung cho item null (VIEW_TYPE_LOADING)
     */
    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.progressBar = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }

    /**
     * ViewHolder dung cho item co data (VIEW_TYPE_DATA)
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPictureProduct;
        private TextView textViewNameProduct;
        private TextView textViewPriceProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPictureProduct = itemView.findViewById(R.id.image_view_picture_product_detail);
            this.textViewNameProduct = itemView.findViewById(R.id.text_view_name_product_detail);
            this.textViewPriceProduct = itemView.findViewById(R.id.text_view_price_product_detail);
        }
    }
}
