package com.chanxa.wnb.bean.bean.onLineShop;

import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/29.
 * 我的收藏
 */
public class MyCollection {

    private ArrayList<String> collectionList;

    public MyCollection() {
        collectionList = new ArrayList<>();
    }

    public boolean goodsIsCollection(String goodsMark) {
        return collectionList.contains(goodsMark);
    }

    public MyCollection(String collGoodsMarkStr) {
        parseColl(collGoodsMarkStr);
    }

    public boolean addOneCollection(String collGoodsMark) {
        if (collectionList.contains(collGoodsMark)) {
            return false;
        }
        return collectionList.add(collGoodsMark);
    }

    public boolean removeOneCollection(String collGoodsMark) {
        return collectionList.remove(collGoodsMark);
    }

    public ArrayList<String> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(ArrayList<String> collectionList) {
        this.collectionList = collectionList;
    }

    @Override
    public String toString() {
        StringBuilder toStr = new StringBuilder();
        for (int i = 0; i < collectionList.size(); i++) {
            if (i != 0) {
                toStr.append(",");
            }
            toStr.append(collectionList.get(i));
        }
        return toStr.toString();
    }

    private void parseColl(String coll) {
        if (collectionList == null) {
            collectionList = new ArrayList<>();
        }
        if (StringUtils.isEmpty(coll)) {
            return;
        }
        String[] strArr = coll.split(",");
        for (String tempStr : strArr) {
            collectionList.add(tempStr);
        }
    }

    public int size() {
        return collectionList.size();
    }
}
