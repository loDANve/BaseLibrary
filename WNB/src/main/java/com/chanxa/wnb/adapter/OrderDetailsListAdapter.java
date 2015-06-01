package com.chanxa.wnb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.bean.onLineShop.OrderDetails;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/12.
 */
public class OrderDetailsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Order> orderArrayList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private WBitmap wBitmap;

    public OrderDetailsListAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return orderArrayList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_orderdetails_listview, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Order order = orderArrayList.get(position);

        viewHolder.mTvOrderGoodsNumber.setText("订单号:" + order.getOrderNumber());
        viewHolder.mTvOrderGoodsState.setText(order.getState());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DeviceUtils.dip2px(context, 100), DeviceUtils.dip2px(context, 100));
        layoutParams.setMargins(0, 0, 20, 0);
        viewHolder.mContainerOrderListGoodsOne.removeAllViews();
        if (order.getOrderDetailsArrayList().size() == 1) {
            OrderDetails orderDetails = order.getOrderDetailsArrayList().get(0);
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.ic_launcher);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String path = orderDetails.getImgPath();
            if (!StringUtils.isEmpty(path)) {
                if (path.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(imageView, path);
                } else {
                    wBitmap.display(imageView, AppConfig.PATH_PREFIX + path);
                }
            }
            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            TextView textViewnName = new TextView(context);
            textViewnName.setText(orderDetails.getName());
            textViewnName.setMaxLines(2);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
            textViewnName.setLayoutParams(layoutParams2);
            TextView textViewNumber = new TextView(context);
            textViewNumber.setText("x" + orderDetails.getNumber());
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textViewNumber.setLayoutParams(layoutParams1);
            relativeLayout.addView(textViewnName);
            relativeLayout.addView(textViewNumber);
            viewHolder.mContainerOrderListGoodsOne.addView(imageView);
            viewHolder.mContainerOrderListGoodsOne.addView(relativeLayout);
        } else {
            for (int i = 0; i < order.getOrderDetailsArrayList().size(); i++) {
                if (i > 2) {
                    break;
                }
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.drawable.ic_launcher);
                String path = order.getOrderDetailsArrayList().get(i).getImgPath();
                if (!StringUtils.isEmpty(path)) {
                    if (path.startsWith(AppConfig.PATH_PREFIX)) {
                        wBitmap.display(imageView, path);
                    } else {
                        wBitmap.display(imageView, AppConfig.PATH_PREFIX + path);
                    }
                }
                viewHolder.mContainerOrderListGoodsOne.addView(imageView);
            }
        }
        viewHolder.mTvOrderListMoney.setText("实付款:¥" + order.getConsumptionMoney());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, v, position, getItemId(position));
            }
        });
        convertView.setTag(viewHolder);
        return convertView;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_orderdetails_listview.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.tv_orderGoodsState)
        TextView mTvOrderGoodsState;
        @InjectView(R.id.tv_orderGoodsNumber)
        TextView mTvOrderGoodsNumber;
        @InjectView(R.id.container_orderListGoodsOne)
        LinearLayout mContainerOrderListGoodsOne;
        @InjectView(R.id.tv_OrderListMoney)
        TextView mTvOrderListMoney;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
