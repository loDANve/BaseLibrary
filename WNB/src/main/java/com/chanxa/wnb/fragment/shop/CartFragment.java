package com.chanxa.wnb.fragment.shop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.shop.SettlementActivity;
import com.chanxa.wnb.adapter.ShopCartAdapter;
import com.chanxa.wnb.bean.onLineShop.GoodsCart;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.observer.ConcreteSubject;
import com.chanxa.wnb.observer.Observer;
import com.chanxa.wnb.view.swipemenulistview.SwipeMenu;
import com.chanxa.wnb.view.swipemenulistview.SwipeMenuCreator;
import com.chanxa.wnb.view.swipemenulistview.SwipeMenuItem;
import com.chanxa.wnb.view.swipemenulistview.SwipeMenuListView;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.StringUtils;

import java.text.NumberFormat;

/**
 * Created by CHANXA on 2015/1/9.
 */
public class CartFragment extends BaseFragment implements Observer {
    private SwipeMenuListView listview_shopCart;
    private TextView tv_cartCountMoney;
    private GoodsCart goodsCart;
    private ShopCartAdapter adapter;
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        goodsCart = WnbApplication.getInstance().getGoodsCart();
        goodsCart.addObserver(this);
        return inflater.inflate(R.layout.fragment_cart, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        //back btn
        rootView.findViewById(R.id.container_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rootView.findViewById(R.id.btn_shopCartSettlement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settlement();
            }
        });
        tv_cartCountMoney = (TextView) rootView.findViewById(R.id.tv_cartCountMoney);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        tv_cartCountMoney.setText("￥"+StringUtils.toDoubleStr(numberFormat.format(goodsCart.getCountMoney()),2));
        listview_shopCart = (SwipeMenuListView) rootView.findViewById(R.id.listview_shopCart);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(DeviceUtils.dip2px(getActivity(), 90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        listview_shopCart.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                goodsCart.removeGoods4Car2(goodsCart.getGoodsList().get(position));
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        listview_shopCart.setCloseInterpolator(new BounceInterpolator());
        listview_shopCart.setMenuCreator(creator);
        adapter = new ShopCartAdapter(getActivity(), WnbApplication.getInstance().getGoodsCart().getGoodsList());
        listview_shopCart.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        WnbApplication.getInstance().getGoodsCart().removeObserver(this);
        super.onDestroy();
    }

    private void settlement() {
        if (goodsCart.getGoodsList().size() > 0) {
            startActivity(new Intent(getActivity(), SettlementActivity.class));
        }
    }

    @Override
    public void onConcreteSubjectChange(ConcreteSubject obj) {
        adapter.notifyDataSetChanged();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        tv_cartCountMoney.setText("¥" + StringUtils.toDoubleStr(numberFormat.format(goodsCart.getCountMoney()),2));
    }
}