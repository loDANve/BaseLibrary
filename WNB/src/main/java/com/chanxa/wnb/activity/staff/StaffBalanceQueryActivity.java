package com.chanxa.wnb.activity.staff;

import android.os.Bundle;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.dao.UserDAO;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StaffService;
import com.wtm.library.utils.StringUtils;

public class StaffBalanceQueryActivity extends DefaultTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_balance_query);
    }

    @Override
    public void initView() {
        super.initView();
        balanceViewInit();
        try {
            User staffUser = UserDAO.read(UserDAO.TAG.STAFFTAG, this);
            if (staffUser != null) {
                String name = staffUser.getCardNumber();
                String pwd = staffUser.getPwd();
                if (StringUtils.isEmpty(name) ||StringUtils.isEmpty(pwd)) {
                    return;
                }
                new StaffService().login(name, pwd,new ServiceCallBack() {
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
                    public void onEXECSuccess(Object resultObj) {
                        Staff staff = (Staff) resultObj;
                        WnbApplication.getInstance().setStaff(staff);
                        balanceViewInit();
                    }
                    @Override
                    public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

                    }
                });
            }
        } catch (Exception e) {
        }
    }

    private void balanceViewInit() {
        if (WnbApplication.getInstance().getStaff() != null) {
            float float1 = Float.parseFloat(WnbApplication.getInstance().getStaff().getAccountBalance());
            float1 =  (float)(Math.round(float1*100))/100;
            ((TextView)(findViewById(R.id.tv_staff_Balance))).setText(float1+"");
        }
    }
    @Override
    public String initTitleText() {
        return "余额查询";
    }
}
