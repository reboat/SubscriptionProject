package com.daily.news;

import android.app.Application;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.SettingManager;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.ad.AdManager;

import cn.daily.news.analytics.AnalyticsManager;

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
        UiModeManager.init(this, R.styleable.SupportUiMode);
        ThemeMode.setUiMode(false);
        AppUtils.setChannel("update");
//        AppUtils.setChannel("bianfeng");

        AnalyticsManager.initAnalytics(this,"", null, null, null);

        AdManager.getInstance().init(this);
        AdManager.getInstance().setSspId(1);

        //        开发环境
//        SettingManager.getInstance().setOpenHttps(false);
//        SettingManager.getInstance().setHost("10.100.62.206:8085");

//        SettingManager.getInstance().setOpenHttps(true);
//        SettingManager.getInstance().setHost("api-new.8531.cn");
//        测试环境
        SettingManager.getInstance().setOpenHttps(true);
        SettingManager.getInstance().setHost("apibeta.8531.cn");
    }
}
