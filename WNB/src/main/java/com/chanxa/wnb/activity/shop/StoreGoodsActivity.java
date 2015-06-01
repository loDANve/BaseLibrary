package com.chanxa.wnb.activity.shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.adapter.ShopHomeGridAdapter;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.ScrollGridView;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 店铺
 */
public class StoreGoodsActivity extends WnbBaseActivity {

    @InjectView(R.id.img_store)
    ImageView mImgStore;
    @InjectView(R.id.tv_store_name)
    TextView mTvStoreName;
    @InjectView(R.id.store_gridView)
    ScrollGridView mStoreGridView;
    @InjectView(R.id.pullScrllview_store)
    PullToRefreshScrollView mPullScrllviewStore;
    @InjectView(R.id.tv_store_phone)
    TextView mTvStorePhone;
    @InjectView(R.id.img_store_call)
    ImageView mImgStoreCall;

    private ProgressDialog progressDialog;
    private int pageIndex = 0;
    private int pageMax = 0;
    private static final int PAGESIZE = 10;
    private ArrayList<Goods> goodsArrayList = new ArrayList<>();
    private String storeMark;
    private ShopHomeGridAdapter adapter;
    private Store curStore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_goods);
    }

    @Override
    public void initData() {
        super.initData();
        storeMark = getIntent().getStringExtra("storeMark");
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        mImgStoreCall.setOnClickListener(this);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPullScrllviewStore.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScrllviewStore.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex = 0;
                obtainGoodsData(pageIndex, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex++;
                if (pageIndex < pageMax) {
                    obtainGoodsData(pageIndex, true);
                    return;
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ViewInject.toast("没有更多商品了");
                        mPullScrllviewStore.onRefreshComplete();
                    }
                });
            }
        });
        if (adapter == null) {
            adapter = new ShopHomeGridAdapter(this, goodsArrayList);
        }
        mStoreGridView.setAdapter(adapter);
        mStoreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreGoodsActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("goodsMark", goodsArrayList.get(position).getMark());
                startActivity(intent);
            }
        });
        obtainGoodsData(pageIndex, false);
        obtainStoreDetails();
    }

    private void store2View() {
        if (curStore == null) {
            return;
        }
        if (!StringUtils.isEmpty(curStore.getImgPath())) {
            WnbApplication.getInstance().getwBitmap().display(mImgStore, AppConfig.PATH_PREFIX+curStore.getImgPath());
        }
        mTvStoreName.setText(curStore.getName());
        mTvStorePhone.setText(curStore.getPhone());
    }

    //获取商品数据
    private void obtainGoodsData(int pageIndex, final boolean isLoadMore) {
        if (!WnbApplication.getInstance().verificationData()) {
            return;
        }
        new StoreService().obtainGoods(WnbApplication.getInstance().getCardToken(), pageIndex, PAGESIZE, null, "", "线上订购", null, storeMark, new ServiceCallBack<ArrayList<Goods>>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                progressDialog.dismiss();
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj) {
                if (!isLoadMore) {
                    goodsArrayList.clear();
                }
                goodsArrayList.addAll(resultObj);
                mPullScrllviewStore.onRefreshComplete();
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj, int pageSize, int dataSize) {
                pageMax = pageSize;
            }
        });
    }

    private void obtainStoreDetails() {
        if (StringUtils.isEmpty(storeMark)) {
            return;
        }
        new StoreService().obtainStoreDetails(getCardToken(), storeMark, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {

            }

            @Override
            public void onEXECisFalse(String errroMsg) {

            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    Store store = XmlUtils.readXML2BeanList(result, Store.class, "DATA", Mapping.storeMapping).get(0);
                    curStore = store;
                    store2View();
                } catch (Exception e) {
                    onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (curStore == null) {
            return;
        }
        Intent intentPhone = new Intent();
        intentPhone.setAction("android.intent.action.DIAL");
        intentPhone.setData(Uri.parse("tel:" + curStore.getPhone()));
        startActivity(intentPhone);
    }
}