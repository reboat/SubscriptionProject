package com.daily.news.subscription.more.column;

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
                public void onSetupParams(Object... params) {
                    put("type", type);
                    put("class_id", id);
                }

                @Override
                public String getApi() {
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
