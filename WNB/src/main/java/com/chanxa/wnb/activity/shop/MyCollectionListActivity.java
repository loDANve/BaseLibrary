package com.chanxa.wnb.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.adapter.MyColltectionListAdapter;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.onLineShop.MyCollection;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.MemberService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.bitmap.PauseOnScrollListener;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import java.util.ArrayList;

public class MyCollectionListActivity extends WnbBaseActivity {

    private ListView listView_MyCollection;
    private MyColltectionListAdapter myColltectionListAdapter;
    private MyCollection myCollection;
    private MemberService memberService;
    private ArrayList<Goods> goodsArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection_list);
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView_MyCollection = (ListView) findViewById(R.id.listView_MyCollection);
        listView_MyCollection.setAdapter(myColltectionListAdapter);
        //listView_MyCollection.setOnScrollListener(new PauseOnScrollListener(WnbApplication.getInstance().getwBitmap(), false, true));
        listView_MyCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyCollectionListActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("goodsMark", goodsArrayList.get(position).getMark());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        myColltectionListAdapter = new MyColltectionListAdapter(goodsArrayList, this);
        myCollection = WnbApplication.getInstance().getCollection();
        if (myCollection == null) {
            obtainCollection();
        } else {
            obtainGoods();
        }
    }

    /**
     * 获取收藏
     */
    public void obtainCollection() {
        if (memberService == null) {
            memberService = new MemberService();
        }
        memberService.obtainCustomExpansionData(getCardToken(), AppConfig.Collection_KEY, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(stateCode + "e==>" + e);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.e(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                LogUtils.e(result);
                try {
                    String collStr = DATAxmlHelper.getAllAttribute(result).get("Value");
                    if (!StringUtils.isEmpty(collStr)) {
                        myCollection = new MyCollection(collStr);
                        WnbApplication.getInstance().setCollection(myCollection);
                        obtainGoods();
                    }
                } catch (DocumentException e) {
                    onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        });
    }

    public void obtainGoods() {
        if (myCollection == null) {
            return;
        }
        new StoreService().obtainGoods(getCardToken(), 0, myCollection.size(), myCollection.toString(), null, "线上订购", null, null, new ServiceCallBack<ArrayList<Goods>>() {
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
            public void onEXECSuccess(ArrayList<Goods> resultObj) {
                goodsArrayList.clear();
                goodsArrayList.addAll(resultObj);
                myColltectionListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj, int pageSize, int dataSize) {

            }
        });
    }
}