package com.daily.news;

import android.app.Application;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;

import cn.daily.news.analytics.AnalyticsManager;
import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.network.DailyNetworkManager;

/**
 * Created by lixinke on 2017/9/26.
 */

public class SubscriptionApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SettingManager.init(this);
        UIUtils.init(this);
//        ThemeMode.initTheme(R.style.AppTheme, R.style.AppThemeNight);
        UiModeManager.init(this, null);
        ThemeMode.init(this);
        ThemeMode.setUiMode(false);
        AppUtils.setChannel("update");
//        AppUtils.setChannel("bianfeng");

        AnalyticsManager.initAnalytics(this,"", null, null, null);

//        AdManager.getInstance().init(this);
//        AdManager.getInstance().setSspId(1);


        DailyNetworkManager.init(this);

        AppUtils.setChannel("bianfeng");

        //        开发环境
//        SettingManager.getInstance().setOpenHttps(false);
//        SettingManager.getInstance().setHost("10.100.62.206:8085");

        //正式
//        SettingManager.getInstance().setOpenHttps(true);
//        SettingManager.getInstance().setHost("api-new.8531.cn");
        //预发
        SettingManager.getInstance().setOpenHttps(true);
        SettingManager.getInstance().setHost("apiprev.8531.cn");

//        测试环境
//        SettingManager.getInstance().setOpenHttps(true);
//        SettingManager.getInstance().setHost("apibeta.8531.cn");
    }
}
