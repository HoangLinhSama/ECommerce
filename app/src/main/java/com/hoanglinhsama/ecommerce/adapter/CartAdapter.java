package com.hoanglinhsama.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.Interface.OnImageViewClickListener;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.eventbus.DisplayCartEvent;
import com.hoanglinhsama.ecommerce.eventbus.TotalMoneyEvent;
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
        Cart cart = ApiUtils.listCart.get(position);
        Picasso.get().load(cart.getPictureProduct()).into(holder.imageViewPictureCart);
        holder.textViewNameCart.setText(cart.getNameProduct());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPriceCart.setText(decimalFormat.format(Double.parseDouble(String.valueOf(cart.getTotalPrice() / cart.getQuantity()))) + "₫");
        holder.textViewQuantityCart.setText(String.valueOf(cart.getQuantity()));
        if (ApiUtils.listCart.get(position).getQuantity() < ApiUtils.listCart.get(position).getQuantityRemain()) {
            holder.imageViewIncreaseCart.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewIncreaseCart.setVisibility(View.INVISIBLE);
        }
        holder.checkBoxChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ApiUtils.listCartChecked.add(cart);
                EventBus.getDefault().post(new TotalMoneyEvent()); // tinh lai tong tien khi them san pham duoc check
            } else { // bo check thi xoa ra khoi list
                for (int i = 0; i < ApiUtils.listCartChecked.size(); i++) {
                    if (cart.getIdProduct() == ApiUtils.listCartChecked.get(i).getIdProduct()) {
                        ApiUtils.listCartChecked.remove(i);
                        EventBus.getDefault().post(new TotalMoneyEvent());
                    }
                }
            }
        });

        /* Bat su kien thi click vao image button tang hoac giam so luong hoac button xoa */
        holder.setOnImageViewClickListener((view, position1, value) -> { // view o day la imageViewButton tang hoac giam
            if (value == 1) // giam
            {
                if (ApiUtils.listCart.get(position1).getQuantity() > 1) {
                    int quantity = ApiUtils.listCart.get(position1).getQuantity() - 1;
                    updateProductToCart(quantity, ApiUtils.listCart.get(position1).getIdProduct()); // cap nhat lai len server
                } else { // neu so luong con 1 ma giam nua thi se xoa san pham ra khoi gio hang
                    delete(view, position1, holder);
                }
            } else {
                if (value == 2) { // tang
                    int quantity = ApiUtils.listCart.get(position1).getQuantity() + 1;
                    updateProductToCart(quantity, ApiUtils.listCart.get(position1).getIdProduct()); // cap nhat lai len server
                } else { // button xoa
                    delete(view, position1, holder);
                }
            }
        });
    }

    private void delete(View view, int position, MyViewHolder holder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
        alertDialog.setTitle("Thông báo xóa sản phẩm");
        alertDialog.setMessage("Xóa sản phẩm khỏi giỏ hàng ?");
        alertDialog.setPositiveButton("Đồng ý", (dialog, which) -> {
            deleteProductToCart(ApiUtils.currentUser.getId(), ApiUtils.listCart.get(position).getIdProduct());
            holder.checkBoxChoose.setChecked(false);
        });
        alertDialog.setNegativeButton("Huỷ", (dialog, which) -> dialog.cancel());
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return ApiUtils.listCart.size();
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

                    /* Dong thoi cung xoa san pham bi xoa ra khoi listCartChecked */
                    for (int i = 0; i < ApiUtils.listCartChecked.size(); i++) {
                        if (ApiUtils.listCartChecked.get(i).getIdProduct() == productId) {
                            ApiUtils.listCartChecked.remove(i);
                        }
                    }
                    getCartDetail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("deleteProductToCart", t.getMessage());
            }
        });
    }

    /**
     * Cap nhat lai thong tin loai san pham da ton tai trong gio hang
     */
    private void updateProductToCart(int quantity, int id) {
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.updateCartDetail(ApiUtils.currentUser.getId(), id, quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    /* Cap nhat du lieu so luong va tong tien cua loai san pham cho listCartChecked */
                    for (int i = 0; i < ApiUtils.listCartChecked.size(); i++) {
                        if (id == ApiUtils.listCartChecked.get(i).getIdProduct()) {
                            ApiUtils.listCartChecked.get(i).setTotalPrice(quantity * (ApiUtils.listCartChecked.get(i).getTotalPrice() / ApiUtils.listCartChecked.get(i).getQuantity())); // cap nhat lai totalPrice cho loai san pham trong listCartChecked
                            ApiUtils.listCartChecked.get(i).setQuantity(quantity); // dong thoi cap nhat lai so luong cho loai san pham trong listCartChecked
                        }
                    }

                    getCartDetail(); // cap nhat du lieu tu server ve cho listCart
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("updateProductToCart", t.getMessage());
            }
        });
    }

    /**
     * Lay du lieu gio hang cua nguoi dung tu server
     */
    private void getCartDetail() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<Cart>> call = dataClient.getCartDetail(ApiUtils.currentUser.getId());
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    ApiUtils.listCart = response.body();
                    EventBus.getDefault().post(new TotalMoneyEvent());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.d("getCartDetail", t.getMessage());
                if (t.getMessage().equals("Expected BEGIN_ARRAY but was STRING at line 1 column 1 path $") || t.getMessage().equals("End of input at line 1 column 1 path $")) { // loi xay ra khi khong get duoc data tu table cart_detail (khi xoa tat ca san pham ra khoi gio hang), cach xu ly nay khong tot
                    ApiUtils.listCart.clear();
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new DisplayCartEvent());// Post event den eventbus de hien thi recyclerview cart khi gio hang trong
                }
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
        private CheckBox checkBoxChoose;
        private Button buttonDelete;
        private OnImageViewClickListener onImageViewClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPictureCart = itemView.findViewById(R.id.image_view_picture_cart);
            this.textViewNameCart = itemView.findViewById(R.id.text_view_name_cart);
            this.textViewPriceCart = itemView.findViewById(R.id.text_view_price_cart);
            this.textViewQuantityCart = itemView.findViewById(R.id.text_view_quantity_cart);
            this.imageViewIncreaseCart = itemView.findViewById(R.id.image_view_increase_cart);
            this.imageViewDecreaseCart = itemView.findViewById(R.id.image_view_decrease_cart);
            this.checkBoxChoose = itemView.findViewById(R.id.checkbox_choose);
            this.buttonDelete = itemView.findViewById(R.id.button_delete);

            /* event click */
            this.imageViewIncreaseCart.setOnClickListener(this);
            this.imageViewDecreaseCart.setOnClickListener(this);
            this.buttonDelete.setOnClickListener(this);
        }

        public void setOnImageViewClickListener(OnImageViewClickListener onImageViewClickListener) {
            this.onImageViewClickListener = onImageViewClickListener;
        }

        @Override
        public void onClick(View v) {
            if (v == this.imageViewDecreaseCart) {
                this.onImageViewClickListener.onClick(v, getAdapterPosition(), 1); // 1 la giam
            }
            if (v == this.imageViewIncreaseCart) {
                this.onImageViewClickListener.onClick(v, getAdapterPosition(), 2); // 2 la tang
            }
            if (v == this.buttonDelete) {
                this.onImageViewClickListener.onClick(v, getAdapterPosition(), 3); // 3 la button delete
            }
        }
    }
}
