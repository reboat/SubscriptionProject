package com.daily.news.subscription.more.column;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/21.
 */

public class LocalColumnStore implements ColumnContract.Store {
    private List<Column> mColumns;

    public LocalColumnStore(List<Column> columns) {
        mColumns = columns;
    }

    @Override
    public Flowable getFlowable(String url) {
        return Flowable.just(mColumns);
    }
}
