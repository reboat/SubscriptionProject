package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribeStore;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/7/17.
 */

public class DetailStore extends SubscribeStore implements DetailContract.Store {

    @Override
    public Flowable getFlowable(String url) {
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

    @Override
    public Flowable<DetailResponse.DataBean> getDetailResponse(final Object... params) {
        return Flowable.create(new FlowableOnSubscribe<DetailResponse.DataBean>() {
            @Override
            public void subscribe(@NonNull final FlowableEmitter<DetailResponse.DataBean> e) throws Exception {
                new APIGetTask<DetailResponse.DataBean>(new APICallBack<DetailResponse.DataBean>() {
                    @Override
                    public void onSuccess(DetailResponse.DataBean data) {
                        if(!e.isCancelled()){
                            e.onNext(data);
                            e.onComplete();
                        }
                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        super.onError(errMsg, errCode);
                        e.onError(new RxException(errMsg,errCode));
                    }
                }) {
                    @Override
                    protected void onSetupParams(Object... params) {
                        put("column_id", params[0]);
                    }

                    @Override
                    protected String getApi() {
                        return "/api/column/article_list";
                    }
                }.exe(params);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
