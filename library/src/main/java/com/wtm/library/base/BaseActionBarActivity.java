package com.wtm.library.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wtm.library.inject.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by wtm on 2014/12/6.
 * activity的基类,你的activity应该继承该类
 */
public class BaseActionBarActivity extends ActionBarActivity implements I_Activity {

    // 是否允许开启返回键最后一个activity弹出退出对话框
    private boolean isOpenExitDialog = true;
    // 是否允许全屏
    private boolean allowFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (allowFullScreen) {// 如果允许全屏显示
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        WActivityManager.getInstance().addActivity2Stack(this);
        initData();
        regReceiver();
        super.onCreate(savedInstanceState);
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
        initData();
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

    public boolean isOpenExitDialog() {
        return isOpenExitDialog;
    }

    public void setOpenExitDialog(boolean isOpenExitDialog) {
        this.isOpenExitDialog = isOpenExitDialog;
    }

    public boolean isAllowFullScreen() {
        return allowFullScreen;
    }

    /**
     * 设置是否全屏
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.allowFullScreen = allowFullScreen;
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
}
