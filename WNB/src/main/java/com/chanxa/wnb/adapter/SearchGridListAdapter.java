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
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/8.
 */
public class SearchGridListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Goods> goodsArrayList;
    private WBitmap wBitmap;

    public enum  ViewStyle{
        LIST,GRID
    }

    private ViewStyle viewStyle = ViewStyle.GRID;

    public SearchGridListAdapter(Context context, ArrayList<Goods> goodsArrayList) {
        this.context = context;
        this.goodsArrayList = goodsArrayList;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return goodsArrayList.size();
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
            if (viewStyle.equals(ViewStyle.GRID)) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_grid, null, false);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_search_goods, null, false);
            }
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Goods goods = goodsArrayList.get(position);
        String path = goods.getImgPath();
        if (!StringUtils.isEmpty(path)) {
            if (path.contains("_small_")) {
                path = path.replaceAll("_small_", "_320x320_");
            }
            if (!path.contains("_320x320_")) {
                if (path.contains(".jpg")) {
                    path = path.replaceAll("_320x320_.jpg", ".jpg");
                } else if (path.contains(".png")) {
                    path = path.replaceAll("_320x320_.png", ".png");
                }
            }
            if (path.startsWith(AppConfig.PATH_PREFIX)) {
                wBitmap.display(viewHolder.mImgItemShopGrid, path);
            } else {
                wBitmap.display(viewHolder.mImgItemShopGrid, AppConfig.PATH_PREFIX + path);
            }
        }
        viewHolder.mTvItemShopDescription.setText(goods.getName());
        viewHolder.mTvItemShopMoney.setText("¥" + StringUtils.toDoubleStr(goods.getMoney(), 2));
        convertView.setTag(viewHolder);
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_shop_grid.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.img_item_shopGrid)
        ImageView mImgItemShopGrid;
        @InjectView(R.id.tv_item_shopDescription)
        TextView mTvItemShopDescription;
        @InjectView(R.id.tv_item_shopMoney)
        TextView mTvItemShopMoney;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public ViewStyle getViewStyle() {
        return viewStyle;
    }

    public void setViewStyle(ViewStyle viewStyle) {
        this.viewStyle = viewStyle;
    }
}
