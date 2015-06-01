package com.chanxa.wnb.activity.staff;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.WelcomeActivity;
import com.chanxa.wnb.adapter.GoodsManageListAdapter;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.StaffServiceCallBack;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffGoodsManageActivity extends BaseActivity {

    @InjectView(R.id.btn_title_back)
    Button mBtnTitleBack;
    @InjectView(R.id.container_addGoods)
    RelativeLayout mContainerAddGoods;
    @InjectView(R.id.listview_goodsManage)
    PullToRefreshListView mListviewGoodsManage;

    private ArrayList<Goods> goodsArrayList=new ArrayList<>();
    private GoodsManageListAdapter adapter;
    private StaffService staffService;
    private int curIndex = 0;
    private final int pageSize = 10;
    private int pageMax = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_goods_manage);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        mBtnTitleBack.setOnClickListener(this);
        mContainerAddGoods.setOnClickListener(this);
        mListviewGoodsManage.setMode(PullToRefreshBase.Mode.BOTH);
        mListviewGoodsManage.setAdapter(adapter);
        mListviewGoodsManage.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curIndex = 0;
                obtainGoods(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                curIndex++;
                if (curIndex >= pageMax) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mListviewGoodsManage.onRefreshComplete();
                            ViewInject.toast("没有更多数据");
                        }
                    });
                    return;
                }
                obtainGoods(true);
            }
        });
        mListviewGoodsManage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StaffGoodsManageActivity.this, StaffUpLoadGoodsActivity.class);
                intent.putExtra(Goods.class.getSimpleName(), goodsArrayList.get(position-1));
                startActivity(intent);
            }
        });
        obtainGoods(false);
    }

    @Override
    public void initData() {
        adapter = new GoodsManageListAdapter(goodsArrayList, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.container_addGoods:
                startActivity(new Intent(this, StaffUpLoadGoodsActivity.class));
                break;
        }
    }

    private void obtainGoods(final boolean isLoadMore) {
        if (staffService == null) {
            staffService = new StaffService();
        }
        Staff staff = WnbApplication.getInstance().getStaff();
        if (staff == null) {
            WActivityManager.getInstance().finishAllActivity();
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }
        staffService.obtainGoods(staff,curIndex,pageSize,new StaffServiceCallBack<ArrayList<Goods>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                mListviewGoodsManage.onRefreshComplete();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                mListviewGoodsManage.onRefreshComplete();
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj, String proof) {
                if (!isLoadMore) {
                    goodsArrayList.clear();
                }
                goodsArrayList.addAll(resultObj);
                adapter.notifyDataSetChanged();
                mListviewGoodsManage.onRefreshComplete();
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj, String proof, int pageSize, int dataSize) {
                WnbApplication.getInstance().getStaff().setProof(proof);
                pageMax = pageSize;
            }
        });
    }
}