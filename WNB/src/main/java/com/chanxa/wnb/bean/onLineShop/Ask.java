package com.chanxa.wnb.bean.onLineShop;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/21.
 * 问题
 */
public class Ask implements Serializable {
    private String id;
    private String name;//问题名字
    private String answer;//答案名
    private ArrayList<AskOptions> askOptionsArrayList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<AskOptions> getAskOptionsArrayList() {
        return askOptionsArrayList;
    }

    public void setAskOptionsArrayList(ArrayList<AskOptions> askOptionsArrayList) {
        this.askOptionsArrayList = askOptionsArrayList;
    }
}
