package com.chanxa.wnb.activity;

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
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.SystemTool;

import java.io.File;

public class MainActivity extends WnbBaseActivity {

    private MainFragment mainFragment = new MainFragment();
    private MemberCardFragment memberCardFragment = new MemberCardFragment();

    private DownLoadBoradcast downLoadBoradcast;

    private ViewPagerCompat viewpager_main;

    private int currentIndex = 1;

    private Fragment[] fragmentArr = new Fragment[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVersion();
    }

    private void checkVersion() {

        new DataService().appVersonUpdata(this, new DataService.VersonCheckUpdata() {
            @Override
            public void haveUpdata(String newestVerson, String curVerson, final String downLoadUrl) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("版本更新").setMessage("发现新版本\n版本号:" + newestVerson +
                        "\n当前版本号:" + curVerson).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, UpDataDownLoadService.class);
                        //intent.setAction("appUpdata");
                        intent.putExtra(AppConfig.DOWNLOAD_URL_KEY, downLoadUrl);
                        startService(intent);
                    }
                }).create();
                alertDialog.show();
            }

            @Override
            public void noUpdata() {

            }
        });
    }

    public void initFragmentList() {
        fragmentArr[0] = memberCardFragment;
        fragmentArr[1] = mainFragment;
    }

    @Override
    public void initData() {
        super.initData();
        initFragmentList();
    }

    @Override
    public void initView() {
        super.initView();
        viewpager_main = (ViewPagerCompat) findViewById(R.id.viewpager_main);
        viewpager_main.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentArr[position];
            }

            @Override
            public int getCount() {
                return fragmentArr.length;
            }
        });
        viewpager_main.setCurrentItem(currentIndex);
        setPagerCanChangePager(false);
        viewpager_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentIndex = position;
                /*if (position == 0 && positionOffset == 0.0) {
                    setPagerCanChangePager(false);
                }*/
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*if (state == 0) {
                    setPagerCanChangePager(currentIndex == 1);
                }*/
                if (state == 0) {
                    setPagerCanChangePager(false);
                }
            }
        });
    }

    @Override
    public void regReceiver() {
        super.regReceiver();
        downLoadBoradcast = new DownLoadBoradcast();
        downLoadBoradcast
                .setOnDownLoadEnd(new DownLoadBoradcast.OnDownLoadEnd() {
                    @Override
                    public void onEnd(File file) {
                        SystemTool.installApk(MainActivity.this, file);
                    }
                });
        IntentFilter filter = new IntentFilter(AppConfig.DOWNLOAD_RECEIVERC);
        registerReceiver(downLoadBoradcast, filter);
    }

    @Override
    public void unRegReceiver() {
        super.unRegReceiver();
        unregisterReceiver(downLoadBoradcast);
    }

    public void pagerIndex(int position) {
        if (position > 1) {
            return;
        }
        LogUtils.e(""+position);
        viewpager_main.setCanTouchChangePager(true);
        viewpager_main.setCurrentItem(position, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       Intent intent = new Intent(MainActivity.this, UpDataDownLoadService.class);
       // intent.setAction("appUpdata");
       stopService(intent);
    }

    public void setPagerCanChangePager(boolean canChange, ViewPagerCompat.OnCanChangePagerListener onCanChangePagerListener) {
        if (onCanChangePagerListener != null) {
            viewpager_main.setOnCanChangePagerListener(onCanChangePagerListener);
        }
        viewpager_main.setCanTouchChangePager(canChange);
    }

    public void setPagerCanChangePager(boolean canChange) {
        viewpager_main.setOnCanChangePagerListener(null);
        viewpager_main.setCanTouchChangePager(canChange);
    }

    public void setPagerTouchMode(boolean mode) {
        viewpager_main.setViewTouchMode(mode);
    }
}