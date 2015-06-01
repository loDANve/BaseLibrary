package com.chanxa.wnb.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.StoredValueRecord;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2014/12/13.
 */
public class LookRecordAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<StoredValueRecord> data;

    public LookRecordAdapter(Context mContext, ArrayList<StoredValueRecord> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_look_record, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StoredValueRecord storedValueRecord = data.get(position);
        String time = storedValueRecord.getTime().trim();
        LogUtils.e(time+"=>size:"+time.length());
        if (!StringUtils.isEmpty(time)) {
            viewHolder.mTvLookRecordDay.setText(time.substring(0, time.lastIndexOf(" ")).trim());
            viewHolder.mTvLookRecordTime.setText(time.substring(time.lastIndexOf(" ")+1,time.length() ).trim());
        }
        viewHolder.mTvLookRecordType.setText(storedValueRecord.getRaMark().trim());

        String changeType = storedValueRecord.getChangeType();
        if (changeType.equals("增加")) {
            viewHolder.mTvLookRecordMoney.setTextColor(0xff4fa502);
            viewHolder.mTvLookRecordMoney.setText("+" + storedValueRecord.getChangeNumber().trim());
        } else {
            viewHolder.mTvLookRecordMoney.setTextColor(0xffd00303);
            viewHolder.mTvLookRecordMoney.setText("-" + storedValueRecord.getChangeNumber().trim());
        }
        convertView.setTag(viewHolder);
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_list_look_record.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.tv_lookRecordDay)
        TextView mTvLookRecordDay;
        @InjectView(R.id.tv_lookRecordTime)
        TextView mTvLookRecordTime;
        @InjectView(R.id.tv_lookRecordType)
        TextView mTvLookRecordType;
        @InjectView(R.id.tv_lookRecordMoney)
        TextView mTvLookRecordMoney;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
