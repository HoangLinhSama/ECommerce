package com.hoanglinhsama.ecommerce.Interface;

import android.view.View;

/**
 * functional interface dinh nghia event cho thanh phan con trong item cua RecyclerView
 */

public interface OnImageViewClickListener {
    void onClick(View view, int position, int value);
}
