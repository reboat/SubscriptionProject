package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribeStore;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public class DetailStore extends SubscribeStore implements DetailContract.Store {

    public interface DetailService {
//        @GET("/api/column/article_list")
//        Flowable<DetailColumn> getDetail(@Query("column_id") String column_id, @Query("size") int size);
    }

    @Override
    public Flowable getFlowable(String url) {

//        return Flowable.timer(400, TimeUnit.MILLISECONDS)
//                .flatMap(new Function<Long, Publisher<DetailColumn>>() {
//                    @Override
//                    public Publisher<DetailResponse.DataBean.DetailBean> apply(@NonNull Long aLong) throws Exception {
//                        return Flowable.just(MockResponse.getInstance().getDetail(""));
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread());
        return null;
    }

    @Override
    public APIBaseTask<DetailResponse.DataBean> getTask(APICallBack callBack) {
        return new APIGetTask<DetailResponse.DataBean>(callBack) {
            @Override
            protected void onSetupParams(Object... params) {
                put("column_id", params[0]);
            }

            @Override
            protected String getApi() {
                return "/api/column/article_list";
            }
        };
    }
}
