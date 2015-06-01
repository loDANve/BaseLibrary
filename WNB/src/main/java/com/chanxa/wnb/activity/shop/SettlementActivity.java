package com.chanxa.wnb.activity.shop;

import android.app.ProgressDialog;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.WelcomeActivity;
import com.chanxa.wnb.activity.WelcomepageActivity;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.onLineShop.Distribution;
import com.chanxa.wnb.bean.onLineShop.GoodsCart;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.ScrollGridView;
import com.chanxa.wnb.view.SquareRelativeLayout;
import com.chanxa.wnb.view.popwindow.DistributionWheelPopDelegate;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettlementActivity extends WnbBaseActivity {

    @InjectView(R.id.gridview_settlement)
    ScrollGridView mGridviewSettlement;
    @InjectView(R.id.tv_goodsCountMoney)
    TextView mTvGoodsCountMoney;
    @InjectView(R.id.tv_goodsFreight)
    TextView mTvGoodsFreight;
    @InjectView(R.id.btn_settlement_subOrder)
    Button mBtnSettlementSubOrder;
    @InjectView(R.id.container_shop_settlementAddress)
    RelativeLayout mContainerShopSettlementAddress;
    @InjectView(R.id.tv_goodsNumber)
    TextView mTvGoodsNumber;
    @InjectView(R.id.tv_Balance)
    TextView mTvBalance;
    @InjectView(R.id.tv_settlementName)
    TextView mTvSettlementName;
    @InjectView(R.id.tv_settlementPhone)
    TextView mTvSettlementPhone;
    @InjectView(R.id.tv_settlementAddress)
    TextView mTvSettlementAddress;
    @InjectView(R.id.tv_DeliveryType)
    TextView mTvDeliveryType;
    @InjectView(R.id.container_DeliveryType)
    RelativeLayout mContainerDeliveryType;
    @InjectView(R.id.tv_back)
    ImageView mTvBack;
    @InjectView(R.id.container_goodsDetailsTitle)
    RelativeLayout mContainerGoodsDetailsTitle;
    @InjectView(R.id.container_settlementData)
    LinearLayout mContainerSettlementData;
    @InjectView(R.id.img_oneGoods)
    ImageView mImgOneGoods;
    @InjectView(R.id.container_item_img_order)
    SquareRelativeLayout mContainerItemImgOrder;
    @InjectView(R.id.tv_oneGoodsName)
    TextView mTvOneGoodsName;
    @InjectView(R.id.tv_oneGoodsNumber)
    TextView mTvOneGoodsNumber;
    @InjectView(R.id.container_oneGoods)
    RelativeLayout mContainerOneGoods;
    @InjectView(R.id.tv_lable_goodsCountMoney)
    TextView mTvLableGoodsCountMoney;
    public static int EDITADDRESSCODE = 454;
    private GoodsCart goodsCart;
    private WBitmap wBitmap;
    private StoreService storeService = new StoreService();
    //配送
    private DistributionWheelPopDelegate distributionWheelPopDelegate;
    private ArrayList<Distribution> distributions = new ArrayList<>();
    private String orderXml;//订单xml数据体
    private double freightMoney = 0.00;//运费
    private String distributionTo = null;//配送至
    private String logisticsType = null;//物流方式
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        if (WnbApplication.getInstance().getCard() != null) {
            mTvBalance.setText("¥" + StringUtils.toDoubleStr(WnbApplication.getInstance().getCard().getBalance(),2));
        }
        mTvGoodsNumber.setText("共" + goodsCart.obtainGoodsNumber() + "件");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        mTvGoodsCountMoney.setText("¥" + StringUtils.toDoubleStr(numberFormat.format(goodsCart.getCountMoney())+"",2));
        findViewById(R.id.tv_back).setOnClickListener(this);
        mBtnSettlementSubOrder.setOnClickListener(this);
        mContainerShopSettlementAddress.setOnClickListener(this);
        if (goodsCart.getGoodsList().size() == 1) {
            Goods goods = goodsCart.getGoodsList().get(0);
            mGridviewSettlement.setVisibility(View.GONE);
            mContainerOneGoods.setVisibility(View.VISIBLE);
            String imgPath = goods.getImgPath();
            if (!StringUtils.isEmpty(imgPath)) {
                if (imgPath.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(mImgOneGoods, imgPath);
                } else {
                    wBitmap.display(mImgOneGoods, AppConfig.PATH_PREFIX + imgPath);
                }
            }
            mTvOneGoodsName.setText(goods.getName());
            mTvOneGoodsNumber.setText("x"+goods.getNumber());
        } else {
            mGridviewSettlement.setVisibility(View.VISIBLE);
            mContainerOneGoods.setVisibility(View.GONE);
            mGridviewSettlement.setAdapter(new SettlementAdapter());
            findViewById(R.id.container_settlementGoods).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettlementActivity.this, GoodsDetailsListActivity.class);
                    intent.putExtra("ArrayList<Goods>",goodsCart.getGoodsList());
                    startActivity(intent);
                }
            });
            mGridviewSettlement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SettlementActivity.this, GoodsDetailsListActivity.class);
                    intent.putExtra("ArrayList<Goods>",goodsCart.getGoodsList());
                    startActivity(intent);
                }
            });
        }
        initDistributionWheelPopDelegate();
        mContainerDeliveryType.setOnClickListener(this);

        Card card = WnbApplication.getInstance().getCard();
        if (card != null) {
            mTvSettlementName.setText(card.getName());
            mTvSettlementPhone.setText(card.getPhone());

        }
        super.initView();
    }

    private void initDistributionWheelPopDelegate() {
        distributionWheelPopDelegate = new DistributionWheelPopDelegate(this, distributions);
        distributionWheelPopDelegate.setOnButtonClick(new DistributionWheelPopDelegate.OnButtonClick() {
            @Override
            public void onPositivebtnClick(WheelView wheelView1, WheelView wheelView2, String curMoney) {
                freightMoney = StringUtils.toDouble(curMoney);
                mTvGoodsFreight.setText("¥" + curMoney);
                Distribution distribution = distributions.get(wheelView1.getSelectedItemPosition());
                logisticsType = distribution.getLogisticsArrayList().get(wheelView2.getSelectedItemPosition()).getType();
                distributionTo = distribution.getProvince() + distribution.getCity() + distribution.getDistrict();
                mTvDeliveryType.setText(distributions.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().get(wheelView2.getSelectedItemPosition()).getType());
            }

            @Override
            public void onNegativebtnClick(WheelView wheelView1, WheelView wheelView2, String curMoney) {
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        goodsCart = WnbApplication.getInstance().getGoodsCart();
        wBitmap = WnbApplication.getInstance().getwBitmap();
        CardToken cardToken = WnbApplication.getInstance().getCardToken();
        if (cardToken == null) {
            WnbApplication.getInstance().clearCache();
            WActivityManager.getInstance().finishAllActivity();
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }
        storeService.obtianLogisticsMoney(WnbApplication.getInstance().getCardToken(), goodsCart.getStoreMark(), new ServiceCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("获取配送信息失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                distributions.clear();
                distributions.addAll((Collection<? extends Distribution>) resultObj);
                initDistributionWheelPopDelegate();
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
        new CardService().readCard(cardToken, new ServiceCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                mTvBalance.setText("¥" + StringUtils.toDoubleStr(WnbApplication.getInstance().getCard().getBalance(),2));
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                mTvBalance.setText("¥" + StringUtils.toDoubleStr(WnbApplication.getInstance().getCard().getBalance(),2));
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                Card card = (Card) resultObj;
                WnbApplication.getInstance().setCard(card);
                mTvBalance.setText("¥" + StringUtils.toDoubleStr(card.getBalance(),2));
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_settlement_subOrder:
                //提交商品
                subOrder();
                return;
            case R.id.container_DeliveryType:
                distributionWheelPopDelegate.show();
                return;

            case R.id.container_shop_settlementAddress:
                Intent intent = new Intent(this, EditAddressActivity.class);
                intent.putExtra("name", mTvSettlementName.getText().toString());
                intent.putExtra("phone", mTvSettlementPhone.getText().toString());
                intent.putExtra("address", mTvSettlementAddress.getText().toString());
                startActivityForResult(intent, EDITADDRESSCODE);
                break;
        }
    }

    private void subOrder() {
        if (checkViewInput()) {
            //拼接orderXml体
            Element data = new BaseElement("DATA");
            data.addAttribute("收货人", mTvSettlementName.getText().toString());
            data.addAttribute("手机号", mTvSettlementPhone.getText().toString());
            data.addAttribute("邮编", "000000");//因后台暂时这么填
            data.addAttribute("收货地址", mTvSettlementAddress.getText().toString());
            data.addAttribute("消费总金额", goodsCart.getCountMoney() + freightMoney + "");
            data.addAttribute("物流方式", logisticsType);
            data.addAttribute("配送至", distributionTo);
            data.addAttribute("运费", freightMoney + "");
            data.addAttribute("邮费货到付款", 0 + "");
            data.addAttribute("备注", "null");
            data.addAttribute("获得总积分", "0");
            for (Goods goods : goodsCart.getGoodsList()) {
                Element childElement = new BaseElement("明细");
                childElement.addAttribute("商品", goods.getMark());
                childElement.addAttribute("数量", goods.getNumber() + "");
                childElement.addAttribute("获得积分", "0");
                childElement.addAttribute("消费金额", StringUtils.toDouble(goods.getMoney()) * goods.getNumber() + "");
                data.add(childElement);
            }
            orderXml = data.asXML();
            LogUtils.e(orderXml);
            //提交数据
            storeService.addNewGoodsOrder(WnbApplication.getInstance().getCardToken(), goodsCart.getStoreMark(), orderXml, new BaseCallBack() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onError(Exception e, int stateCode) {
                    ViewInject.toast("提交失败,请重试");
                    progressDialog.dismiss();
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    ViewInject.toast(errroMsg);
                    progressDialog.dismiss();
                }

                @Override
                public void onEXECSuccess(String result) {
                    ViewInject.toast("下单成功");
                    goodsCart.clrear();
                    progressDialog.dismiss();
                    startActivity(new Intent(SettlementActivity.this, OrderListActivity.class));
                    finish();
                }

                @Override
                public void onComplete(String result) {

                }
            });
        }
    }

    private boolean checkViewInput() {
        if (StringUtils.isEmpty(mTvSettlementName.getText().toString())) {
            ViewInject.toast("请输入收货人姓名");
            return false;
        }
        if (StringUtils.isEmpty(mTvSettlementPhone.getText().toString())) {
            ViewInject.toast("请输入收货人手机号");
            return false;
        }
        if (StringUtils.isEmpty(mTvSettlementAddress.getText().toString())) {
            ViewInject.toast("请输入收货地址");
            return false;
        }
        if (StringUtils.isEmpty(mTvDeliveryType.getText().toString())) {
            ViewInject.toast("请选择送货类型");
            return false;
        }
        if (StringUtils.toDouble(WnbApplication.getInstance().getCard().getBalance()) < goodsCart.getCountMoney()) {
            ViewInject.toast("余额不足,请先充值");
            return false;
        }
        return true;
    }

    class SettlementAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return goodsCart.getGoodsList().size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(SettlementActivity.this).inflate(R.layout.item_gridview_settlement, null, false);
                viewHolder = new ViewHolder();
                viewHolder.item_img_order = (ImageView) convertView.findViewById(R.id.item_img_order);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String imgPath = goodsCart.getGoodsList().get(position).getImgPath();
            if (!StringUtils.isEmpty(imgPath)) {
                if (imgPath.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(viewHolder.item_img_order, imgPath);
                } else {
                    wBitmap.display(viewHolder.item_img_order, AppConfig.PATH_PREFIX + imgPath);
                }
            }
            convertView.setTag(viewHolder);
            return convertView;
        }

        class ViewHolder {
            ImageView item_img_order;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITADDRESSCODE) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                String address = data.getStringExtra("address");
                mTvSettlementName.setText(name);
                mTvSettlementPhone.setText(phone);
                mTvSettlementAddress.setText(address);
            }
        }
    }
}
