package com.chanxa.wnb.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.activity.staff.StaffMentionNowActivity;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.Profit;
import com.chanxa.wnb.bean.ProfitRecord;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RebateQueryActivity extends DefaultTitleActivity {

    @InjectView(R.id.tv_yesterdayIncome)
    TextView mTvYesterdayIncome;  //昨日收益

    @InjectView(R.id.tv_accountBalance)
    TextView mTvAccountBalance;   //账户余额

    @InjectView(R.id.tv_millionProfit)
    TextView mTvMillionProfit;    //万份收益

    @InjectView(R.id.tv_countProfit)
    TextView mTvCountProfit;      //累计收益

    @InjectView(R.id.tv_monthProfit)
    TextView mTvMonthProfit;      //月收益

    @InjectView(R.id.tv_yearProfit)
    TextView mTvYearProfit;       //年收益率

    @InjectView(R.id.tv_rebateBalance)
    TextView tv_rebateBalance;    //返利余额

    private CardService cardService;
    ProgressDialog progressDialog;

    private static String rebate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebate_query);
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.inject(this);
        double yearProfit = 0.00;
        //如果年收益率不为空,计算万份收益
        if (!StringUtils.isEmpty(getCard().getYearInterestRate())) {
            yearProfit = Double.parseDouble(getCard().getYearInterestRate());
            String millionProfit = "" + (10000 * yearProfit) / 365;
            int pointIndex = millionProfit.indexOf(".");
            if (pointIndex != -1 && millionProfit.length() > pointIndex + 3) {
                millionProfit = millionProfit.substring(0, pointIndex + 3);
            }
            mTvMillionProfit.setText(millionProfit);
        } else {
            mTvMillionProfit.setText("0");
        }
        mTvYearProfit.setText(StringUtils.toDoubleStr(String.valueOf(yearProfit*100),2) + "%");//设置年收益率
        //设置账户余额
        CardToken cardToken = WnbApplication.getInstance().getCardToken();
        if (cardToken != null) {
            cardService.readCard(cardToken,new ServiceCallBack() {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(Exception e, int stateCode) {
                    mTvAccountBalance.setText(getCard().getBalance());
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    mTvAccountBalance.setText(getCard().getBalance());
                }

                @Override
                public void onEXECSuccess(Object resultObj) {
                    Card card = (Card) resultObj;
                    WnbApplication.getInstance().setCard(card);
                    mTvAccountBalance.setText(card.getBalance());
                    tv_rebateBalance.setText(StringUtils.toDoubleStr((Integer.parseInt(card.getCanUseScore()) / 100) + "", 2));
                    rebate = card.getCanUseScore();
                }

                @Override
                public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

                }
            });
        }
        findViewById(R.id.container_mentionNow).setOnClickListener(this);
        findViewById(R.id.container_cashRebate).setOnClickListener(this);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
    }

    @Override
    public void initData() {
        super.initData();
        cardService = new CardService();
        queryProfit();
        queryYesterdayProfit();//查询昨日收益
    }

    /**
     * 查询昨日收益
     */
    private void queryYesterdayProfit() {
        cardService.obtainProfitDetails(getCardToken(), 0, 1, new ServiceCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                ArrayList<ProfitRecord> arrayList = (ArrayList) resultObj;
                ProfitRecord profitRecord = arrayList.get(0);
                mTvYesterdayIncome.setText(profitRecord.getYesterdayMoney());
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
    }

    /**
     * 查询收益
     */
    public void queryProfit() {
        cardService.obtainCardProfit(getCardToken(), new ServiceCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("查询收益失败");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                profit2View((Profit) resultObj);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
    }

    @Override
    public String initTitleText() {
        return getString(R.string.rebateQuery);
    }

    private void profit2View(Profit profit) {
        if (profit == null) {
            return;
        }
        if (!StringUtils.isEmpty(profit.getCountProfit())) {//设置总收益
            mTvCountProfit.setText(profit.getCountProfit());
        } else {
            mTvCountProfit.setText("0");
        }
        if (!StringUtils.isEmpty(profit.getLastMonthProfit())) {//设置月收益
            mTvMonthProfit.setText(profit.getLastMonthProfit());
        } else {
            mTvMonthProfit.setText("0");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.container_mentionNow) {
            startActivity(new Intent(this, MentionNowActivity.class));
        }else if (v.getId() == R.id.container_cashRebate){
            Intent intent = new Intent(this, CashRebateActivity.class);
            intent.putExtra("rebate", rebate);
            startActivity(intent);
        }
    }
}