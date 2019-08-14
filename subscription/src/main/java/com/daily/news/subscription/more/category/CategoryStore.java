package com.daily.news.subscription.more.category;


import cn.daily.news.biz.core.network.compatible.APIBaseTask;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APIGetTask;
import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public class CategoryStore implements CategoryContract.Store<CategoryResponse> {
    private static String MORE_URL = "";

    @Override
    public Flowable<CategoryResponse> getFlowable(String url) {
        return null;
    }

    @Override
    public String getUrl() {
        return MORE_URL;
    }

    @Override
    public APIBaseTask<CategoryResponse.DataBean> getTask(APICallBack<CategoryResponse.DataBean> apiCallBack) {
        return new APIGetTask<CategoryResponse.DataBean>(apiCallBack) {
            @Override
            public void onSetupParams(Object... params) {
                put("type", params[0]);
            }

            @Override
            public String getApi() {
                return "/api/subscription/class_list";
            }
        };
    }
}
