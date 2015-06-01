package com.chanxa.wnb.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.FenLeiStore;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/12.
 */
public class FenLeiTypeListViewAdapter extends BaseAdapter {
    private Context context;
    private int selectPosition = 0;
    private ArrayList<FenLeiStore> fenLeiStores;

    public FenLeiTypeListViewAdapter(ArrayList<FenLeiStore> fenLeiStores, Context context) {
        this.fenLeiStores = fenLeiStores;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fenLeiStores.size();
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
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_fenlei_list_type, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTvShopTypeName.setText(fenLeiStores.get(position).getName());
        if (selectPosition == position) {
            viewHolder.mTvShopTypeName.setBackgroundColor(Color.TRANSPARENT);
            if (position == getCount()-1) {
                viewHolder.mTvDivider.setVisibility(View.INVISIBLE);
            }
        } else {
            viewHolder.mTvShopTypeName.setBackgroundResource(R.drawable.bg_fenlei_type);
            viewHolder.mTvDivider.setVisibility(View.VISIBLE);
        }
        viewHolder.mTvShopTypeName.setPadding(20, 20, 20, 20);
        convertView.setTag(viewHolder);
        return convertView;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_shop_fenlei_list_type.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.tv_shopTypeName)
        TextView mTvShopTypeName;

        @InjectView(R.id.tv_divider)
        TextView mTvDivider;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
