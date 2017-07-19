package com.daily.news.subscription.article;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lixinke on 2017/7/12.
 */

public class ArticleAdapter extends RecyclerView.Adapter {
    private static final int ARTICLE_TYPE = 1;
    private static final int VIDEO_TYPE = 2;
    private static final int MULTIPLE_PICTURES = 3;

    private List<Article> mArticles;

    public ArticleAdapter(List<Article> articles) {
        mArticles = articles;
    }

    @Override
    public int getItemViewType(int position) {

        Article article = mArticles.get(position);
        if (!TextUtils.isEmpty(article.video_url)) {
            return VIDEO_TYPE;
        }
        if (article.list_pics.size() == 1) {
            return ARTICLE_TYPE;
        }
        return MULTIPLE_PICTURES;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = null;
        switch (viewType) {
            case ARTICLE_TYPE:
                itemView = inflater.inflate(R.layout.item_article, parent, false);
                return new ArticleViewHolder(itemView);
            case VIDEO_TYPE:
                itemView = inflater.inflate(R.layout.item_video, parent, false);
                return new VideoViewHolder(itemView);
            case MULTIPLE_PICTURES:
                itemView = inflater.inflate(R.layout.item_multiple_picture, parent, false);
                return new MultiplePictureViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = mArticles.get(position);
        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            articleViewHolder.mTitleView.setText(article.list_title);
            Glide.with(holder.itemView).load(article.list_pics.get(0)).into(articleViewHolder.mImageView);
            articleViewHolder.mInfoView.setText(String.format(Locale.getDefault(), "%s %d万人阅读 %d万人点赞", article.channel_name,article.read_count, article.like_count));
        } else if (holder instanceof VideoViewHolder) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.mTitleView.setText(article.list_title);
            Glide.with(holder.itemView).load(article.list_pics.get(0)).into(videoViewHolder.mImageView);
            videoViewHolder.mCategoryView.setText(article.channel_name);
            videoViewHolder.mInfoView.setText(String.format(Locale.getDefault(), "%d万人观看 %d万人点赞", article.read_count, article.like_count));
        } else if (holder instanceof MultiplePictureViewHolder) {
            MultiplePictureViewHolder multiplePictureViewHolder = (MultiplePictureViewHolder) holder;
            multiplePictureViewHolder.mTitleView.setText(article.list_title);
            Glide.with(holder.itemView).load(article.list_pics.get(0)).into(multiplePictureViewHolder.mImageView1);
            Glide.with(holder.itemView).load(article.list_pics.get(1)).into(multiplePictureViewHolder.mImageView2);
            Glide.with(holder.itemView).load(article.list_pics.get(2)).into(multiplePictureViewHolder.mImageView3);
            multiplePictureViewHolder.mInfoView.setText(String.format(Locale.getDefault(), "%d万人观看 %d万人点赞", article.read_count, article.like_count));
        }
    }

    @Override
    public int getItemCount() {
        return mArticles != null ? mArticles.size() : 0;
    }

    public void updateValue(List<Article> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.article_imageView)
        ImageView mImageView;
        @BindView(R2.id.article_title)
        TextView mTitleView;
        @BindView(R2.id.article_info)
        TextView mInfoView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.video_imageView)
        ImageView mImageView;
        @BindView(R2.id.video_title)
        TextView mTitleView;
        @BindView(R2.id.video_recommend_view)
        TextView mRecommedView;
        @BindView(R2.id.video_category)
        TextView mCategoryView;
        @BindView(R2.id.video_info)
        TextView mInfoView;
        @BindView(R2.id.video_share)
        ImageView mShareView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class MultiplePictureViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.multiple_picture_article_title)
        TextView mTitleView;
        @BindView(R2.id.multiple_picture_imageView1)
        ImageView mImageView1;
        @BindView(R2.id.multiple_picture_imageView2)
        ImageView mImageView2;
        @BindView(R2.id.multiple_picture_imageView3)
        ImageView mImageView3;
        @BindView(R2.id.multiple_picture_info)
        TextView mInfoView;

        public MultiplePictureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
