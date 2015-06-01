package com.wtm.library.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * 如需使用tab分页,可以使用此类对象
 * Created by Administrator on 2014/12/6.
 */
public abstract class BaseTabActionBarActivity extends BaseActionBarActivity {

    protected ArrayList<BaseFragment> fragmentList;
    private int container_fragment;
    private int currentTabPosition = 0;
    private OnTabChangeListener onTabChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        container_fragment = setFragmentContainer();
        fragmentList = setFragmentList();
        if (container_fragment!=0 && fragmentList!=null){
            initTab();
        }
    }

    /**
     * 切换当前显示的fragment
     * @param position
     */
    protected void changeTab(int position){
        if (fragmentList==null || fragmentList.size()<2) return;
        if (currentTabPosition != position) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragmentList.get(currentTabPosition));
            if (!fragmentList.get(position).isAdded()) {
                trx.add(container_fragment,fragmentList.get(position));
            }
            trx.show(fragmentList.get(position)).commit();
        }
        if (onTabChangeListener!=null){
            onTabChangeListener.onTabChanged(currentTabPosition,position);
        }
        currentTabPosition = position;
    }

    /**
     * 必须为fragment设置一个在rootView存在的容器id
     * @return
     */
    protected abstract int setFragmentContainer();


    /**
     * 设置一个fragment的列表
     */
    public abstract ArrayList<BaseFragment> setFragmentList();

    /**
     * 初始化tab，fragmentList至少需要有2个fragment
     */
    protected  void initTab(){
        getSupportFragmentManager().beginTransaction()
                .add(container_fragment, fragmentList.get(0))
                .add(container_fragment, fragmentList.get(1))
                .hide(fragmentList.get(1)).show(fragmentList.get(0)).commit();
    }

    public interface OnTabChangeListener{
        void onTabChanged(int oldPosition,int newPosition);
    }



    public int getCurrentTabPosition() {
        return currentTabPosition;
    }

    public ArrayList<BaseFragment> getFragmentList() {
        return fragmentList;
    }

    public OnTabChangeListener getOnTabChangeListener() {
        return onTabChangeListener;
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
    }
}
