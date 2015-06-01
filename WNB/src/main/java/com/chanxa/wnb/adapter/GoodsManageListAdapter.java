package com.chanxa.wnb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/28.
 */
public class GoodsManageListAdapter extends BaseAdapter {

    private ArrayList<Goods> goodsArrayList;
    private Context context;
    private WBitmap wBitmap;

    public GoodsManageListAdapter(ArrayList<Goods> goodsArrayList, Context context) {
        this.goodsArrayList = goodsArrayList;
        this.context = context;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return goodsArrayList.size();
//        return 20;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_goods_manage, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Goods goods = goodsArrayList.get(position);
        if (!StringUtils.isEmpty(goods.getImgPath())) {
            if (goods.getImgPath().startsWith(AppConfig.PATH_PREFIX)) {
                wBitmap.display(viewHolder.mImgItemGoodsManage, goods.getImgPath());
            } else {
                wBitmap.display(viewHolder.mImgItemGoodsManage, AppConfig.PATH_PREFIX + goods.getImgPath());
            }
        }
        viewHolder.mTvItemGoodsManageGoodsName.setText(goods.getName());
        viewHolder.mTvItemGoodsManageGoodsMoney.setText("¥" + StringUtils.toDoubleStr(StringUtils.toDouble(goods.getMoney()) + "", 2));
        convertView.setTag(viewHolder);
        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.img_item_goods_manage)
        ImageView mImgItemGoodsManage;
        @InjectView(R.id.tv_item_goods_manage_goodsName)
        TextView mTvItemGoodsManageGoodsName;
        @InjectView(R.id.tv_item_goods_manage_goodsMoney)
        TextView mTvItemGoodsManageGoodsMoney;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
