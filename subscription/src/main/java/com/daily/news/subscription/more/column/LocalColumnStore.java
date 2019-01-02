package com.daily.news.subscription.more.column;

import com.daily.news.subscription.subscribe.SubscribeStore;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.APICallBack;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/21.
 */

public class LocalColumnStore extends SubscribeStore implements ColumnContract.Store {
    private List<ColumnResponse.DataBean.ColumnBean> mColumns;
    private int type;
    private long id;
    private boolean has_more;

    public LocalColumnStore(List<ColumnResponse.DataBean.ColumnBean> columns) {
        mColumns = columns;
    }
    public LocalColumnStore(List<ColumnResponse.DataBean.ColumnBean> columns, int type, long id, boolean has_more) {
        mColumns = columns;
        this.type = type;
        this.id = id;
        this.has_more = has_more;
    }

    @Override
    public Flowable getFlowable(String url) {
        return Flowable.just(mColumns);
    }


    @Override
    public APIBaseTask getTask(APICallBack apiCallBack) {
        if(mColumns == null && type != 0) {
            return new APIGetTask<ColumnResponse.DataBean>(apiCallBack ) {
                @Override
                protected void onSetupParams(Object... params) {
                    put("type", type);
                    put("class_id", id);
                }

                @Override
                protected String getApi() {
                    return "/api/red_boat/column_list";
                }
            };

        }else {
            ColumnResponse.DataBean dataBean = new ColumnResponse.DataBean();
            dataBean.elements = mColumns;
            dataBean.has_more = has_more;
            apiCallBack.onSuccess(dataBean);
            return null;
        }
    }
}
