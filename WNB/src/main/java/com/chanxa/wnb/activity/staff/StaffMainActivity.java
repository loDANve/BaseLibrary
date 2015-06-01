package com.chanxa.wnb.activity.staff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.UserLoginActivity;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.bean.StaffStoreGoods;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.GoodsTypeXmlParser;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;

import org.dom4j.DocumentException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffMainActivity extends BaseActivity {


    @InjectView(R.id.container_staff_userData)
    LinearLayout mContainerStaffUserData;
    @InjectView(R.id.container_staff_have_money_query)
    LinearLayout mContainerStaffHaveMoneyQuery;
    @InjectView(R.id.container_staff_deal_record)
    LinearLayout mContainerDealRecord;
    @InjectView(R.id.container_staff_mention_now)
    LinearLayout mContainerMentionNow;
    @InjectView(R.id.container_staff_upLoadGoods)
    LinearLayout mContainerUpLoadGoods;
    @InjectView(R.id.container_staff_manage_order)
    LinearLayout mContainerManageOrder;
    @InjectView(R.id.container_staff_store_maintenance)
    LinearLayout mContainerStoreMaintenance;
    /*@InjectView(R.id.btn_reg)
    Button mBtnReg;*/

    ProgressDialog progressDialog;
    private Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
    }

    @Override
    public void initData() {
        super.initData();
        staff = WnbApplication.getInstance().getStaff();
        if (staff != null && staff.isCanUpLoadGoods()) {
            initStoreTypeData();
        }
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        super.initView();
        mContainerStaffUserData.setOnClickListener(this);
        mContainerStaffHaveMoneyQuery.setOnClickListener(this);
        mContainerDealRecord.setOnClickListener(this);
        mContainerMentionNow.setOnClickListener(this);
        mContainerUpLoadGoods.setOnClickListener(this);
        mContainerManageOrder.setOnClickListener(this);
        mContainerStoreMaintenance.setOnClickListener(this);
        //mBtnReg.setOnClickListener(this);
        if (staff != null) {
            if (!staff.isCanUpLoadGoods()) {
                mContainerUpLoadGoods.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.container_staff_userData:          //个人信息
                intent.setClass(this, StaffDataActivity.class);
                break;
            case R.id.container_staff_have_money_query:  //余额查询
                intent.setClass(this, StaffBalanceQueryActivity.class);
                break;
            case R.id.container_staff_deal_record:       //交易记录
                intent.setClass(this, StaffLookRecordActivity.class);
                break;
            case R.id.container_staff_mention_now:       //申请提现
                intent.setClass(this, StaffMentionNowActivity.class);
                break;
            case R.id.container_staff_upLoadGoods:       //商品管理
                intent.setClass(this, StaffGoodsManageActivity.class);
//              intent.setClass(this, StaffUpLoadGoodsActivity.class);
                break;
            case R.id.container_staff_manage_order:      //订单管理
                intent.setClass(this, StaffManageOrderActivity.class);
                break;
            case R.id.container_staff_store_maintenance: //店面维护
                intent.setClass(this,StaffStoreMaintenance.class);
                break;
            /*case R.id.btn_reg:
                ViewInject.toast("退出登录");
                WnbApplication.getInstance().clearCache();
                WActivityManager.getInstance().finishAllActivity();
                startActivity(new Intent(this, UserLoginActivity.class));
                return;*/
        }
        startActivity(intent);
    }

    private void initStoreTypeData() {
        if (!staff.verification()) {
            return;
        }
        new StaffService().obtainStoreGoodsTypes(staff, new BaseCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("获取员工店面信息失败");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    String newProof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    WnbApplication.getInstance().getStaff().setProof(newProof);
                    WnbApplication.getInstance().setStaffStoreGoodsLV1ArrayList(new GoodsTypeXmlParser().parserStaffStoreGoodsLV1(result));
                    progressDialog.dismiss();
                } catch (Exception e) {
                    onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        });
    }
}
