package com.daily.news.subscription.more.column;

import android.text.TextUtils;

import com.daily.news.subscription.subscribe.SubscribeStore;

import java.util.List;

import cn.daily.news.biz.core.network.compatible.APIBaseTask;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APIGetTask;
import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/21.
 */

public class LocalColumnStore extends SubscribeStore implements ColumnContract.Store {
    private List<ColumnResponse.DataBean.ColumnBean> mColumns;
    private String className;
    private long start = -1;
    private int mType;

    public LocalColumnStore(List<ColumnResponse.DataBean.ColumnBean> columns) {
        mColumns = columns;
    }

    public LocalColumnStore(List<ColumnResponse.DataBean.ColumnBean> columns, String className, long start, boolean has_more, int type) {
        mColumns = columns;
        this.className = className;
        this.start = start;
        mType = type;
    }

    @Override
    public Flowable getFlowable(String url) {
        return Flowable.just(mColumns);
    }


    @Override
    public APIBaseTask getTask(APICallBack apiCallBack) {
        if (mColumns == null && !TextUtils.isEmpty(className)) {
            return new APIGetTask<ColumnResponse.DataBean>(apiCallBack) {
                @Override
                public void onSetupParams(Object... params) {
                    put("class_name", className);
                    put("type", mType);
                    if (start != -1) {
                        put("start", start);
                    }
                }

                @Override
                public String getApi() {
                    return "/api/subscription/column_list";
                }
            };

        } else {
            ColumnResponse.DataBean dataBean = new ColumnResponse.DataBean();
            dataBean.elements = mColumns;
            apiCallBack.onSuccess(dataBean);
            return null;
        }
    }
}
