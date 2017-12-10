package com.daily.news;

import android.app.Application;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.SettingManager;
import com.zjrb.core.utils.UIUtils;

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
        ThemeMode.initTheme(R.style.AppTheme, R.style.AppThemeNight);
        UiModeManager.init(this, R.styleable.SupportUiMode);
        ThemeMode.setUiMode(false);
        AppUtils.setChannel("bianfeng");

        AnalyticsManager.initAnalytics(this,"");
    }
}
