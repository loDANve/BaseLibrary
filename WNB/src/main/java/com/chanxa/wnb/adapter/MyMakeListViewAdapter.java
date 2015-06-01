package com.chanxa.wnb.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.MakeRecord;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2014/12/13.
 */
public class MyMakeListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MakeRecord> makeRecordArrayList;

    public MyMakeListViewAdapter(Context mContext, ArrayList<MakeRecord> makeRecordArrayList) {
        this.mContext = mContext;
        this.makeRecordArrayList = makeRecordArrayList;
    }

    @Override
    public int getCount() {
        return makeRecordArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_mymake, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MakeRecord makeRecord = makeRecordArrayList.get(position);
        viewHolder.mTvItemMymakeDate.setText(makeRecord.getMakeDate());
        viewHolder.mTvItemMymakeTime.setText(makeRecord.getMakeTime());

        String state = "";
        Drawable nav_up=mContext.getResources().getDrawable(R.drawable.icon_yes);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        switch (makeRecord.getState()) {
            case "0":
                state = "未处理";
                nav_up = mContext.getResources().getDrawable(R.drawable.icon_no);
                break;
            case "1":
                state = "已处理";
                break;
            case "2":
                state = "已取消";
                nav_up = mContext.getResources().getDrawable(R.drawable.icon_no);
                break;
            case "3":
                state = "已结算";
                break;
        }

        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        viewHolder.mTvItemMymakeState.setCompoundDrawables(null, null, nav_up, null);
        viewHolder.mTvItemMymakeState.setText(state);


        viewHolder.mTvItemMymakeStoreName.setText(makeRecord.getStoreMark());
        viewHolder.mTvItemMymakePersonName.setText(makeRecord.getGoodsName());
        convertView.setTag(viewHolder);
        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.tv_item_mymakeDate)
        TextView mTvItemMymakeDate;
        @InjectView(R.id.tv_item_mymakeTime)
        TextView mTvItemMymakeTime;
        @InjectView(R.id.tv_item_mymakeState)
        TextView mTvItemMymakeState;
        @InjectView(R.id.tv_item_mymakeStoreName)
        TextView mTvItemMymakeStoreName;
        @InjectView(R.id.tv_item_mymakePersonName)
        TextView mTvItemMymakePersonName;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
