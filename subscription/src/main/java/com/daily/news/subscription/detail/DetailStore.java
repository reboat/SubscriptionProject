package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribeStore;

import cn.daily.news.biz.core.network.compatible.APIBaseTask;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APIGetTask;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;
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
            public void onSetupParams(Object... params) {
                put("column_id", params[0]);
            }

            @Override
            public String getApi() {
                return "/api/column/article_list";
            }
        };
    }

    @Override
    public Flowable<DetailResponse> getDetailResponse(final LoadViewHolder progressBar, final Object... params) {
        return Flowable.create(new FlowableOnSubscribe<DetailResponse>() {
            @Override
            public void subscribe(@NonNull final FlowableEmitter<DetailResponse> e) throws Exception {
                new APIGetTask<DetailResponse.DataBean>(new APICallBack<DetailResponse.DataBean>() {
                    @Override
                    public void onSuccess(DetailResponse.DataBean data) {
                        if (!e.isCancelled()) {
                            DetailResponse response = new DetailResponse();
                            response.code = 200;
                            response.data = data;
                            e.onNext(response);
                            e.onComplete();
                        }
                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        super.onError(errMsg, errCode);
                        // FlowableEmitter 调用 onError和onComplete时，会被cancel。
                        if (!e.isCancelled()) {
                            DetailResponse response = new DetailResponse();
                            response.code = errCode;
                            response.message = errMsg;
                            e.onNext(response);
                        }
                    }
                }) {
                    @Override
                    public void onSetupParams(Object... params) {
                        put("column_id", params[0]);
                    }

                    @Override
                    public String getApi() {
                        return "/api/column/article_list";
                    }
                }.bindLoadViewHolder(progressBar).exe(params);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
