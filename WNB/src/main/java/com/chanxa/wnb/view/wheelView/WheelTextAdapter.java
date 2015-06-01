package com.chanxa.wnb.view.wheelView;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/26.
 */
public class WheelTextAdapter extends BaseAdapter {
    ArrayList<TextInfo> mData = null;
    int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
    int mHeight = 50;
    Context mContext = null;

    public WheelTextAdapter(Context context) {
        mContext = context;
        mHeight = (int) Utils.pixelToDp(context, mHeight);
    }

    public void setData(ArrayList<TextInfo> data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    public void setItemSize(int width, int height) {
        mWidth = width;
        mHeight = (int) Utils.pixelToDp(mContext, height);
    }

    @Override
    public int getCount() {
        return (null != mData) ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;

        if (null == convertView) {
            convertView = new TextView(mContext);
            convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
            textView = (TextView) convertView;
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            textView.setTextColor(Color.BLACK);
        }

        if (null == textView) {
            textView = (TextView) convertView;
        }
        if (position<=mData.size()) {
            TextInfo info = mData.get(position);
            textView.setText(info.mText);
            textView.setTextColor(info.mColor);
        }
        return convertView;
    }
}
