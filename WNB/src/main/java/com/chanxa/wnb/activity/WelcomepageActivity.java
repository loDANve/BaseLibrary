package com.chanxa.wnb.activity;

import android.os.Bundle;
import android.view.View;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.wtm.library.base.BaseActivity;

/**
 * Created by CHANXA on 2014/12/30.
 */
public class WelcomepageActivity extends WnbBaseActivity {

    public WelcomepageActivity(){setAllowFullScreen(true);setHiddenActionBar(true);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
