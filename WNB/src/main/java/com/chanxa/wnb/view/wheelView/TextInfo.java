package com.chanxa.wnb.view.wheelView;

import android.graphics.Color;

/**
 * Created by CHANXA on 2014/12/26.
 */
public class TextInfo {
    public TextInfo(int index, String text, boolean isSelected) {
        mIndex = index;
        mText = text;
        mIsSelected = isSelected;

        if (isSelected) {
            mColor = Color.BLUE;
        }
    }

    public int mIndex;
    public String mText;
    public boolean mIsSelected = false;
    public int mColor = Color.BLACK;
}