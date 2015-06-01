package com.chanxa.wnb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.adapter.MyMakeListViewAdapter;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.MakeRecord;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.MakeService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.view.popwindow.TwoWheelPopDelegate;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;

import java.util.ArrayList;

public class MyMakeActivity extends WnbBaseActivity {
    private PullToRefreshListView listview_myMake;
    private String[] yearArr, monthArr;
    private TwoWheelPopDelegate twoWheelPopDelegate;
    private ArrayList<MakeRecord> makeRecordArrayList = new ArrayList<>();
    private MyMakeListViewAdapter adapter;
    private ProgressBar progressBar;
    private ViewGroup container_makeContent;
    private int curPageIndex = 0;
    private final int pageSize = 10;
    private int pageMax = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_make);
    }

    @Override
    public void initView() {
        super.initView();
        container_makeContent = (ViewGroup) findViewById(R.id.container_makeContent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listview_myMake = (PullToRefreshListView) findViewById(R.id.listview_myMake);
        listview_myMake.setMode(PullToRefreshBase.Mode.BOTH);

        listview_myMake.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPageIndex = 0;
                botainMake(curPageIndex, pageSize, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPageIndex++;
                if (curPageIndex >= pageMax) {

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            ViewInject.toast("没有更多数据");
                            listview_myMake.onRefreshComplete();
                        }
                    });
                    return;
                }
                botainMake(curPageIndex, pageSize, true);
            }
        });
        adapter = new MyMakeListViewAdapter(this, makeRecordArrayList);
        listview_myMake.setAdapter(adapter);
    }

    public void backOnclick(View v) {
        if (R.id.btn_title_back == v.getId()) finish();
    }


    @Override
    public void initData() {
        super.initData();
        yearArr = getResources().getStringArray(R.array.year);
        monthArr = getResources().getStringArray(R.array.month);
        botainMake(curPageIndex, pageSize, false);
    }

    private void botainMake(int pageIndex, int pageSize, final boolean isLoadMore) {
        new MakeService().obtainOnLineMake(getCardToken(), pageIndex, pageSize, new ServiceCallBack() {
            @Override
            public void onStart() {
                container_makeContent.removeAllViews();
                container_makeContent.addView(progressBar);
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("获取预约失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                if (!isLoadMore) {
                    makeRecordArrayList.clear();
                }
                makeRecordArrayList.addAll((java.util.Collection<? extends MakeRecord>) resultObj);
                adapter.notifyDataSetChanged();
                container_makeContent.removeAllViews();
                container_makeContent.addView(listview_myMake);
                listview_myMake.onRefreshComplete();
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageMaxIndex, int dataSize) {
                pageMax = pageMaxIndex;
            }
        });
    }

    public CardToken getCardToken() {
        CardToken cardToken = WnbApplication.getInstance().getCardToken();
        if (cardToken == null) {
            WnbApplication.getInstance().clearCache();
            WActivityManager.getInstance().finishAllActivity();
            startActivity(new Intent(this, WelcomepageActivity.class));
            finish();
        }
        return cardToken;
    }

}