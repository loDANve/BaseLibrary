package com.chanxa.wnb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.view.makeperson.MakePersonLinearLayout;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class MakePersonActivity extends WnbBaseActivity {
    private LinearLayout container_makePerson;
    private Button btn_makePerson_sub;
    private ArrayList<Goods> goodsArrayList = new ArrayList<>();
    private ArrayList<MakePersonLinearLayout> makePersonLinearLayouts = new ArrayList<>();

    private Goods goods = null;
    private String storeMark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_person);
    }

    @Override
    public void initView() {
        super.initView();
        container_makePerson = (LinearLayout) findViewById(R.id.container_makePerson);
        btn_makePerson_sub = (Button) findViewById(R.id.btn_makePerson_sub);
        btn_makePerson_sub.setOnClickListener(this);
        obtainMakePerson();
    }

    @Override
    public void initData() {
        storeMark = getIntent().getStringExtra("storeMark");
    }

    private void obtainMakePerson(){
        if (StringUtils.isEmpty(storeMark)) {
            return;
        }
        new StoreService().obtainGoods(getCardToken(), 0, 40,"", "", "线上预约", null, storeMark, new ServiceCallBack<ArrayList<Goods>>() {
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
                dataBind();
            }
            @Override
            public void onEXECSuccess(ArrayList<Goods> resultObj, int pageSize, int dataSize) {

            }
        });
    }

    private void dataBind() {
        if (goodsArrayList.size() < 1 || container_makePerson == null) {
            return;
        }
        MakePersonLinearLayout makePersonLinearLayout = new MakePersonLinearLayout(this);
        for (int i = 0; i < goodsArrayList.size(); i++) {
            LogUtils.e("i:" + i);
            Goods goods = goodsArrayList.get(i);
            if (!makePersonLinearLayout.addGoods(goods) || i == goodsArrayList.size() - 1) {
                makePersonLinearLayout.refreshView();
                container_makePerson.addView(makePersonLinearLayout);
                makePersonLinearLayouts.add(makePersonLinearLayout);
                makePersonLinearLayout.setOnMakePersonClickListener(new MakePersonLinearLayout.onMakePersonClickListener() {
                    @Override
                    public void onClick(MakePersonLinearLayout makePersonLinearLayout1, int position, Goods goods) {
                        for (MakePersonLinearLayout makePersonLinearLayout2 : makePersonLinearLayouts) {
                            if (makePersonLinearLayout2 == makePersonLinearLayout1) {
                                continue;
                            }
                            makePersonLinearLayout2.hiddenText();
                            makePersonLinearLayout2.clearSelect();
                        }
                        makePersonLinearLayout1.showText();
                        MakePersonActivity.this.goods = goods;
                    }
                });
                makePersonLinearLayout = new MakePersonLinearLayout(this);
                if (i == goodsArrayList.size() - 1) {
                    return;
                }
                i--;
            }
        }
//        container_makePerson.addView(makePersonLinearLayout);
//        makePersonLinearLayouts.add(makePersonLinearLayout);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btn_makePerson_sub) {
            if (this.goods == null) {
                ViewInject.toast("请选择一个人员");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(Goods.class.getSimpleName(), this.goods);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    public void backOnclick(View v) {
        if (R.id.btn_title_back == v.getId()) finish();
    }
}