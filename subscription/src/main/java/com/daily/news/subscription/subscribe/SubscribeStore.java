package com.daily.news.subscription.subscribe;

import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.APICallBack;

import org.reactivestreams.Publisher;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/8/25.
 */

public class
SubscribeStore implements SubscribeContract.Store {
    @Override
    public Flowable getSubmitSubscribeFlowable(final ColumnResponse.DataBean.ColumnBean s) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<?>>() {
            @Override
            public Publisher<?> apply(@NonNull Long aLong) throws Exception {
                Random random = new Random();
                if (random.nextBoolean()) {
                    return Flowable.just(s);
                } else {
                    return Flowable.error(new Throwable("提交失败"));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public APIBaseTask getSubmitSubscribeTask(APICallBack<ColumnResponse.DataBean.ColumnBean> apiCallBack) {
        return new APIPostTask<ColumnResponse.DataBean.ColumnBean>(apiCallBack) {
            @Override
            protected void onSetupParams(Object... params) {
                put("column_id",params[0]);
                put("do_subscribe",params[1]);
            }

            @Override
            protected String getApi() {
                return "/api/column/subscribe_action";
            }
        };
    }
}
