package com.daily.news;

import android.app.Application;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.utils.SettingManager;
import com.zjrb.core.utils.UIUtils;

/**
 * Created by lixinke on 2017/9/26.
 */

public class SubscriptionApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SettingManager.init(this);
        UIUtils.init(this);
        ThemeMode.initTheme(R.style.AppTheme, R.style.NightAppTheme);
        UiModeManager.init(this, R.styleable.SupportUiMode);
        ThemeMode.setUiMode(true);
    }
}
