package com.chanxa.wnb.activity.airticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AirTicketMainActivity extends WnbBaseActivity {
    @InjectView(R.id.btn_title_back)
    protected Button btn_title_back;
    @InjectView(R.id.tv_title)
    protected TextView tv_title;
    @InjectView(R.id.btn_oneway)
    protected ImageButton btn_oneway;
    @InjectView(R.id.btn_return)
    protected ImageButton btn_return;
    @InjectView(R.id.btn_international)
    protected ImageButton btn_international;

    private Fragment oneWayFragment;
    private Fragment returnFragment;
    private Fragment internationalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airticket_main);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        tv_title.setText("机票搜索");
        btn_title_back.setOnClickListener(this);
        btn_oneway.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        btn_international.setOnClickListener(this);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.btn_oneway:

                break;
            case R.id.btn_return:

                break;
            case R.id.btn_international:

                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}