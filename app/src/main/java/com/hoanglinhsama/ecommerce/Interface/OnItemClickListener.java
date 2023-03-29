package com.hoanglinhsama.ecommerce.Interface;

import android.view.View;

/**
 * functional interface dinh nghia su kien click va long click tren recycler view
 */
@FunctionalInterface
public interface OnItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
