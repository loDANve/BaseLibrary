package com.chanxa.wnb.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.adapter.LookRecordAdapter;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.StoredValueRecord;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.popwindow.TwoWheelPopDelegate;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 查看记录
 */
public class LookRecordActivity extends WnbBaseActivity {

    private PullToRefreshListView listView;
    private String[] yearArr, monthArr;
    private TwoWheelPopDelegate twoWheelPopDelegate;
    private LookRecordAdapter lookRecordAdapter;
    private ArrayList<StoredValueRecord> storedValueRecordArrayList = new ArrayList<>();
    private int index = 0;//当前页码数
    private final int DATAPAGESIZE = 10;
    private int indexCount;//最大消息页码数
//    private int maxDataSize;//最大消息条目
    private AlertDialog dataDetailsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_record);
        obtainStoredValueRecord(index, DATAPAGESIZE, false);
    }

    private void obtainStoredValueRecord(int index, int size, final boolean isLoadMore) {
        new CardService().obtainStoredValueRecord(getCardToken(), index, size,
                new ServiceCallBack<ArrayList<StoredValueRecord>>() {
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
                    public void onEXECSuccess(ArrayList<StoredValueRecord> resultObj) {
                        ArrayList<StoredValueRecord> storedValueRecordArrayLists = resultObj;
                        if (!isLoadMore) {
                            storedValueRecordArrayList.clear();
                        }
                        storedValueRecordArrayList.addAll(storedValueRecordArrayLists);
                        lookRecordAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }

                    @Override
                    public void onEXECSuccess(ArrayList<StoredValueRecord> resultObj, int pageSize, int dataSize) {
                        indexCount = pageSize;
//                        maxDataSize = dataSize;
                    }
                });
    }

    @Override
    public void initView() {
        super.initView();
        listView = (PullToRefreshListView) this.findViewById(R.id.listview_lookRecord);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        lookRecordAdapter = new LookRecordAdapter(this, storedValueRecordArrayList);
        //findViewById(R.id.btn_lookRecordMonthChoose).setOnClickListener(this);
        listView.setAdapter(lookRecordAdapter);
        initCheckPop();
        initDataDialog();
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                obtainStoredValueRecord(index, DATAPAGESIZE, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                if (index >= indexCount) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            ViewInject.toast("没有更多数据");
                            listView.onRefreshComplete();
                        }
                    });
                    return;
                }
               obtainStoredValueRecord(index, DATAPAGESIZE, true);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reMark = storedValueRecordArrayList.get(position-1).getRaMark();
                if (StringUtils.isEmpty(reMark)) {
                    return;
                }
                dataDetailsDialog.setMessage(reMark);
                dataDetailsDialog.show();
            }
        });
    }

    private void initDataDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        dataDetailsDialog = builder.create();
    }

    public void backOnclick(View v) {
        if (R.id.btn_title_back == v.getId()) finish();
    }
    @Override
    public void initData() {
        super.initData();
        yearArr = getResources().getStringArray(R.array.year);
        monthArr = getResources().getStringArray(R.array.month);
    }

    private void initCheckPop() {
        twoWheelPopDelegate = new TwoWheelPopDelegate(this, yearArr, monthArr);
        twoWheelPopDelegate.setOnButtonClick(new TwoWheelPopDelegate.OnButtonClick() {
            @Override
            public void onPositivebtnClick(WheelView wheelView1, WheelView wheelView2) {
                onSelectDate(yearArr[wheelView1.getSelectedItemPosition()], monthArr[wheelView2.getSelectedItemPosition()]);
            }

            @Override
            public void onNegativebtnClick(WheelView wheelView1, WheelView wheelView2) {
            }
        });
    }

    private void onSelectDate(String year, String month) {
        ViewInject.toast(year + month);
    }
}
