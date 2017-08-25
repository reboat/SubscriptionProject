package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribeStore;
import com.zjrb.coreprojectlibrary.api.base.APIPostTask;
import com.zjrb.coreprojectlibrary.api.callback.APICallBack;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lixinke on 2017/7/17.
 */

public class DetailStore extends SubscribeStore implements DetailContract.Store {

    public interface DetailService {
        @GET("/api/column/article_list")
        Flowable<DetailColumn> getDetail(@Query("column_id") String column_id, @Query("size") int size);
    }

    @Override
    public Flowable<DetailColumn> getFlowable(String url) {

//
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
//        DetailService service = retrofit.create(DetailService.class);
//        return service.getDetail(url, 10);


//        return Flowable.timer(400, TimeUnit.MILLISECONDS)
//                .flatMap(new Function<Long, Publisher<DetailColumn>>() {
//                    @Override
//                    public Publisher<DetailColumn> apply(@NonNull Long aLong) throws Exception {
//                        return Flowable.just(MockResponse.getInstance().getDetail(""));
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread());
        return null;
    }

    @Override
    public APIPostTask getTask(APICallBack callBack) {
        return new APIPostTask<DetailColumn>(callBack) {
            @Override
            protected void onSetupParams(Object... params) {
                put("column_id", params[0]);
                put("size", params[1]);
            }

            @Override
            protected String getApi() {
                return "/api/column/article_list";
            }
        };
    }
}
