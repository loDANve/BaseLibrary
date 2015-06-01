package com.chanxa.wnb.activity.airticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.androidService.UpDataDownLoadService;
import com.chanxa.wnb.broadcast.DownLoadBoradcast;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.fragment.MainFragment;
import com.chanxa.wnb.fragment.MemberCardFragment;
import com.chanxa.wnb.service.DataService;
import com.chanxa.wnb.view.ViewPagerCompat;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.SystemTool;

import java.io.File;

public class MainActivity extends WnbBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariticket_main);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
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