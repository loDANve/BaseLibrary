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
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.view.ShopAddSubView;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/12.
 */
public class ShopCartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Goods> arrayList;
    private WBitmap wBitmap;

    public ShopCartAdapter(Context context, ArrayList<Goods> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_shop_cart, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Goods goods = arrayList.get(position);
        if (!StringUtils.isEmpty(goods.getImgPath())) {
            if (goods.getImgPath().startsWith(AppConfig.PATH_PREFIX)) {
                wBitmap.display(viewHolder.mImgCart, goods.getImgPath());
            } else {
                wBitmap.display(viewHolder.mImgCart, AppConfig.PATH_PREFIX + goods.getImgPath());
            }
        }
        viewHolder.mTvCartName.setText(goods.getName());
        viewHolder.mTvCartMoney.setText("¥"+StringUtils.toDoubleStr(StringUtils.toDouble(goods.getMoney())+"",2));
        viewHolder.mPickerCart.setNumber(goods.getNumber());
        viewHolder.mPickerCart.setOnNumberChangeListener(new ShopAddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int count) {
                goods.setNumber(count);
                goods.save();
                WnbApplication.getInstance().getGoodsCart().notifyDataChanged();
            }
        });
        convertView.setTag(viewHolder);
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.img_cart)
        ImageView mImgCart;
        @InjectView(R.id.tv_cartName)
        TextView mTvCartName;
        @InjectView(R.id.tv_cartMoney)
        TextView mTvCartMoney;
        @InjectView(R.id.picker_cart)
        ShopAddSubView mPickerCart;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}