package com.wtm.library.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wtm on 2014/12/6.
 * fragment的基类,如需使用fragment 你应该继承此类
 */
public abstract class BaseFragment extends Fragment{

    protected abstract View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected  View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        rootView = inflaterView(inflater,container,savedInstanceState);
        initData();
        initView();
        regReceiver();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegReceiver();
    }

    /**
     * 在此方法内注册广播监听,此方法无需手动调用
     */
    public void regReceiver(){};

    /**
     * 取消广播,此方法无需手动调用
     */
    public void unRegReceiver(){};
    /**
     * 初始化视图,在初始化数据之后
     */
    public void initView(){};
    /**
     * 初始化数据
     */
    public void initData(){};

    public View getRootView() {
        return rootView;
    }
}
