package com.daily.news.subscription.home;

import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;

/**
 * Created by lixinke on 2017/9/26.
 */

public class RecommendColumnFragment extends ColumnFragment {
    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
//        Toast.makeText(getContext(),bean.subscribed?"订阅成功":"取消订阅成功",Toast.LENGTH_SHORT).show();
    }
}
