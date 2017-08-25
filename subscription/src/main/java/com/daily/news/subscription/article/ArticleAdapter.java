package com.daily.news.subscription.article;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lixinke on 2017/7/12.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private static final int ARTICLE_TYPE = 1;
    private static final int VIDEO_TYPE = 2;
    private static final int MULTIPLE_PICTURES = 3;

    private List<ArticleResponse.DataBean.Article> mArticles;

    public ArticleAdapter(List<ArticleResponse.DataBean.Article> articles) {
        mArticles = articles;
    }

    @Override
    public int getItemViewType(int position) {

        ArticleResponse.DataBean.Article article = mArticles.get(position);
        if (!TextUtils.isEmpty(article.video_url)) {
            return VIDEO_TYPE;
        }
        if (article.list_pics.size() == 1) {
            return ARTICLE_TYPE;
        }
        return MULTIPLE_PICTURES;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
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
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        ArticleResponse.DataBean.Article article = mArticles.get(position);
        holder.bindData(article);
    }

    @Override
    public int getItemCount() {
        return mArticles != null ? mArticles.size() : 0;
    }

    public void updateValue(List<ArticleResponse.DataBean.Article> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void addMore(List<ArticleResponse.DataBean.Article> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    static abstract class ViewHolder extends RecyclerView.ViewHolder {
        Resources mResources;

        public ViewHolder(View itemView) {
            super(itemView);
            mResources = itemView.getResources();
        }

        public abstract void bindData(ArticleResponse.DataBean.Article article);
    }

    static class ArticleViewHolder extends ViewHolder {
        @BindView(R2.id.article_imageView)
        ImageView mImageView;
        @BindView(R2.id.article_title)
        TextView mTitleView;
        @BindView(R2.id.article_info)
        TextView mInfoView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mResources = itemView.getResources();
        }

        @Override
        public void bindData(ArticleResponse.DataBean.Article article) {
            mTitleView.setText(article.list_title);
            Glide.with(itemView).load(article.list_pics.get(0)).into(mImageView);
            String info = String.format(Locale.getDefault(), mResources.getString(R.string.article_info_format), article.channel_name, article.read_count, article.like_count);
            mInfoView.setText(info);
        }
    }

    static class VideoViewHolder extends ViewHolder {
        @BindView(R2.id.video_imageView)
        ImageView mImageView;
        @BindView(R2.id.video_title)
        TextView mTitleView;
        @BindView(R2.id.video_category)
        TextView mCategoryView;
        @BindView(R2.id.video_info)
        TextView mInfoView;
        @BindView(R2.id.video_play_time_view)
        TextView mPlayTimeView;
        SimpleDateFormat mDateFormat = new SimpleDateFormat("hh:mm");


        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindData(ArticleResponse.DataBean.Article article) {
            mTitleView.setText(article.list_title);
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(itemView).applyDefaultRequestOptions(options).load(article.list_pics.get(0)).into(mImageView);
            mCategoryView.setText(article.channel_name);
            String info = String.format(Locale.getDefault(), mResources.getString(R.string.video_info_format), article.read_count, article.like_count);
            mInfoView.setText(info);
            mPlayTimeView.setText(mDateFormat.format(article.video_duration));
        }

        @OnClick(R2.id.video_recommend_view)
        void onRecommend(View view) {
            Toast.makeText(view.getContext(), "Recommend", Toast.LENGTH_SHORT).show();
        }

        @OnClick(R2.id.video_share)
        void onShare(View view) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
            shareIntent.setType("text/plain");
            view.getContext().startActivity(Intent.createChooser(shareIntent, "分享到"));
        }

        @OnClick(R2.id.video_play_view)
        void onPlayVideo(View view) {
            Toast.makeText(view.getContext(), "onPlayVideo", Toast.LENGTH_SHORT).show();
        }
    }

    static class MultiplePictureViewHolder extends ViewHolder {
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

        @Override
        public void bindData(ArticleResponse.DataBean.Article article) {
            mTitleView.setText(article.list_title);
            Glide.with(itemView).load(article.list_pics.get(0)).into(mImageView1);
            Glide.with(itemView).load(article.list_pics.get(1)).into(mImageView2);
            Glide.with(itemView).load(article.list_pics.get(2)).into(mImageView3);
            String info = String.format(Locale.getDefault(), mResources.getString(R.string.multiple_pic_info_format), article.read_count, article.like_count);
            mInfoView.setText(info);
        }
    }
}
