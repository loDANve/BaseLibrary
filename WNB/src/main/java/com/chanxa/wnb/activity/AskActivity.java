package com.chanxa.wnb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.activity.shop.BugGoodsDetailsActivity;
import com.chanxa.wnb.adapter.AskListAdapter;
import com.chanxa.wnb.bean.onLineShop.Ask;
import com.chanxa.wnb.view.StretchedListView;
import com.wtm.library.inject.ViewInject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 问答活动
 */
public class AskActivity extends DefaultTitleActivity {

    @InjectView(R.id.list_ask)
    StretchedListView mListAsk;
    @InjectView(R.id.btn_ask_sub)
    Button mBtnAskSub;
    ArrayList<Ask> askArrayList = null;
    private AskListAdapter askListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
    }

    @Override
    public void initData() {
        super.initData();
        askArrayList = (ArrayList<Ask>) getIntent().getSerializableExtra("ArrayList<Ask>");
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        mBtnAskSub.setOnClickListener(this);
        if (askArrayList != null) {
            askListAdapter = new AskListAdapter(this,askArrayList);
            mListAsk.setAdapter(askListAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (askListAdapter == null) {
            return;
        }
        if (askListAdapter != null) {
            askListAdapter.setShowCorrectIcon(true);
            askListAdapter.notifyDataSetChanged();
            if (askListAdapter.isAllYes()) {
                Intent intent = getIntent();
                intent.setClass(this, BugGoodsDetailsActivity.class);
                startActivity(intent);
                finish();
            } else {
                ViewInject.toast("回答错误,请重新答题");
            }
        }
    }

    @Override
    public String initTitleText() {
        return "问答";
    }
}
