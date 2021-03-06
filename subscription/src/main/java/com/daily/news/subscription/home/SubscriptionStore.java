package com.daily.news.subscription.home;

import com.daily.news.subscription.mock.MockResponse;
import com.daily.news.subscription.subscribe.SubscribeStore;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.APICallBack;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionStore extends SubscribeStore implements SubscriptionContract.Store<SubscriptionResponse> {
    @Override
    public Flowable<SubscriptionResponse> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<SubscriptionResponse>>() {
            @Override
            public Publisher<SubscriptionResponse> apply(@NonNull Long aLong) throws Exception {
                return Flowable.just(MockResponse.getInstance().getSubscriptionResponse());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<SubscriptionResponse> getRefreshFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<SubscriptionResponse>>() {
            @Override
            public Publisher<SubscriptionResponse> apply(@NonNull Long aLong) throws Exception {
                return Flowable.just(MockResponse.getInstance().getRefreshSubscriptionResponse());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public APIBaseTask getTask(APICallBack<SubscriptionResponse.DataBean> apiCallBack) {
        return  new APIGetTask<SubscriptionResponse.DataBean>(apiCallBack) {
            @Override
            protected void onSetupParams(Object... params) {
            }

            @Override
            protected String getApi() {
                return "/api/column/first_page_info";
            }
        }.setShortestTime(400);
    }
}
