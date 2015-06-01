package com.chanxa.wnb.activity.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.adapter.Staff_ManageOrderListViewAdapter;
import com.chanxa.wnb.bean.Drawing;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.StaffOrderDetail;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.StaffServiceCallBack;
import com.chanxa.wnb.view.xListView.XListView;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.theme.classic.FollowListPage;

public class StaffManageOrderActivity extends DefaultTitleActivity implements XListView.IXListViewListener{

    private XListView orderListView;
    private Staff_ManageOrderListViewAdapter manageOrderAdapter;
    private ArrayList<Order> staffOrderList = new ArrayList<>();

    private int index = 0;  //当前页码数
    private int indexCount; //最大消息页码数
    private final int DATAPAGESIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_order);
    }

    @Override
    public void initView() {
        super.initView();
        //下拉刷新，上拉加载
        orderListView = (XListView)findViewById(R.id.listView_staff_manageOrder);
        orderListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据

        manageOrderAdapter = new Staff_ManageOrderListViewAdapter(StaffManageOrderActivity.this, staffOrderList);
        orderListView.setAdapter(manageOrderAdapter);
        orderListView.setXListViewListener(this);
        orderListView.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*int maxPage =  ((StoreFragment) parentFragment).getMaxPager();
                if (totalItemCount==firstVisibleItem+visibleItemCount && totalItemCount >= 8) {
                    if (pageIndex > maxPage) {
                        //ViewInject.toast(getActivity(), "没有更多了");
                        onLoad();
                        mListView.setPullLoadEnable(false);
                        return;
                    }
                    pageIndex++;
                    ((StoreFragment) parentFragment).loadMore(pageIndex);
                }*/
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        obtainGoodsOrder(index, DATAPAGESIZE, false);
    }

    public void obtainGoodsOrder(int index, int size, final boolean isLoadMore) {
        new StaffService().obtainGoodsOrder(WnbApplication.getInstance().getStaff(), index, size, new StaffServiceCallBack<ArrayList<Order>>() {
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
            public void onEXECSuccess(ArrayList<Order> resultObj, String proof) {
                WnbApplication.getInstance().getStaff().setProof(proof);
                ArrayList<Order> orderList = (ArrayList<Order>) resultObj;
                if (!isLoadMore) {
                    staffOrderList.clear();
                }
                staffOrderList.addAll(orderList);
                /*for (int i =0; i<staffOrderList.size(); i++){
                    for (int j =0; j<staffOrderList.get(i).getOrderDetailsArrayList().size(); j++) {
                        LogUtils.fff(staffOrderList.get(i).getOrderDetailsArrayList().get(j).getImgPath());
                    }
                }*/
                manageOrderAdapter.notifyDataSetChanged();
                //pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onEXECSuccess(ArrayList<Order> resultObj, String proof, int pageSize, int dataSize) {
                indexCount = pageSize;
            }
        });
    }

    /**
     * 停止刷新，
     */
    private void onLoad() {
        /*mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");*/
    }

    /**
     * 刷新，
     */
    @Override
    public void onRefresh() {
        /*pageIndex = 0;
        ((StoreFragment) parentFragment).startLocation();
        mListView.setPullLoadEnable(true);*/
    }

    // 加载更多
    @Override
    public void onLoadMore() {
        /*pageIndex++;
        int maxPage =  ((StoreFragment) parentFragment).getMaxPager();
        if (pageIndex >= maxPage) {
            ViewInject.toast(getActivity(), "没有更多了");
            onLoad();
            mListView.setPullLoadEnable(false);
            return;
        }
        ((StoreFragment) parentFragment).loadMore(pageIndex);*/
    }

    @Override
    public String initTitleText() {
        return "订单管理";
    }
}
