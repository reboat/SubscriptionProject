package com.daily.news.subscription.more.search;

import com.daily.news.subscription.subscribe.SubscribeStore;

import cn.daily.news.biz.core.network.compatible.APIBaseTask;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APIGetTask;
import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */
//
//public class SearchStore extends ColumnStore {
//    @Override
//    public Flowable<List<ColumnResponse.DataBean.ColumnBean>> getFlowable(String url) {
//        return Flowable.timer(400, TimeUnit.MILLISECONDS)
//                .flatMap(new Function<Long, Publisher<List<ColumnResponse.DataBean.ColumnBean>>>() {
//                    @Override
//                    public Publisher<List<ColumnResponse.DataBean.ColumnBean>> apply(@NonNull Long aLong) throws Exception {
//                        Random random = new Random();
//                        List<ColumnResponse.DataBean.ColumnBean> columns = new ArrayList<>();
//                        for (int i = 0; i < 30; i++) {
//                            ColumnResponse.DataBean.ColumnBean column = new ColumnResponse.DataBean.ColumnBean();
//                            column.article_count = random.nextInt();
//                            column.subscribe_count = random.nextInt();
//                            column.name = "搜索数据" + random.nextInt();
//                            column.subscribed = random.nextBoolean();
//                            columns.add(column);
//                        }
//
//                        return Flowable.just(columns);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread());
//    }
//
//    @Override
//    public APIBaseTask getTask(APICallBack<ColumnResponse.DataBean> apiCallBack) {
//        return new APIGetTask<ColumnResponse.DataBean>(apiCallBack) {
//            @Override
//            protected void onSetupParams(Object... params) {
//                put("keyword",params[0]);
//            }
//
//            @Override
//            protected String getApi() {
//                return "/api/column_class/search";
//            }
//        };
//    }
//}

public class SearchStore extends SubscribeStore implements SearchContract.Store {

    @Override
    public Flowable getFlowable(String url) {
        return null;
    }


    @Override
    public APIBaseTask getTask(APICallBack apiCallBack) {
        return new APIGetTask<SearchResponse.DataBean>(apiCallBack) {
            @Override
            public void onSetupParams(Object... params) {
                put("keyword", params[0]);
                if (params.length > 1) {
                    put("from", params[1]);
                }
            }

            @Override
            public String getApi() {
                return "/api/subscription/search";
            }
        };
    }

}