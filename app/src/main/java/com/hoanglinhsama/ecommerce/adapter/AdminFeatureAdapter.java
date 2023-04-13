package com.hoanglinhsama.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.model.AdminFeature;

import java.util.List;

public class AdminFeatureAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<AdminFeature> listAdminFeature;

    public AdminFeatureAdapter(Context context, int layout, List<AdminFeature> listAdminFeature) {
        this.context = context;
        this.layout = layout;
        this.listAdminFeature = listAdminFeature;
    }

    @Override
    public int getCount() {
        return listAdminFeature.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        private ImageView ivPictureTypeProduct;
        private TextView tvNameTypeProduct;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.ivPictureTypeProduct = convertView.findViewById(R.id.image_view_type_product);
            viewHolder.tvNameTypeProduct = convertView.findViewById(R.id.text_view_type_product);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AdminFeature adminFeature = listAdminFeature.get(position);
        viewHolder.tvNameTypeProduct.setText(adminFeature.getName());
        viewHolder.ivPictureTypeProduct.setImageResource(adminFeature.getPicture());
        return convertView;
    }
}
