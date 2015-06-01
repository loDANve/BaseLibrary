package com.chanxa.wnb.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MakeDetailsActivity;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2014/12/12.
 */
public class StoreListViewAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Store> storeArrayList;
    private WBitmap wBitmap;

    public StoreListViewAdapter(Context context, ArrayList<Store> storeArrayList) {
        this.context = context;
        this.storeArrayList = storeArrayList;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return storeArrayList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_liststore, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Store store = storeArrayList.get(position);

        viewHolder.mTvItemStorelistName.setText(store.getName());
        if (!StringUtils.isEmpty(store.getDistance())) {
            if (Double.parseDouble(store.getDistance()) * 1000 > 10000) {
                viewHolder.mTvItemStorelistDistance.setText(">10000" + "m");
            } else {
                viewHolder.mTvItemStorelistDistance.setText(Double.parseDouble(store.getDistance()) * 1000 + "m");
            }
            viewHolder.icon_location.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mTvItemStorelistDistance.setText("");
            viewHolder.icon_location.setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.isEmpty(store.getImgPath())) {
            WnbApplication.getInstance().getwBitmap().display(viewHolder.mImgItemStorelist, AppConfig.PATH_PREFIX+store.getImgPath());
        }
        View.OnClickListener onClickListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("store", store);
                        Intent intent = new Intent(context, MakeDetailsActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                };

        viewHolder.mBtnItemStorelistSub.setOnClickListener(onClickListener);
        viewHolder.mTvItemStorelistIntroduction.setText(store.getReMark());
        convertView.setOnClickListener(onClickListener);
        wBitmap.display(viewHolder.mImgItemStorelist, AppConfig.PATH_PREFIX + store.getImgPath());
        convertView.setTag(viewHolder);
        return convertView;
    }

    public void refreshData(ArrayList<Store> t_storeArrayList) {
        storeArrayList.clear();
        storeArrayList.addAll(t_storeArrayList);
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_liststore.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.img_item_storelist)
        ImageView mImgItemStorelist;
        @InjectView(R.id.tv_item_storelist_name)
        TextView mTvItemStorelistName;
        @InjectView(R.id.tv_item_storelist_Introduction)
        TextView mTvItemStorelistIntroduction;
        @InjectView(R.id.btn_item_storelist_sub)
        Button mBtnItemStorelistSub;
        @InjectView(R.id.tv_item_storelist_distance)
        TextView mTvItemStorelistDistance;
        @InjectView(R.id.icon_location)
        ImageView icon_location;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}