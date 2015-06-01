package com.chanxa.wnb.fragment.childFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.baidu.mapapi.model.LatLng;
import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MakeDetailsActivity;
import com.chanxa.wnb.adapter.StoreListViewAdapter;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.fragment.StoreFragment;
import com.chanxa.wnb.view.xListView.XListView;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.bitmap.PauseOnScrollListener;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by CHANXA on 2014/12/12.
 */
public class StoreChildListFragment extends BaseFragment implements XListView.IXListViewListener {


    public StoreChildListFragment() {
    }

    public void setParentFragment(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }
    private XListView mListView;
    private ProgressBar progressBar;
    private Fragment parentFragment;
    private ViewGroup container_viewRoot;
    private StoreListViewAdapter storeListViewAdapter;
    private ArrayList<Store> storeArrayList = new ArrayList<>();
    private static int pageIndex = 0;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_storechild_list, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        /** 下拉刷新，上拉加载 */
        mListView = (XListView) rootView.findViewById(R.id.listView_store);// 这个listview是在这个layout里面
        mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据

        storeListViewAdapter = new StoreListViewAdapter(getActivity(), storeArrayList);
        mListView.setAdapter(storeListViewAdapter);
        mListView.setXListViewListener(this);
        mListView.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int maxPage =  ((StoreFragment) parentFragment).getMaxPager();
                if (totalItemCount==firstVisibleItem+visibleItemCount && totalItemCount >= 8) {
                    if (pageIndex > maxPage) {
                        //ViewInject.toast(getActivity(), "没有更多了");
                        onLoad();
                        mListView.setPullLoadEnable(false);
                        return;
                    }
                    pageIndex++;
                    ((StoreFragment) parentFragment).loadMore(pageIndex);
                }
            }
        });

        /*listView = (XListView) rootView.findViewById(R.id.listView_store);
        listView.setAdapter(storeListViewAdapter);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                ((StoreFragment)parentFragment).startLocation();
            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                if (pageIndex == 4){
                    ViewInject.toast(getActivity(), "无法加载更多了");
                    return;
                }
                ((StoreFragment)parentFragment).loadMore(pageIndex);
            }
        });
        listView.setOnScrollListener(new PauseOnScrollListener(WnbApplication.getInstance().getwBitmap(),false, true));*/
        container_viewRoot = (ViewGroup) rootView.findViewById(R.id.container_viewRoot);
        container_viewRoot.removeAllViews();
        container_viewRoot.addView(progressBar);
    }

    /**
     * 停止刷新，
     */
    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    // 刷新
    @Override
    public void onRefresh() {
        pageIndex = 0;
        ((StoreFragment) parentFragment).startLocation();
        mListView.setPullLoadEnable(true);
    }

    // 加载更多
    @Override
    public void onLoadMore() {
        /*pageIndex++;
        int maxPage =  ((StoreFragment) parentFragment).getMaxPager();
        if (pageIndex >= maxPage) {
            ViewInject.toast(getActivity(), "没有更多了");
            onLoad();
            mListView.setPullLoadEnable(false);
            return;
        }
        ((StoreFragment) parentFragment).loadMore(pageIndex);*/
    }



    /**
     * 由父类调用
     */
    public void refresh(LatLng centerLalng, ArrayList<Store> storeArrayLists) {
        storeArrayList.clear();
        storeArrayList.addAll(storeArrayLists);
        storeListViewAdapter.refreshData(storeArrayLists);
        container_viewRoot.removeAllViews();
        container_viewRoot.addView(mListView);
        onLoad();
    }
}
