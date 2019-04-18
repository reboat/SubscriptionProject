package com.daily.news;

import android.app.Application;

import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;

import cn.daily.news.biz.core.SettingBiz;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.network.DailyNetworkManager;


/**
 * Created by lixinke on 2017/9/26.
 */

public class SubscriptionApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.init(this);
        DailyNetworkManager.init(this);
        AppUtils.setChannel("bianfeng");
        SettingBiz.init();
        ThemeMode.init(this);
    }
}
