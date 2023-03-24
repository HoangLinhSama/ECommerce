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
import com.hoanglinhsama.ecommerce.model.Product;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> listProduct;
    private int layout;
    private Context context;

    public ProductAdapter(List<Product> listProduct, int layout, Context context) {
        this.listProduct = listProduct;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = listProduct.get(position);
        Picasso.get().load(product.getPicture()).into(holder.imageViewPictureProduct);
        holder.textViewNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); // tao mau dinh dang nnn.nnn.nnn
        holder.textViewPriceProduct.setText(decimalFormat.format(Double.parseDouble(product.getPrice())) + "₫");

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPictureProduct;
        private TextView textViewNameProduct;
        private TextView textViewPriceProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPictureProduct = itemView.findViewById(R.id.image_view_picture_product);
            this.textViewNameProduct = itemView.findViewById(R.id.text_view_name_product);
            this.textViewPriceProduct = itemView.findViewById(R.id.text_view_price_product);
        }
    }
}
