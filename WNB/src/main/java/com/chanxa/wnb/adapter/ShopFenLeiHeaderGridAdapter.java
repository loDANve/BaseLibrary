package com.chanxa.wnb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.StaffStoreGoodsLV2;
import com.chanxa.wnb.bean.onLineShop.GoodsHeader;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.view.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/12.
 */
public class ShopFenLeiHeaderGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private Context context;
    private ArrayList<GoodsHeader> goodsHeaderArrayList;
    private WBitmap wBitmap;

    public ShopFenLeiHeaderGridAdapter(Context context, ArrayList<GoodsHeader> goodsHeaderArrayList) {
        this.context = context;
        this.goodsHeaderArrayList = goodsHeaderArrayList;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        return goodsHeaderArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsHeaderArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridheader_view, null, false);
            viewHolder = new GridViewHolder(convertView);
        } else {
            viewHolder = (GridViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(goodsHeaderArrayList.get(position).getName());
        String imgPath = goodsHeaderArrayList.get(position).getImgPath();
        if (!StringUtils.isEmpty(imgPath)) {
            if (imgPath.startsWith(AppConfig.PATH_PREFIX)) {
                wBitmap.display(viewHolder.mImageView3, imgPath);
            } else {
                wBitmap.display(viewHolder.mImageView3, AppConfig.PATH_PREFIX + imgPath);
            }
        }
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return goodsHeaderArrayList.get(position).getGoodsHeaderParent().getHearId();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_header, null, false);
            viewHolder = new HeaderViewHolder(convertView);
        } else {
            viewHolder = (HeaderViewHolder) convertView.getTag();
        }
        GoodsHeader goodsHeader = goodsHeaderArrayList.get(position);
        viewHolder.mTvHeader.setText(goodsHeader.getGoodsHeaderParent().getName());
        convertView.setTag(viewHolder);
        return convertView;
    }

    static class GridViewHolder {
        @InjectView(R.id.imageView3)
        ImageView mImageView3;
        @InjectView(R.id.textView)
        TextView mTextView;

        GridViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class HeaderViewHolder {
        @InjectView(R.id.tv_header)
        TextView mTvHeader;

        HeaderViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
