package com.daily.news.subscription.detail;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.daily.news.subscription.subscribe.SubscribeContract;

import cn.daily.news.biz.core.network.compatible.APIBaseTask;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;
import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface DetailContract {
    interface Presenter extends BasePresenter, SubscribeContract.Presenter {
        void onRefresh(String uid);
    }

    interface View extends UIBaseView<Presenter>, SubscribeContract.View {
        void updateValue(DetailResponse dataBean);

        void onRefreshComplete();
    }

    interface Store extends BaseStore, SubscribeContract.Store {
        APIBaseTask getTask(APICallBack<DetailResponse.DataBean> callBack);

        Flowable<DetailResponse> getDetailResponse(LoadViewHolder progressBar, Object... params);
    }
}
