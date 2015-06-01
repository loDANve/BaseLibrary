package com.chanxa.wnb.bean.bean.onLineShop;

import com.chanxa.wnb.bean.bean.Goods;
import com.chanxa.wnb.observer.ConcreteSubject;
import com.chanxa.wnb.observer.Observer;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;

/**
 * 购物车ﳵ
 *
 * @author wtm
 */
public class GoodsCart implements ConcreteSubject {
    private ArrayList<Observer> observersList;
    private ArrayList<Goods> goodsList;
    private double countMoney = 0;
    private String storeMark=null;
    public GoodsCart() {
        goodsList = new ArrayList<Goods>();
        observersList = new ArrayList<Observer>();
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<Goods> goodsList) {
        this.goodsList = goodsList;
        notifyDataChanged();
    }

    public double getCountMoney() {
        this.countMoney = 0;
        if (goodsList != null && goodsList.size() != 0) {
            for (Goods goodsTemp : goodsList) {
                double goodsTempCountMoney = Double.parseDouble(goodsTemp.getMoney())
                        * goodsTemp.getNumber();
                this.countMoney += goodsTempCountMoney;
            }
        }
        LogUtils.e(countMoney+"");
        return countMoney;
    }

    public boolean addGoods2Cart(Goods goods) {
        if (getStoreMark() == null) {
            storeMark = goods.getStoreMark();
        } else {
            if (!getStoreMark().equals(goods.getStoreMark())) {
                return false;
            }
        }
        if (!isContains(goods)) {
            goodsList.add(goods);
            goods.save();
            LogUtils.e("保存:" + goods.getName());
            notifyDataChanged();
        } else {
            Goods goods2 = goodsList.get(indexOf(goods));
            goods2.setNumber(goods2.getNumber() + 1);
            goods2.save();
            LogUtils.e("保存:" + goods.getName());
            notifyDataChanged();
        }
        return true;
    }

    private int indexOf(Goods goods){
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).getMark().equals(goods.getMark()) || goodsList.get(i).getMark() == goods.getMark()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isContains(Goods goods) {
        for (Goods tempGoods : goodsList) {
            if (tempGoods.getMark().equals(goods.getMark()) || tempGoods.getMark() == goods.getMark()) {
                return true;
            }
        }
        return false;
    }

    public int obtainGoodsNumber(){
        int count = 0;
        for (Goods tempGoods : goodsList) {
            count += tempGoods.getNumber();
        }
        return count;
    }

    public void removeGoods4Car2(Goods goods) {
        if (isContains(goods)) {
            goodsList.remove(goods);
            goods.delete();
            notifyDataChanged();
        }
    }

    public void setCountMoney(float countMoney) {
        this.countMoney = countMoney;
        notifyDataChanged();
    }

    @Override
    public void addObserver(Observer observer) {
        observersList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observersList.contains(observer)) {
            observersList.remove(observer);
        }
    }

    @Override
    public void notifyDataChanged() {
        for (Observer observer : observersList) {
            observer.onConcreteSubjectChange(this);
        }
    }

    public void clrear() {
        for (Goods goods : goodsList) {
            goods.delete();
        }
        goodsList.clear();
        notifyDataChanged();
    }

    public String getStoreMark() {
        if (goodsList.size() > 0) {
            return goodsList.get(0).getStoreMark();
        }
        return null;
    }

    public void setStoreMark(String storeMark) {
        this.storeMark = storeMark;
    }
}
