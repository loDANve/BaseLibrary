package com.chanxa.wnb.activity.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.chanxa.wnb.activity.WelcomeActivity;
import com.chanxa.wnb.activity.WelcomepageActivity;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.DataService;
import com.chanxa.wnb.utils.DateUtils;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.base.UncaughtHandler;
import com.wtm.library.base.WActivityManager;

/**
 * Created by CHANXA on 2014/12/31.
 */
public class WnbBaseActivity extends BaseActivity {

    public CardToken getCardToken() {
        if (!WnbApplication.getInstance().verificationData()) {
            reStartApp();
        }
        CardToken cardToken = WnbApplication.getInstance().getCardToken();
        return cardToken;
    }

    public Card getCard() {
        if (!WnbApplication.getInstance().verificationData()) {
            reStartApp();
        }
        Card card = WnbApplication.getInstance().getCard();
        return card;
    }

    public void reStartApp() {
        WnbApplication.getInstance().clearCache();
        WActivityManager.getInstance().finishAllActivity();
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }
}
