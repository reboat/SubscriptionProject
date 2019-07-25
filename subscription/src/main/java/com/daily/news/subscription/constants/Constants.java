package com.daily.news.subscription.constants;

/**
 * Created by lixinke on 2017/9/26.
 */

public class Constants {
    public static final int MAX_COUNT = 10000;

    public interface Action {
        String SUBSCRIBE_SUCCESS = "subscribe_success";
        String HIT_RANK_SUCCESS = "hit_rank_success";
    }

    public interface Name {
        String SUBSCRIBE = "subscribe";
        String ID = "id";
        String SCORE = "score";
    }
}
