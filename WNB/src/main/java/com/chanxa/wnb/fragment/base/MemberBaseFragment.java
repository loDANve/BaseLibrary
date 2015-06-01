package com.chanxa.wnb.fragment.base;

import android.content.ComponentName;
import android.content.Intent;

import com.chanxa.wnb.activity.WelcomeActivity;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.base.WActivityManager;

/**
 * Created by CHANXA on 2015/1/16.
 */
public abstract class MemberBaseFragment extends BaseFragment {

    public CardToken getCardToken() {
        CardToken cardToken = WnbApplication.getInstance().getCardToken();
        if (cardToken == null) {
            /*WnbApplication.getInstance().clearCache();
            WActivityManager.getInstance().finishAllActivity();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            ComponentName cn = new ComponentName("com.chanxa.wnb", "com.chanxa.wnb.activity.WelcomeActivity");
            intent.setComponent(cn);
            startActivity(intent);*/
            reStartApp();
            return null;
        }
        return cardToken;
    }

    public Card getCard() {
        Card card = WnbApplication.getInstance().getCard();
        if (card == null) {
           /* WnbApplication.getInstance().clearCache();
            WActivityManager.getInstance().finishAllActivity();
            startActivity(new Intent(getActivity(), WelcomeActivity.class));
            getActivity().finish();*/
            reStartApp();
            return null;
        }
        return card;
    }

    public void reStartApp() {
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this);
        WnbApplication.getInstance().clearCache();
        WActivityManager.getInstance().finishAllActivity();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        ComponentName cn = new ComponentName("com.chanxa.wnb", "com.chanxa.wnb.activity.WelcomeActivity");
        intent.setComponent(cn);
        startActivity(intent);
    }
}
