package com.daily.news.subscription.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.model.Recommend;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lixinke on 2017/7/11.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder> {
    private Context mContext;
    private List<Recommend> mRecommends;
    private OnSubscribeListener mOnSubscribeListener;

    public RecommendAdapter(Context context, List<Recommend> list) {
        mContext = context;
        mRecommends = list;
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_recommend, null);
        return new RecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        Recommend recommend = mRecommends.get(position);
        Glide.with(holder.itemView).load(recommend.picUrl).into(holder.subImageView);
        holder.subArticleNumView.setText("文章"+recommend.articleCount);
        holder.subNumView.setText("订阅数"+recommend.subscribeCount);
        holder.subTitleView.setText(recommend.name);
        holder.subBtnView.setTag(recommend);
    }

    @Override
    public int getItemCount() {
        return mRecommends != null ? mRecommends.size() : 0;
    }

    public void setOnSubscribeListener(OnSubscribeListener onSubscribeListener) {
        mOnSubscribeListener = onSubscribeListener;
    }

    public interface OnSubscribeListener {
        void onSubscribe(Recommend recommend);
    }

    @OnClick(R2.id.recommend_subscription_btn)
    public void onSubscribe(View v) {
        if (mOnSubscribeListener != null) {
            mOnSubscribeListener.onSubscribe((Recommend) v.getTag());
        }
    }

    static class RecommendViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.recommend_imageView)
        ImageView subImageView;
        @BindView(R2.id.recommend_title)
        TextView subTitleView;
        @BindView(R2.id.recommend_subscription_num)
        TextView subNumView;
        @BindView(R2.id.recommend_article_num)
        TextView subArticleNumView;
        @BindView(R2.id.recommend_subscription_btn)
        TextView subBtnView;

        public RecommendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
