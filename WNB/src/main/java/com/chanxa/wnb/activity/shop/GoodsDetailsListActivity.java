package com.chanxa.wnb.activity.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.view.ElasticListView;
import com.chanxa.wnb.view.SquareRelativeLayout;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商品清单
 */
public class GoodsDetailsListActivity extends BaseActivity {

    @InjectView(R.id.tv_back)
    ImageView mTvBack;
    @InjectView(R.id.listView_goodsDetails)
    ListView mListViewGoodsDetails;
    private ArrayList<Goods> goodsArrayList = new ArrayList<>();
    private WBitmap wBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details_list);
    }

    @Override
    public void initData() {
        super.initData();
        wBitmap = WnbApplication.getInstance().getwBitmap();
        goodsArrayList.clear();
        goodsArrayList.addAll((java.util.Collection<? extends Goods>) getIntent().getSerializableExtra("ArrayList<Goods>"));
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        mTvBack.setOnClickListener(this);
        if (goodsArrayList.size() > 0) {
            mListViewGoodsDetails.setAdapter(new ItsAdapter());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private class ItsAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_list_aty_goods_details, null, false);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Goods goods = goodsArrayList.get(position);
            String path = goods.getImgPath();
            if (!StringUtils.isEmpty(path)) {
                if (path.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(viewHolder.mImageView1, path);
                } else {
                    wBitmap.display(viewHolder.mImageView1, AppConfig.PATH_PREFIX + path);
                }
            }
            double money = goods.getNumber() * StringUtils.toDouble(goods.getMoney());
            viewHolder.mTvMoney.setText("¥"+StringUtils.toDoubleStr(money+"",2));
            viewHolder.mTvGoodsName.setText(goods.getName());
            viewHolder.mTvNumber.setText("x"+goods.getNumber());
            convertView.setTag(viewHolder);
            return convertView;
        }

        private class ViewHolder {
            ImageView mImageView1;
            TextView mTvMoney;
            TextView mTvGoodsName;
            TextView mTvNumber;
            ViewHolder(View view) {
                mImageView1 = (ImageView) view.findViewById(R.id.imageView1);
                mTvMoney = (TextView) view.findViewById(R.id.tv_money);
                mTvGoodsName = (TextView) view.findViewById(R.id.tv_goodsName);
                mTvNumber = (TextView) view.findViewById(R.id.tv_Number);
            }
        }
    }
}
