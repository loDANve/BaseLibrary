package com.chanxa.wnb.fragment.staff;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.adapter.CommissionAdapter;
import com.chanxa.wnb.adapter.DrawingAdapter;
import com.chanxa.wnb.bean.Commission;
import com.chanxa.wnb.bean.Drawing;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.StaffServiceCallBack;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/8.
 */
public class DrawingFragment extends BaseFragment {

    private PullToRefreshListView pullToRefreshListView;

    private int index = 0;//当前页码数
    private final int DATAPAGESIZE = 10;
    private int indexCount;//最大消息页码数
    private ArrayList<Drawing> drawingArrayList = new ArrayList<>();
    private DrawingAdapter adapter;
    private boolean firstLoadData = true;
    private AlertDialog dataDetailsDialog;//显示信息对话框
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.staff_fragment, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        initDataDialog();
        pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview_lookRecord);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new DrawingAdapter(getActivity(),drawingArrayList);
        pullToRefreshListView.setAdapter(adapter);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                obtainDrawingDetails(index, DATAPAGESIZE, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                if (index >= indexCount) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            ViewInject.toast("没有更多数据");
                            pullToRefreshListView.onRefreshComplete();
                        }
                    });
                    return;
                }
                obtainDrawingDetails(index, DATAPAGESIZE, true);
            }
        });
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reMark = drawingArrayList.get(position-1).getReMark();
                if (StringUtils.isEmpty(reMark)) {
                    return;
                }
                dataDetailsDialog.setMessage(reMark);
                dataDetailsDialog.show();
            }
        });
    }

    /**
     * 数据懒加载
     */
    public void loadData() {
        if (firstLoadData) {
            obtainDrawingDetails(index, DATAPAGESIZE, false);
            firstLoadData = false;
        }
    }

    public void obtainDrawingDetails(int index, int size, final boolean isLoadMore) {
        new StaffService().obtainDrawingDetails(WnbApplication.getInstance().getStaff(), index, size, new StaffServiceCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {

            }

            @Override
            public void onEXECisFalse(String errroMsg) {

            }

            @Override
            public void onEXECSuccess(Object resultObj, String proof) {
                WnbApplication.getInstance().getStaff().setProof(proof);
                ArrayList<Drawing> drawingArrayList1 = (ArrayList<Drawing>) resultObj;
                if (!isLoadMore) {
                    drawingArrayList.clear();
                }
                drawingArrayList.addAll(drawingArrayList1);
                adapter.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onEXECSuccess(Object resultObj, String proof, int pageSize, int dataSize) {
                indexCount = pageSize;
            }
        });
    }

    private void initDataDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        dataDetailsDialog = builder.create();
    }
}

