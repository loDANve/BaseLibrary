package com.chanxa.wnb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.onLineShop.OrderDetails;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/2/26.
 */
public class Staff_OrderDetailListViewAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<OrderDetails> staffOrderDetailGoodsList;
    private WBitmap wBitmap;

    public Staff_OrderDetailListViewAdapter(Context context, ArrayList<OrderDetails> staffOrderDetailGoodsList) {
        this.context = context;
        this.staffOrderDetailGoodsList = staffOrderDetailGoodsList;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return staffOrderDetailGoodsList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_staff_order_detail, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final OrderDetails goods = staffOrderDetailGoodsList.get(position);

        if (!StringUtils.isEmpty(goods.getName())) {
            viewHolder.mTvItemGoodsName.setText(goods.getName());
        }
        /*if (!StringUtils.isEmpty(goods.get) && !StringUtils.isEmpty(goods.getMoney())) {
            viewHolder.mTvItemGoodsPrice.setText(""+goods.getNumber() * Integer.parseInt(goods.getMoney()));
        }*/
        if (!StringUtils.isEmpty(goods.getNumber())) {
            viewHolder.mTvItemGoodsNumber.setText("x" + goods.getNumber());
        }

        if (!StringUtils.isEmpty(goods.getImgPath())) {
            WnbApplication.getInstance().getwBitmap().display(viewHolder.mImgItemGoodsImg, AppConfig.PATH_PREFIX+goods.getImgPath());
        }

        /*View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("staffOrderDetail", staffOrderDetail);
                    Intent intent = new Intent(context, StaffOrderDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
        };

        convertView.setOnClickListener(onClickListener);*/
        convertView.setTag(viewHolder);
        return convertView;
    }

    public void refreshData(ArrayList<Goods> staffOrderDetailGoodsList) {
        staffOrderDetailGoodsList.clear();
        staffOrderDetailGoodsList.addAll(staffOrderDetailGoodsList);
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_liststore.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.tv_staff_orderDetail_itemGoodsName)
        TextView mTvItemGoodsName;
        @InjectView(R.id.tv_staff_orderDetail_itemGoodsPrice)
        TextView mTvItemGoodsPrice;
        @InjectView(R.id.tv_staff_orderDetail_itemGoodsNumber)
        TextView mTvItemGoodsNumber;
        @InjectView(R.id.img_staff_orderDetail_itemImg)
        ImageView mImgItemGoodsImg;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}