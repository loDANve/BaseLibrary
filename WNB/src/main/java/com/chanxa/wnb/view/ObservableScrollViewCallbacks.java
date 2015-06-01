package com.chanxa.wnb.view;

public interface ObservableScrollViewCallbacks {
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    public void onDownMotionEvent();

    public void onUpOrCancelMotionEvent(ScrollState scrollState);
}
