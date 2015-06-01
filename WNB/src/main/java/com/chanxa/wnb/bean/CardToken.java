package com.chanxa.wnb.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.wtm.library.utils.StringUtils;

/**
 * 令牌
 */
public class CardToken extends Model {

	private String token;// 令牌
	private String checkCode;// 校验码
	private String mark;// 卡标识

	public CardToken(String token, String checkCode, String mark) {
		super();
		this.token = token;
		this.checkCode = checkCode;
		this.mark = mark;
	}

    public boolean verification() {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        if (StringUtils.isEmpty(checkCode)) {
            return false;
        }
        if (StringUtils.isEmpty(mark)) {
            return false;
        }
        return true;
    }


	public CardToken() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
