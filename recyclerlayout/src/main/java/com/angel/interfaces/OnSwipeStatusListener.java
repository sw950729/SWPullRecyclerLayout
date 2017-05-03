package com.angel.interfaces;

import com.angel.layout.SWSlipeLayout;

public interface OnSwipeStatusListener {
    void onStatusChanged(SWSlipeLayout layout, SWSlipeLayout.Status status);
}