package com.hoanglinhsama.ecommerce;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Class de chinh lai khoang cach giua cac item trong RecyclerView
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceSize;

    public ItemDecoration(int spaceSize) {
        this.spaceSize = spaceSize;
    }

    /**
     * Dat offset 4 chieu cua cac item
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = spaceSize;
        outRect.bottom = spaceSize;
        outRect.left = spaceSize;
        outRect.right = spaceSize;
    }
}
