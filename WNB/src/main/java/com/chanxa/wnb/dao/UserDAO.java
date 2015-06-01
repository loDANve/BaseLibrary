package com.chanxa.wnb.dao;

import android.content.Context;

import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.utils.encryption.ThreeDes;
import com.wtm.library.utils.SharedPreferenceService;
import com.wtm.library.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by CHANXA on 2014/12/20.
 * 保存用户账号密码
 */
public class UserDAO {

    public enum TAG {
        MEMBERTAG, STAFFTAG
    }

    public static boolean save(User user, TAG tag, Context context) throws Exception {
        SharedPreferenceService sharedPreferenceService = new SharedPreferenceService(context);
        HashMap map = new HashMap();
        map.put("CardNumber", user.getCardNumber());
        if (!StringUtils.isEmpty(user.getPwd())) {
            String pwd = ThreeDes.jiaMi(AppConfig._3DESKEY, user.getPwd());
            map.put("Pwd", pwd);
        } else {
            map.put("Pwd", "");
        }
        return sharedPreferenceService.saveToSharedPreference(tag.name(), map);
    }

    public static User read(TAG tag, Context context) throws Exception {
        User user = new User();
        SharedPreferenceService sharedPreferenceService = new SharedPreferenceService(context);
        HashMap map = sharedPreferenceService.readFromSharedPreference(tag.name());
        user.setCardNumber(map.get("CardNumber") + "");
        if (map.get("Pwd") != null && !(map.get("Pwd") + "").equals("")) {
            user.setPwd(ThreeDes.jieMi(AppConfig._3DESKEY, map.get("Pwd") + ""));
        }
        return user;
    }
}
