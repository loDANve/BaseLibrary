package com.chanxa.wnb.activity.staff;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.popwindow.OneWheelPopDelegate;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffChangeDataActivity extends DefaultTitleActivity {

    @InjectView(R.id.edit_staffName)
    EditText mEditStaffName;
    @InjectView(R.id.edit_staffPersonId)
    EditText mEditStaffPersonId;
    @InjectView(R.id.edit_staffBankCardNumber)
    EditText mEditStaffBankCardNumber;
    @InjectView(R.id.edit_staffOpenBankName)
    EditText mEditStaffOpenBankName;
    @InjectView(R.id.tv_staffBlankName)
    TextView mTvStaffBlankName;
    @InjectView(R.id.container_staffBankName)
    RelativeLayout mContainerStaffBankName;
    @InjectView(R.id.btn_staff_changeData)
    Button mBtnStaffChangeData;

    private OneWheelPopDelegate blankPopDelegate;
    private String[] blankArr;
    private Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_change_data);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        mContainerStaffBankName.setOnClickListener(this);
        blankPopDelegate = new OneWheelPopDelegate(this, blankArr);
        blankPopDelegate.setOnButtonClick(new OneWheelPopDelegate.OnButtonClick() {
            @Override
            public void onPositivebtnClick(WheelView wheelView1) {
                mTvStaffBlankName.setText(blankArr[wheelView1.getSelectedItemPosition()]);
            }

            @Override
            public void onNegativebtnClick(WheelView wheelView1) {
            }
        });
        mBtnStaffChangeData.setOnClickListener(this);
        dataBind();
    }

    @Override
    public void initData() {
        super.initData();
        blankArr = getResources().getStringArray(R.array.bankNameArr);
        staff = WnbApplication.getInstance().getStaff();
    }

    private void dataBind() {
        if (staff == null) {
            return;
        }
        mEditStaffName.setText(staff.getName() + "");
        mEditStaffPersonId.setText(staff.getPersonID() + "");
        if (staff.getPersonIDIsOk().equals("1")) {//已经审核 不允许修改身份证了
            mEditStaffPersonId.setFocusable(false);
        }
        String bankNumber = staff.getBankNumber();
        if (StringUtils.isEmpty(bankNumber)) {
            return;
        }
        String[] arr = bankNumber.split(",");
        String bankNumberStr = arr[2];
        String openBankName = arr[0];
        String bankName = arr[1];
        mEditStaffOpenBankName.setText(openBankName + "");
        mTvStaffBlankName.setText(bankName + "");
        mEditStaffBankCardNumber.setText(bankNumberStr + "");
    }

    @Override
    public String initTitleText() {
        return "修改资料";
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.container_staffBankName:
                blankPopDelegate.show();
                break;
            case R.id.btn_staff_changeData:
                changeData();
                break;
        }
    }

    private Staff obtainStaffFromView() {
        Staff staff1 = staff.clone();
        //staff;
        staff1.setName(mEditStaffName.getText().toString() + "");
        String bankStr = obtainBankStrFromView();
        if (!StringUtils.isEmpty(bankStr)) {
            staff1.setBankNumber(bankStr);
        } else {
            return null;
        }
        staff1.setPersonID(mEditStaffPersonId.getText().toString() + "");
        return staff1;
    }

    private String obtainBankStrFromView() {
        String bankStr = "";

        String openBankName = mEditStaffOpenBankName.getText().toString();
        String bankCardNumber = mEditStaffBankCardNumber.getText().toString();
        String bankName = mTvStaffBlankName.getText().toString();
        if (StringUtils.isEmpty(openBankName)) {
            ViewInject.toast("开户名不能为空");
            return null;
        }
        if (StringUtils.isEmpty(bankCardNumber)) {
            ViewInject.toast("银行卡不能为空");
            return null;
        }
        if (StringUtils.isEmpty(bankName)) {
            ViewInject.toast("开户银行不能为空");
            return null;
        }
        bankStr += openBankName + ",";
        bankStr += bankName + ",";
        bankStr += bankCardNumber;
        return bankStr;
    }

    private void changeData() {
        final Staff staff = obtainStaffFromView();
        if (staff == null) {
            return;
        }
        if (!staff.verification()) {
            ViewInject.toast("请输入完整信息");
            return;
        }
        new StaffService().changeData(staff, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("修改失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    String newProof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    WnbApplication.getInstance().setStaff(staff);
                    WnbApplication.getInstance().getStaff().setProof(newProof);
                    ViewInject.toast("修改成功");
                } catch (DocumentException e) {
                    onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {
                ViewInject.toast(result);
            }
        });
    }
}
