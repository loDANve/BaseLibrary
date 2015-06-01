package com.chanxa.wnb.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.adapter.OrderDetailsListViewAdapter;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.view.StretchedListView;
import com.wtm.library.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderDetailsActivity extends WnbBaseActivity {
    @InjectView(R.id.tv_orderGoodsState)
    TextView mTvOrderGoodsState;
    @InjectView(R.id.tv_orderGoodsNumber)
    TextView mTvOrderGoodsNumber;
    @InjectView(R.id.tv_orderDetails_ReceiveName)
    TextView mTvOrderDetailsReceiveName;
    @InjectView(R.id.tv_orderDetails_ReceivePhone)
    TextView mTvOrderDetailsReceivePhone;
    @InjectView(R.id.tv_orderDetails_ReceiveAddress)
    TextView mTvOrderDetailsReceiveAddress;
    @InjectView(R.id.listview_orderDetails)
    StretchedListView mListviewOrderDetails;
    @InjectView(R.id.tv_goodsCountMoney)
    TextView mTvGoodsCountMoney;
    @InjectView(R.id.tv_goodsFreight)
    TextView mTvGoodsFreight;
    @InjectView(R.id.tv_orderDetails_Payment)
    TextView mTvOrderDetailsPayment;
    @InjectView(R.id.tv_orderCreateTime)
    TextView mTvOrderCreateTime;
    private Order order;
    private OrderDetailsListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details2);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        mTvOrderGoodsState.setText(order.getState());
        mTvOrderGoodsNumber.setText("订单号" + order.getOrderNumber());
        mTvOrderDetailsReceiveName.setText(order.getReceiverPersonName());
        mTvOrderDetailsReceivePhone.setText(order.getReceiverPersonPhone());
        mTvOrderDetailsReceiveAddress.setText(order.getReceiverAddress());
        mTvGoodsCountMoney.setText("¥" + StringUtils.toDoubleStr(StringUtils.toDouble(order.getConsumptionMoney()) - StringUtils.toDouble(order.getReceiverMoney()) + "", 2));
        mTvOrderDetailsPayment.setText("¥" + order.getConsumptionMoney());
        mTvGoodsFreight.setText("¥" + order.getReceiverMoney());
        mTvOrderCreateTime.setText("下单时间:" + order.getSubTime());
        adapter = new OrderDetailsListViewAdapter(order.getOrderDetailsArrayList(), this);
        mListviewOrderDetails.setAdapter(adapter);
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.btn_evaluation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_evaluation:
                startActivity(new Intent(this,ShopEvaluationActivity.class));
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        order = (Order) getIntent().getExtras().get(Order.class.getSimpleName());
    }
}
