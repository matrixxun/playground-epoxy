package com.akarbowy.epoxyexample.ui.helpers;


import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class VerticalGridInnerSpacingDecoration extends ItemDecoration {
    private static final int OUTER_PADDING_DP = 0;
    private static final int INNER_PADDING_DP = 4;
    private int outerPadding = -1;
    private int innerPadding = -1;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        if (outerPadding == -1 || innerPadding == -1) {
            DisplayMetrics m = view.getResources().getDisplayMetrics();
            outerPadding = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, OUTER_PADDING_DP, m);
            innerPadding = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, INNER_PADDING_DP, m);
        }

        int position = parent.getChildAdapterPosition(view);
        final GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        final SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();

        // Zero everything out for the common case
        outRect.setEmpty();

        int spanSize = spanSizeLookup.getSpanSize(position);
        int spanCount = layoutManager.getSpanCount();
        int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);

        int totalItems = parent.getAdapter().getItemCount();
        int rowCount = (int) Math.ceil(totalItems / spanCount);
        int row = position / spanCount;

        if (rowCount > 1) {
            if (row == 0) {
                // first row
                outRect.bottom = innerPadding;
            } else if (row == rowCount - 1) {
                // last row
                outRect.top = innerPadding;
            } else {
                //inner row
                outRect.top = innerPadding;
                outRect.bottom = innerPadding;
            }
        }

        if (spanSize == spanCount) {
            // Only item in row
            outRect.left = outerPadding;
            outRect.right = outerPadding;
        } else if (spanIndex == 0) {
            // First item in row
            outRect.left = outerPadding;
            outRect.right = innerPadding;
        } else if (spanIndex == spanCount - 1) {
            // Last item in row
            outRect.left = innerPadding;
            outRect.right = outerPadding;
        } else {
            // Inner item (not relevant for less than three columns)
            outRect.left = innerPadding;
            outRect.right = innerPadding;
        }
    }
}