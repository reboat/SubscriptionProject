package com.daily.news.subscription.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lixinke on 2017/7/14.
 */

public class DetailArticleAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_article,null);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.detail_article_imageView)
        ImageView imageView;
        @BindView(R2.id.detail_article_brief)
        TextView briefView;
        @BindView(R2.id.detail_article_count)
        TextView readCount;
        @BindView(R2.id.detail_article_good_count)
        TextView goodCount;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
