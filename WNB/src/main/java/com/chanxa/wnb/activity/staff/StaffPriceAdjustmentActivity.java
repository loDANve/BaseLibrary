package com.chanxa.wnb.activity.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.adapter.Staff_ManageOrderListViewAdapter;
import com.chanxa.wnb.adapter.Staff_PriceAdjustmentListViewAdapter;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.StaffOrderDetail;
import com.chanxa.wnb.view.xListView.XListView;

import java.util.ArrayList;

public class StaffPriceAdjustmentActivity extends DefaultTitleActivity{

    private StaffOrderDetail staffOrderDetail;
    private ListView goodsList;
    private Staff_PriceAdjustmentListViewAdapter priceAdjustmentListViewAdapter;
    private ArrayList<Goods> staffOrderDetailGoodsList = new ArrayList<Goods>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_price_adjustment);
    }

    @Override
    public void initView() {
        super.initView();
        goodsList = (ListView)findViewById(R.id.listView_staff_priceAdjustment);
        priceAdjustmentListViewAdapter = new Staff_PriceAdjustmentListViewAdapter(StaffPriceAdjustmentActivity.this, staffOrderDetailGoodsList);
        goodsList.setAdapter(priceAdjustmentListViewAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        staffOrderDetail = (StaffOrderDetail) getIntent().getSerializableExtra("staffOrderDetail");
        if (staffOrderDetail == null) {
            return;
        }
        staffOrderDetailGoodsList = staffOrderDetail.getGoodsList();
    }

    @Override
    public String initTitleText() {
        return "单价调整";
    }
}
