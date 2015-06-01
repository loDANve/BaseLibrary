package com.chanxa.wnb.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.adapter.OrderDetailsListAdapter;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.inject.ViewInject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderListActivity extends WnbBaseActivity {
    PullToRefreshListView mListviewOrderDetails;
    private OrderDetailsListAdapter orderDetailsListAdapter;
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private int indexCount = 0;//页数max
    private final int pageDataSize = 10;//页面条数
    private int curPageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
    }

    @Override
    public void initView() {
        mListviewOrderDetails = (PullToRefreshListView) findViewById(R.id.listview_orderDetails);
        mListviewOrderDetails.setMode(PullToRefreshBase.Mode.BOTH);
        orderDetailsListAdapter = new OrderDetailsListAdapter(this, orderArrayList);
        mListviewOrderDetails.setAdapter(orderDetailsListAdapter);
        orderDetailsListAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderListActivity.this, OrderDetailsActivity.class);
                intent.putExtra(Order.class.getSimpleName(),orderArrayList.get(position));
                startActivity(intent);
            }
        });
        mListviewOrderDetails.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ontainOrder(curPageIndex, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (curPageIndex >= indexCount) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            ViewInject.toast("没有更多数据");
                            mListviewOrderDetails.onRefreshComplete();
                        }
                    });
                    return;
                }
                ontainOrder(curPageIndex, true);
            }
        });
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        ontainOrder(curPageIndex, false);
    }

    public void ontainOrder(int pageIndex, final boolean isLoadMore) {
        new StoreService().obtianGoodsOrder(WnbApplication.getInstance().getCardToken(), pageIndex, pageDataSize, new ServiceCallBack<ArrayList<Order>>() {
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
            public void onEXECSuccess(ArrayList<Order> resultObj) {
                ArrayList<Order> tempOrderArrayList = (ArrayList<Order>) resultObj;
                if (!isLoadMore) {
                    orderArrayList.clear();
                    indexCount = 0;
                }
                orderArrayList.addAll(tempOrderArrayList);
                orderDetailsListAdapter.notifyDataSetChanged();
                curPageIndex++;
                mListviewOrderDetails.onRefreshComplete();
            }

            @Override
            public void onEXECSuccess(ArrayList<Order> resultObj, int pageSize, int dataSize) {
                indexCount = pageSize;
            }
        });
    }
}