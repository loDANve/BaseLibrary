package com.chanxa.wnb.activity.shop;

import android.os.Bundle;
import android.view.View;

import com.chanxa.wnb.R;
import com.wtm.library.base.BaseActivity;

public class ShopEvaluationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_evaluation);
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
