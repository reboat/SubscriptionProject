package com.daily.news.subscription.more.category;

import com.daily.news.subscription.mock.MockResponse;
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
 * Created by lixinke on 2017/7/17.
 */

public class CategoryStore implements CategoryContract.Store<CategoryResponse> {
    private static String MORE_URL = "";

    @Override
    public Flowable<CategoryResponse> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<CategoryResponse>>() {
                    @Override
                    public Publisher<CategoryResponse> apply(@NonNull Long aLong) throws Exception {
                        CategoryResponse response = MockResponse.getInstance().getCategoryResponse();
                        return Flowable.just(response);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    @Override
    public String getUrl() {
        return MORE_URL;
    }

    @Override
    public APIBaseTask<CategoryResponse.DataBean> getTask(APICallBack<CategoryResponse.DataBean> apiCallBack) {
        return new APIGetTask<CategoryResponse.DataBean>(apiCallBack) {
            @Override
            protected void onSetupParams(Object... params) {

                put("type", params[0]);
            }

            @Override
            protected String getApi() {
                return "/api/red_boat/class_list";
            }
        };
    }
}
