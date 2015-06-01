package com.chanxa.wnb.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.Bank;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.RechargeProducts;
import com.chanxa.wnb.bean.Segment;
import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.bean.onLineShop.Ask;
import com.chanxa.wnb.dao.AskDao;
import com.chanxa.wnb.dao.BankDao;
import com.chanxa.wnb.dao.UserDAO;
import com.chanxa.wnb.service.RechargeRateService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class PayPhoneBillActivity extends DefaultTitleActivity {

    private TextView tv_recharge_amount, tv_ownershipOfLand, tv_recharge_products;
    private EditText et_import_phoneNum, et_import_pwd;
    private Button btn_immediately_recharge, btn_clear_phone;

    private ArrayList<Segment> segmentArrayList; //充值手机号码信息
    private ArrayList<RechargeProducts> rechargeProductsArrayList = new ArrayList<RechargeProducts>(); //充值手机-话费充值产品信息

    private RechargeRateService rechargeRateService = new RechargeRateService();

    private ProgressDialog progressBar; //菊花

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_phonebill);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.pay_phonebill);
    }

    @Override
    public void initView() {
        super.initView();
        btn_clear_phone = (Button) findViewById(R.id.btn_clear_phone);
        tv_recharge_amount = (TextView) findViewById(R.id.tv_recharge_amount);
        tv_ownershipOfLand = (TextView) findViewById(R.id.tv_ownershipOfLand);
        tv_recharge_products = (TextView) findViewById(R.id.tv_recharge_products);
        et_import_pwd = (EditText) findViewById(R.id.et_import_pwd);
        et_import_phoneNum = (EditText) findViewById(R.id.et_import_phoneNum);
        et_import_phoneNum.setText(getCard().getPhone().toString());
        checkSegment(getCard().getPhone().toString());
        et_import_phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String phoneNum = et_import_phoneNum.getText().toString();
                if (phoneNum.length() != 11) {
                    return;
                }
                Pattern phonePattern = Pattern
                        .compile("^1[3|4|5|7|8]\\d{9}$");
                if (!phonePattern.matcher(phoneNum).matches()) {
                    ViewInject.toast("请输入正确的手机号码");
                    return;
                }
                checkSegment(phoneNum);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String phoneNum = et_import_phoneNum.getText().toString();
                if (phoneNum.length() > 0) {
                    btn_clear_phone.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_phone.setVisibility(View.INVISIBLE);
                }
                if (phoneNum.length() < 11 || phoneNum.length() < 1) {
                    tv_ownershipOfLand.setText("");
                    tv_recharge_products.setText("");
                    rechargeProductsArrayList.clear();
                }
            }
        });
        btn_immediately_recharge = (Button) findViewById(R.id.btn_immediately_recharge);
        //findViewById(R.id.container_setUpProtectionVerification).setOnClickListener(this);
        findViewById(R.id.container_recharge_amount).setOnClickListener(this);
        btn_immediately_recharge.setOnClickListener(this);
        btn_clear_phone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_recharge_amount:
                rechargeAmount();
                break;
            case R.id.btn_immediately_recharge:
                immediateRecharge();
                break;
            case R.id.btn_clear_phone:
                et_import_phoneNum.setText("");
                break;
        }
    }

    /**
     * 查询号码归属地
     *
     * @param phoneNum
     */
    private void checkSegment(String phoneNum) {
        rechargeRateService.querySegment(getCardToken(), phoneNum, new ServiceCallBack() {
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
                        segmentArrayList = (ArrayList<Segment>) resultObj;
                        String text = segmentArrayList.get(0).getProvince() + segmentArrayList.get(0).getCarrieroperator();
                        tv_ownershipOfLand.setText(text);
                        setRechargeProducts();
                    }

                    @Override
                    public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

                    }
                }
        );
    }

    /**
     * 选择充值金额
     */
    private void rechargeAmount() {
        if (rechargeProductsArrayList.isEmpty()) {
            return;
        }
        if (et_import_phoneNum.getText().toString().length() != 11) {
            ViewInject.toast("手机号码不对");
            return;
        }
        final DecimalFormat df = new DecimalFormat("#.00");
        String[] denominations = new String[rechargeProductsArrayList.size()];
        for (int i = 0; i < rechargeProductsArrayList.size(); i++) {
            denominations[i] = rechargeProductsArrayList.get(i).getDenomination() + "元　(优惠价:" + df.format(Double.parseDouble(rechargeProductsArrayList.get(i).getSellingPrice())) + "元)";
        }

        new AlertDialog.Builder(this)
                .setTitle("充值金额")
                        //.setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(denominations, 0,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                tv_recharge_amount.setText(rechargeProductsArrayList.get(which).getDenomination());
                                String text = "优惠价:　" +df.format(Double.parseDouble(rechargeProductsArrayList.get(which).getSellingPrice()))  + "元";
                                tv_recharge_products.setText(text);
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton("取消", null)
                .show();
    }

    private void setRechargeProducts() {
        rechargeRateService.getRechargeProducts(getCardToken(), segmentArrayList.get(0).getCarrieroperator(),
                segmentArrayList.get(0).getProvince(),
                new ServiceCallBack() {
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
                        rechargeProductsArrayList = (ArrayList<RechargeProducts>) resultObj;
                        for (int i = 0; i < rechargeProductsArrayList.size(); i++) {
                            if (tv_recharge_amount.getText().toString().equals(rechargeProductsArrayList.get(i).getDenomination())) {
                                DecimalFormat df = new DecimalFormat("#.00");
                                String text = "优惠价:　" +df.format(Double.parseDouble(rechargeProductsArrayList.get(i).getSellingPrice()))  + "元";
                                tv_recharge_products.setText(text);
                            }
                        }
                        compareProductsList();
                    }

                    @Override
                    public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

                    }
                });
    }

    private void immediateRecharge() {
        String phoneStr = et_import_phoneNum.getText().toString();
        if (phoneStr.length() != 11){
            ViewInject.toast("请输入手机号");
            return;
        }

        if (StringUtils.isEmpty(et_import_pwd.getText().toString())) {
            ViewInject.toast("请输入密码");
            return;
        }

        User user = null;
        String pwd = null;
        try {
            user = UserDAO.read(UserDAO.TAG.MEMBERTAG, this);
            pwd = user.getPwd();
        } catch (Exception e) {
            ViewInject.toast("获取密码失败");
            return;
        }

        if (!pwd.equals(et_import_pwd.getText().toString())) {
            ViewInject.toast("密码错误");
            return;
        }

        String balance = subZeroAndDot(getCard().getBalance()); //账户余额

        int recharge_amount = Integer.parseInt(tv_recharge_amount.getText().toString());  //充值面额
        if (Double.parseDouble(balance) < recharge_amount) {
            ViewInject.toast("没有足够的余额");
            return;
        }

        rechargeRateService.rechargeOrder(getCardToken(), phoneStr, pwd,
                tv_recharge_amount.getText().toString(), new BaseCallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(Exception e, int stateCode) {
                        ViewInject.toast("充值失败");
                    }

                    @Override
                    public void onEXECisFalse(String errroMsg) {
                        ViewInject.toast("充值失败");
                    }

                    @Override
                    public void onEXECSuccess(String result) {
                        ViewInject.toast("充值成功，10分钟内到账");
                        finish();
                    }

                    @Override
                    public void onComplete(String result) {
                    }
                });
    }

    /**
     * 将从服务器获得的充值产品按面额排序
     */
    private void compareProductsList() {
        Comparator<RechargeProducts> comparator = new Comparator<RechargeProducts>() {
            public int compare(RechargeProducts s1, RechargeProducts s2) {
                //按照面额排序
                if (s1.getDenomination() != s2.getDenomination()) {
                    return Integer.parseInt(s1.getDenomination()) - Integer.parseInt(s2.getDenomination());
                } else {
                    return Integer.parseInt(s1.getDenomination());
                }
            }
        };
        Collections.sort(this.rechargeProductsArrayList, comparator);
    }

    public String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
