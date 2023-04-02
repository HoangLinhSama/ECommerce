package com.hoanglinhsama.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.Interface.OnImageViewClickListener;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.activity.MainActivity;
import com.hoanglinhsama.ecommerce.eventbus.NotifyDataEvent;
import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private int layout;
    private MyViewHolder myViewHolder;
    private int myPosition;
    private int size; // bien de theo doi kich thuoc cua list tai cac thoi diem

    public CartAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, null);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        myPosition = position;
        myViewHolder = holder;
        size = MainActivity.listCart.size();
        Cart cart = MainActivity.listCart.get(position);
        Picasso.get().load(cart.getPictureProduct()).into(holder.imageViewPictureCart);
        holder.textViewNameCart.setText(cart.getNameProduct());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPriceCart.setText(decimalFormat.format(Double.parseDouble(String.valueOf(cart.getTotalPrice() / cart.getQuantity()))) + "₫");
        holder.textViewQuantityCart.setText(String.valueOf(cart.getQuantity()));
        holder.setOnImageViewClickListener(new OnImageViewClickListener() { // bat su kien thi click vao image button tang hoac giam so luong
            @Override
            public void onClick(View view, int position, int value) { // view o day la imageViewButton tang hoac giam
                if (value == 1) // giam
                {
                    if (MainActivity.listCart.get(position).getQuantity() > 1) { // so luong toi thieu phai la 1
                        int quantity = MainActivity.listCart.get(position).getQuantity() - 1;
                        updateProductToCart(quantity, MainActivity.listCart.get(position).getIdProduct()); // cap nhat lai len server
                    } else { // neu so luong con 1 ma giam nua thi se xoa san pham ra khoi gio hang
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
                        alertDialog.setTitle("Thông báo xóa sản phẩm");
                        alertDialog.setMessage("Xóa sản phẩm khỏi giỏ hàng ?");
                        alertDialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProductToCart(MainActivity.userId, MainActivity.listCart.get(position).getIdProduct());
                            }
                        });
                        alertDialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                } else { // tang
                    if (MainActivity.listCart.get(position).getQuantity() < MainActivity.listCart.get(position).getQuantityRemain()) { // so luong toi da la so luong con lai cua san pham
                        int quantity = MainActivity.listCart.get(position).getQuantity() + 1;
                        updateProductToCart(quantity, MainActivity.listCart.get(position).getIdProduct()); // cap nhat lai len server
                    } else {
                        Toast.makeText(context, "Số lượng sản phẩm còn lại không đủ để thêm vào giỏ hàng !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.listCart.size();
    }

    /**
     * Xoa san pham ra khoi gio hang
     */
    private void deleteProductToCart(int userId, int productId) {
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.deleteCartDetail(userId, productId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Xóa sản phẩm thành công ! ", Toast.LENGTH_SHORT).show();
                    getCartDetail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Cap nhat lai thong tin loai san pham da ton tai trong gio hang
     */
    private void updateProductToCart(int quantity, int id) {
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.updateCartDetail(MainActivity.userId, id, quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    getCartDetail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Lay du lieu gio hang cua nguoi dung tu server
     */
    private void getCartDetail() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<Cart>> call = dataClient.getCartDetail(MainActivity.userId);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    MainActivity.listCart = response.body();
                    EventBus.getDefault().post(new NotifyDataEvent()); // post event den eventbus
                    if (MainActivity.listCart.size() == size) { // size list khong thay doi thi chi la tang giam so luong loai san pham trong gio hang
                        Cart cart = MainActivity.listCart.get(myPosition);
                        myViewHolder.textViewQuantityCart.setText(String.valueOf(cart.getQuantity())); // cap nhat lai so luong cua loai san pham trong gio hang
                    } else { // nguoc lai kich thuoc thay doi thi la da xoa san pham
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.d("getCartDetail", t.getMessage());
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageViewPictureCart;
        private TextView textViewNameCart;
        private TextView textViewPriceCart;
        private TextView textViewQuantityCart;
        private ImageView imageViewDecreaseCart;
        private ImageView imageViewIncreaseCart;
        private OnImageViewClickListener onImageViewClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPictureCart = itemView.findViewById(R.id.image_view_picture_cart);
            this.textViewNameCart = itemView.findViewById(R.id.text_view_name_cart);
            this.textViewPriceCart = itemView.findViewById(R.id.text_view_price_cart);
            this.textViewQuantityCart = itemView.findViewById(R.id.text_view_quantity_cart);
            this.imageViewIncreaseCart = itemView.findViewById(R.id.image_view_increase_cart);
            this.imageViewDecreaseCart = itemView.findViewById(R.id.image_view_decrease_cart);

            /* event click */
            this.imageViewIncreaseCart.setOnClickListener(this);
            this.imageViewDecreaseCart.setOnClickListener(this);
        }

        public void setOnImageViewClickListener(OnImageViewClickListener onImageViewClickListener) {
            this.onImageViewClickListener = onImageViewClickListener;
        }

        @Override
        public void onClick(View v) {
            if (v == this.imageViewDecreaseCart) {
                this.onImageViewClickListener.onClick(v, getAdapterPosition(), 1); // 1 la giam, 2 la tang
            }
            if (v == this.imageViewIncreaseCart) {
                this.onImageViewClickListener.onClick(v, getAdapterPosition(), 2);
            }
        }
    }
}
