package com.daily.news.subscription.widget;

import com.daily.news.subscription.R;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.news.other.Utils;

public class SubscriptionDivider extends ListSpaceDivider {
    public SubscriptionDivider(float leftMarginDp,float rightMarginDp) {
        super(0.5, R.color._eeeeee, true);
        int left = Utils.dp2px(UIUtils.getContext(),leftMarginDp);
        int right = Utils.dp2px(UIUtils.getContext(),rightMarginDp);
        mLeftMargin = left;
        mRightMargin = right;
    }
}