package com.chanxa.wnb.activity.staff;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.adapter.Staff_OrderDetailListViewAdapter;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.StaffOrderDetail;
import com.chanxa.wnb.bean.onLineShop.Order;
import com.chanxa.wnb.bean.onLineShop.OrderDetails;

import java.util.ArrayList;

public class StaffOrderDetailActivity extends DefaultTitleActivity{

    private Order staffOrderDetail;
    private ListView orderDetailListView;
    private Staff_OrderDetailListViewAdapter listViewAdapter;
    private ArrayList<OrderDetails> staffOrderDetailGoodsList = new ArrayList<OrderDetails>();

    private TextView tv_orderNumber, tv_user, tv_phone, tv_coordinate, tv_sumPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_order_detail);
    }

    @Override
    public void initView() {
        super.initView();
        orderDetailListView = (ListView)findViewById(R.id.listView_staff_orderDetail);
        listViewAdapter = new Staff_OrderDetailListViewAdapter(StaffOrderDetailActivity.this, staffOrderDetailGoodsList);
        orderDetailListView.setAdapter(listViewAdapter);

        tv_orderNumber = (TextView)findViewById(R.id.tv_staff_orderDetail_orderNumber);
        tv_orderNumber.setText("订单号:"+ staffOrderDetail.getOrderNumber());
        tv_user = (TextView)findViewById(R.id.tv_staff_orderDetail_user);
        tv_user.setText(staffOrderDetail.getReceiverPersonName());
        tv_phone = (TextView)findViewById(R.id.tv_staff_orderDetail_phone);
        tv_phone.setText(staffOrderDetail.getReceiverPersonPhone());
        tv_coordinate = (TextView)findViewById(R.id.tv_staff_orderDetail_coordinate);
        tv_coordinate.setText(staffOrderDetail.getReceiverAddress());
        tv_coordinate = (TextView)findViewById(R.id.tv_staff_orderDetail_coordinate);
        tv_coordinate.setText(staffOrderDetail.getReceiverAddress());
        tv_coordinate = (TextView)findViewById(R.id.tv_staff_orderDetail_sumPrice);
        tv_coordinate.setText(staffOrderDetail.getReceiverAddress());

    }

    @Override
    public void initData() {
        super.initData();
        staffOrderDetail = (Order) getIntent().getSerializableExtra("staffOrderDetail");
        if (staffOrderDetail == null) {
            return;
        }
        staffOrderDetailGoodsList = staffOrderDetail.getOrderDetailsArrayList();
    }

    @Override
    public String initTitleText() {
        return "订单详情";
    }
}
