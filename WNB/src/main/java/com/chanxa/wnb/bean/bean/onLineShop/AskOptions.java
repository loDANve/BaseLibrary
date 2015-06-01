package com.chanxa.wnb.bean.bean.onLineShop;

import java.io.Serializable;

/**
 * Created by CHANXA on 2015/1/21.
 * 问题的选项
 */
public class  AskOptions implements Serializable {

    private String question_id;
    private String name;
    private String content;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
