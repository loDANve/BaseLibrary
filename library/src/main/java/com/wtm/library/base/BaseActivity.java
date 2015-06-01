package com.wtm.library.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wtm.library.inject.ViewInject;

/**
 * Created by Administrator on 2014/12/11.
 */
public class BaseActivity extends FragmentActivity implements I_Activity,View.OnClickListener{
    // 是否允许开启返回键最后一个activity弹出退出对话框
    private boolean isOpenExitDialog = true;
    // 是否允许全屏
    private boolean allowFullScreen = false;
    // 是否隐藏ActionBar
    private boolean hiddenActionBar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (hiddenActionBar){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        if (allowFullScreen) {// 如果允许全屏显示
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        WActivityManager.getInstance().addActivity2Stack(this);
        initData();
        regReceiver();
    }



    @Override
    protected void onDestroy() {
        unRegReceiver();
        super.onDestroy();
        WActivityManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isOpenExitDialog && keyCode == KeyEvent.KEYCODE_BACK
                && WActivityManager.getInstance().getCount() < 2) {
            ViewInject.getInstance().getExitDialog(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void regReceiver() {

    }

    @Override
    public void unRegReceiver() {

    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
    }
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
    }

    public View getRootView(){
        return ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
    }

    public boolean isOpenExitDialog() {
        return isOpenExitDialog;
    }

    public void setOpenExitDialog(boolean isOpenExitDialog) {
        this.isOpenExitDialog = isOpenExitDialog;
    }

    public boolean isHiddenActionBar() {
        return hiddenActionBar;
    }

    public void setHiddenActionBar(boolean hiddenActionBar) {
        this.hiddenActionBar = hiddenActionBar;
    }

    public boolean isAllowFullScreen() {
        return allowFullScreen;
    }

    public void setAllowFullScreen(boolean allowFullScreen) {
        this.allowFullScreen = allowFullScreen;
    }

    @Override
    public void onClick(View v) {

    }
}
