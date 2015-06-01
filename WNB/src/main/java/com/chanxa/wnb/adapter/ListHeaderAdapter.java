package com.chanxa.wnb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.onLineShop.GoodsHeader;
import com.chanxa.wnb.bean.onLineShop.GoodsHeaderParent;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/19.
 */
public class ListHeaderAdapter extends BaseAdapter {
    private ArrayList<GoodsHeaderParent> goodsHeaderParents;
    private Context context;
    private WBitmap wBitmap;
    private OnItemClick onItemClick;

    public ListHeaderAdapter(ArrayList<GoodsHeaderParent> goodsHeaderParents, Context context) {
        this.goodsHeaderParents = goodsHeaderParents;
        this.context = context;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    @Override
    public int getCount() {
        LogUtils.e("111");
        return goodsHeaderParents.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listheader, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final GoodsHeaderParent goodsHeaderParent = goodsHeaderParents.get(position);
        viewHolder.mItemTvListHeader.setText(goodsHeaderParent.getName());
        viewHolder.mItemTvListHeader.setBackgroundColor(0x00ffffff);
        viewHolder.mItemTvListHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.toDo(goodsHeaderParent.getMark());
                }
            }
        });
        viewHolder.mItemContainerListHeader.removeAllViews();
        boolean isNextLine = false;
        ViewGroup viewGroup = null;
        for (int i = 0; i < goodsHeaderParent.getGoodsHeaderArrayList().size(); i++) {
            final GoodsHeader goodsHeader = goodsHeaderParent.getGoodsHeaderArrayList().get(i);
            ImageView imageView = null;
            TextView textView = null;
            switch (i % 3) {
                case 0:
                    viewGroup = null;
                    viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.item_listview_header_con, null, false);
                    imageView = (ImageView) viewGroup.findViewById(R.id.imageView1);
                    textView = (TextView) viewGroup.findViewById(R.id.textView1);
                    viewHolder.mItemContainerListHeader.addView(viewGroup);
                    break;
                case 1:
                    imageView = (ImageView) viewGroup.findViewById(R.id.imageView2);
                    textView = (TextView) viewGroup.findViewById(R.id.textView2);
                    break;
                case 2:
                    imageView = (ImageView) viewGroup.findViewById(R.id.imageView3);
                    textView = (TextView) viewGroup.findViewById(R.id.textView3);
                    isNextLine = true;
                    break;
            }
            String path = goodsHeader.getImgPath();
            if (!StringUtils.isEmpty(path)) {
                if (path.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(imageView, path);
                } else {
                    wBitmap.display(imageView, AppConfig.PATH_PREFIX + path);
                }
            }
            imageView.setBackgroundColor(0xffffffff);
            textView.setText(goodsHeader.getName());
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.toDo(goodsHeader.getMark());
                    }
                }
            };
            imageView.setOnClickListener(onClickListener);
            textView.setOnClickListener(onClickListener);
           /* if (isNextLine || goodsHeaderParent.getGoodsHeaderArrayList().size() < 4) {
                viewHolder.mItemContainerListHeader.addView(viewGroup);
                viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.item_listview_header_con, null, false);
                isNextLine = false;
            }*/
        }
        convertView.setTag(viewHolder);
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_listheader.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.item_tv_listHeader)
        TextView mItemTvListHeader;
        @InjectView(R.id.item_containerListHeader)
        LinearLayout mItemContainerListHeader;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void toDo(String mark);
    }
}
