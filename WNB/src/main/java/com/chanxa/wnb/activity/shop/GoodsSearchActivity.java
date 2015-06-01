package com.chanxa.wnb.activity.shop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.dao.SearchRecordDataDAO;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodsSearchActivity extends WnbBaseActivity {

    @InjectView(R.id.search_back)
    ImageButton mSearchBack;
    @InjectView(R.id.edit_search)
    EditText mEditSearch;
    @InjectView(R.id.tv_search)
    TextView mTvSearch;
    @InjectView(R.id.listview_searchRecord)
    ListView mListviewSearchRecord;
    Button btn_searchClearRecord;
    private SearchRecordDataDAO searchRecordDataDAO;
    private View footView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> recordData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_search);
        dataBind();
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        mSearchBack.setOnClickListener(this);
        mTvSearch.setOnClickListener(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mEditSearch.setFocusable(true);
                mEditSearch.setFocusableInTouchMode(true);
                mEditSearch.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEditSearch,
                        InputMethodManager.RESULT_SHOWN);
                inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        footView = LayoutInflater.from(this).inflate(
                R.layout.foot_listview_search, null);
        btn_searchClearRecord = (Button) footView
                .findViewById(R.id.btn_searchClearRecord);
        btn_searchClearRecord.setOnClickListener(this);
        mListviewSearchRecord.addFooterView(footView);
        super.initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.search_back:
                SystemTool.hideKeyBoard(this);
                finish();
                break;
            case R.id.btn_searchClearRecord:
                clearRecord();
                break;
            case R.id.tv_search:
                // 1.add to record
                String searchData = mEditSearch.getText().toString();
                if (StringUtils.isEmpty(searchData)) {
                    return;
                }
                // 1.add to record
                searchRecordDataDAO.add(mEditSearch.getText().toString());
                SystemTool.hideKeyBoard(this);
                // 2.search data
                Intent intent = new Intent(this, SearchResultActivity.class);
                intent.putExtra("goodsType", "");
                intent.putExtra("goodsName", searchData);
                startActivity(intent);
                break;
        }
    }

    // set searchRecordListView data
    private void dataBind() {
        Collections.reverse(recordData);
        adapter = new ArrayAdapter(this, R.layout.item_listview4search,
                R.id.txt_item_searchRecord, recordData);
        mListviewSearchRecord.setAdapter(adapter);
        mListviewSearchRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mEditSearch.setText(recordData.get(position));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        dataBind();
    }

    @Override
    public void initData() {
        super.initData();
        recordData = new ArrayList<String>();
        searchRecordDataDAO = new SearchRecordDataDAO(this);
        ArrayList<String> localData = searchRecordDataDAO.findAll2List();
        if (localData != null && localData.size() > 0) {
            recordData.addAll(localData);
        }
    }

    public void clearRecord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String dialogMsg = "是否删除历史记录?";
        String okStr = "确定";
        String cancleStr = "取消";
        builder.setMessage(dialogMsg);
        builder.setCancelable(false);
        builder.setNegativeButton(cancleStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(okStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (recordData.size() == 0) {
                    return;
                }
                recordData.clear();
                searchRecordDataDAO.clear();
                adapter.notifyDataSetChanged();
            }
        });
        builder.create();
        builder.show();
        builder = null;
    }
}