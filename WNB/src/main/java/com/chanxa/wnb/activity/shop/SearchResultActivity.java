package com.chanxa.wnb.activity.shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.adapter.SearchGridListAdapter;
import com.chanxa.wnb.adapter.ShopHomeGridAdapter;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wtm.library.bitmap.PauseOnScrollListener;
import com.wtm.library.inject.ViewInject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchResultActivity extends WnbBaseActivity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {


    @InjectView(R.id.gridView_searchResult)
    PullToRefreshGridView mGridViewSearchResult;
    @InjectView(R.id.edit_shop_search)
    EditText mEditShopSearch;
    @InjectView(R.id.btn_changeLG)
    Button mBtnChangeLG;
    @InjectView(R.id.listView_searchResult)
    PullToRefreshListView mListViewSearchResult;
    private ArrayList<Goods> goodsArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int curPageIndex = 0;
    private int pageMax = 1;
    private static final int PAGESIZE = 10;
    private String goodsType = null;//查询的商品类别
    private String goodsName = null;//查询的商品名
    private String storeMark = null;//查询的店铺id
    private SearchGridListAdapter adapter;

    private boolean isGridView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        setContentView(R.layout.activity_search_result);
    }

    @Override
    public void initData() {
        super.initData();
        goodsType = getIntent().getStringExtra("goodsType");
        goodsName = getIntent().getStringExtra("goodsName");
        storeMark = getIntent().getStringExtra("storeMark");
        adapter = new SearchGridListAdapter(this, goodsArrayList);
        obtainGoodsData(false);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);

        findViewById(R.id.tv_back).setOnClickListener(this);
        mBtnChangeLG.setOnClickListener(this);
        mEditShopSearch.setOnClickListener(this);

        mGridViewSearchResult.setMode(PullToRefreshBase.Mode.BOTH);
        mGridViewSearchResult.setOnRefreshListener(this);
        mGridViewSearchResult.setAdapter(adapter);
        mGridViewSearchResult.setOnItemClickListener(this);
        mGridViewSearchResult.setOnScrollListener(new PauseOnScrollListener(WnbApplication.getInstance().getwBitmap(), false, true));

        mListViewSearchResult.setMode(PullToRefreshBase.Mode.BOTH);
        mListViewSearchResult.setOnRefreshListener(this);
        mListViewSearchResult.setOnItemClickListener(this);
        mListViewSearchResult.setAdapter(adapter);
        mListViewSearchResult.setVisibility(View.GONE);
        mListViewSearchResult.setOnScrollListener(new PauseOnScrollListener(WnbApplication.getInstance().getwBitmap(), false, true));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.edit_shop_search:
                startActivity(new Intent(this, GoodsSearchActivity.class));
                break;
            case R.id.btn_changeLG:
                //change gridView or listview
                if (isGridView) {
                    mGridViewSearchResult.setVisibility(View.GONE);
                    mListViewSearchResult.setVisibility(View.VISIBLE);
                    adapter.setViewStyle(SearchGridListAdapter.ViewStyle.LIST);
                    isGridView = false;
                } else {
                    mGridViewSearchResult.setVisibility(View.VISIBLE);
                    mListViewSearchResult.setVisibility(View.GONE);
                    adapter.setViewStyle(SearchGridListAdapter.ViewStyle.GRID);
                    isGridView = true;
                }
                mBtnChangeLG.setSelected(!isGridView);
                break;
        }
    }

    private void obtainGoodsData(final boolean isLoadMore) {
        new StoreService().obtainGoods(WnbApplication.getInstance().getCardToken(), 0, PAGESIZE,null, goodsName, "线上订购", goodsType, storeMark, new ServiceCallBack<ArrayList<Goods>>() {
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
                    curPageIndex = 0;
                    goodsArrayList.clear();
                }
                ArrayList<Goods> arrayList = resultObj;
                goodsArrayList.addAll(arrayList);
                adapter.notifyDataSetChanged();
                mGridViewSearchResult.onRefreshComplete();
                mListViewSearchResult.onRefreshComplete();
                curPageIndex++;
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj, int pageSize, int dataSize) {
                curPageIndex = pageSize;
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        obtainGoodsData(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (curPageIndex < pageMax) {
            obtainGoodsData(true);
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ViewInject.toast("没有更多数据");
                    mGridViewSearchResult.onRefreshComplete();
                    mListViewSearchResult.onRefreshComplete();
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getBaseContext(), GoodsDetailsActivity.class);
        if (!isGridView) {
            position -= 1;
        }
        intent.putExtra("goodsMark", goodsArrayList.get(position).getMark());
        startActivity(intent);
    }
}
