package com.daily.news.subscription.article;

import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;

import java.util.List;

/**
 * Created by lixinke on 2017/7/12.
 */

public class ArticleAdapter extends NewsBaseAdapter {
    public ArticleAdapter(List datas) {
        super(datas);
    }

    public void updateValue(List<ArticleResponse.DataBean.Article> elements) {
        getData().clear();
        getData().addAll(elements);
        notifyDataSetChanged();
    }
}
