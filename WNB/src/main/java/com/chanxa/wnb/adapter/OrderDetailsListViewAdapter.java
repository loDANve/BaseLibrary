package com.chanxa.wnb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.onLineShop.OrderDetails;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/15.
 */
public class OrderDetailsListViewAdapter extends BaseAdapter {
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private Context context;
    private WBitmap wBitmap;

    public OrderDetailsListViewAdapter(ArrayList<OrderDetails> orderDetailsArrayList, Context context) {
        this.orderDetailsArrayList = orderDetailsArrayList;
        this.context = context;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return orderDetailsArrayList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_order_details, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderDetails orderDetails = orderDetailsArrayList.get(position);
        viewHolder.mTvOrderDetailsGoodsName.setText(orderDetails.getName());
        viewHolder.mTvItemGoodsNumber.setText("x"+orderDetails.getNumber());
        String path = orderDetails.getImgPath();
        if (!StringUtils.isEmpty(path)) {
            if (path.startsWith(AppConfig.PATH_PREFIX)) {
                wBitmap.display(viewHolder.mImgOrderGoods, path);
            } else {
                wBitmap.display(viewHolder.mImgOrderGoods, AppConfig.PATH_PREFIX + path);
            }
        }
        convertView.setTag(viewHolder);
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_list_order_details.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.img_orderGoods)
        ImageView mImgOrderGoods;
        @InjectView(R.id.tv_orderDetails_GoodsName)
        TextView mTvOrderDetailsGoodsName;
        @InjectView(R.id.tv_item_goodsNumber)
        TextView mTvItemGoodsNumber;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
