package com.chanxa.wnb.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.staff.StaffOrderDetailActivity;
import com.chanxa.wnb.activity.staff.StaffPriceAdjustmentActivity;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.StaffOrderDetail;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2014/12/12.
 */
public class Staff_ManageOrderListViewAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Order> staffOrderList;
    private WBitmap wBitmap;

    public Staff_ManageOrderListViewAdapter(Context context, ArrayList<Order> staffOrderList) {
        this.context = context;
        this.staffOrderList = staffOrderList;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return staffOrderList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_staff_manage_order, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Order order = staffOrderList.get(position);

        if (!StringUtils.isEmpty(order.getOrderNumber())) {
            viewHolder.mTvItemOrderNum.setText("订单号:" + order.getOrderNumber());
        }
        if (!StringUtils.isEmpty(order.getState())) {
            viewHolder.mTvItemOrderIsAuditing.setText(order.getState());
        }
        if (order.getOrderDetailsArrayList().size() != 0) {
            viewHolder.mTvItemOrderGoodsAmount.setText("共" + order.getOrderDetailsArrayList().size() + "件");
        }
        if (!StringUtils.isEmpty(order.getConsumptionMoney())) {
            viewHolder.mTvItemOrderSumPrice.setText("总计:￥" + order.getConsumptionMoney());
        }

        if (order.getOrderDetailsArrayList().size() != 0) {
            int goodsAmount = order.getOrderDetailsArrayList().size();
            viewHolder.mTvItemOrdergoodsTitle.setText(order.getOrderDetailsArrayList().get(0).getName());
            for (int i = 0; i < goodsAmount; i++){
                if (!StringUtils.isEmpty(order.getOrderDetailsArrayList().get(i).getImgPath())) {
                    WnbApplication.getInstance().getwBitmap().display(viewHolder.imageList.get(i), AppConfig.PATH_PREFIX+order.getOrderDetailsArrayList().get(i).getImgPath());
                }
            }
            if (goodsAmount == 1) {
                viewHolder.mImgItemOrderPic1.setVisibility(View.VISIBLE);
                viewHolder.mTvItemOrdergoodsTitle.setVisibility(View.VISIBLE);
            } else if (goodsAmount == 2) {
                viewHolder.mImgItemOrderPic1.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic2.setVisibility(View.VISIBLE);
            } else if (goodsAmount == 3) {
                viewHolder.mImgItemOrderPic1.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic2.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic3.setVisibility(View.VISIBLE);
            } else if (goodsAmount == 4) {
                viewHolder.mImgItemOrderPic1.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic2.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic3.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic4.setVisibility(View.VISIBLE);
            } else if(goodsAmount >= 5){
                viewHolder.mImgItemOrderPic1.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic2.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic3.setVisibility(View.VISIBLE);
                viewHolder.mImgItemOrderPic4.setVisibility(View.VISIBLE);
                viewHolder.mTvItemOrderDot.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.mLayoutItemChangePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StaffPriceAdjustmentActivity.class);
                context.startActivity(intent);
            }
        });

        View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("staffOrderDetail", order);
                    Intent intent = new Intent(context, StaffOrderDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            };

        convertView.setOnClickListener(onClickListener);
        convertView.setTag(viewHolder);
        return convertView;
    }

    public void refreshData(ArrayList<Order> staffOrderList) {
        staffOrderList.clear();
        staffOrderList.addAll(staffOrderList);
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_liststore.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.staff_orderList_orderNum)
        TextView mTvItemOrderNum;
        @InjectView(R.id.staff_orderList_isAuditing)
        TextView mTvItemOrderIsAuditing;
        @InjectView(R.id.staff_orderList_goodsAmount)
        TextView mTvItemOrderGoodsAmount;
        @InjectView(R.id.staff_orderList_sumPrice)
        TextView mTvItemOrderSumPrice;
        @InjectView(R.id.staff_orderList_Pic1)
        ImageView mImgItemOrderPic1;
        @InjectView(R.id.staff_orderList_Pic2)
        ImageView mImgItemOrderPic2;
        @InjectView(R.id.staff_orderList_Pic3)
        ImageView mImgItemOrderPic3;
        @InjectView(R.id.staff_orderList_Pic4)
        ImageView mImgItemOrderPic4;
        @InjectView(R.id.staff_orderList_goodsTitle)
        TextView mTvItemOrdergoodsTitle;
        @InjectView(R.id.staff_orderList_dot)
        TextView mTvItemOrderDot;
        @InjectView(R.id.container_staff_changePrice)
        LinearLayout mLayoutItemChangePrice;

        ArrayList<ImageView> imageList = new ArrayList<ImageView>();

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
            imageList.add(mImgItemOrderPic1);
            imageList.add(mImgItemOrderPic2);
            imageList.add(mImgItemOrderPic3);
            imageList.add(mImgItemOrderPic4);
        }
    }
}