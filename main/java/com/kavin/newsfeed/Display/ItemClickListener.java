package com.kavin.newsfeed.Display;

import android.view.View;

public interface ItemClickListener {

    void onClick(View view, int position, boolean isLongClick);
}