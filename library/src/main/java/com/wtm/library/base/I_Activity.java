package com.wtm.library.base;

import java.util.Objects;

/**
 * Created by wtm on 2014/12/6.
 */
public interface I_Activity {
    /**init**/
    void initView();
    void initData();

    /**BroadcastReceiver**/
    void regReceiver();
    void unRegReceiver();

}
