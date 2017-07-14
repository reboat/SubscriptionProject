package com.daily.news.subscription.subscription.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daily.news.subscription.base.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.subscription.SubscriptionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 无订阅时，推荐订阅内容
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder> {
    private Context mContext;
    private List<SubscriptionBean.DataBean.RecommendBean> mRecommends;
    private OnSubscribeListener mOnSubscribeListener;
    private OnItemClickListener<SubscriptionBean.DataBean.RecommendBean> mOnItemClickListener;

    public RecommendAdapter(Context context, List<SubscriptionBean.DataBean.RecommendBean> list) {
        mContext = context;
        mRecommends = list;
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_recommend, null);
        return new RecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, final int position) {
        final SubscriptionBean.DataBean.RecommendBean recommend = mRecommends.get(position);
        Glide.with(holder.itemView).load(recommend.pic_url).into(holder.subImageView);
        holder.subArticleNumView.setText("文章" + recommend.article_count);
        holder.subNumView.setText("订阅数" + recommend.subscribe_count);
        holder.subTitleView.setText(recommend.name);
        holder.subBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSubscribeListener != null) {
                    mOnSubscribeListener.onSubscribe(recommend);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, recommend);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecommends != null ? mRecommends.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener<SubscriptionBean.DataBean.RecommendBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 订阅监听事件
     *
     * @param onSubscribeListener
     */
    public void setOnSubscribeListener(OnSubscribeListener onSubscribeListener) {
        mOnSubscribeListener = onSubscribeListener;
    }

    /**
     * 订阅接口
     */
    public interface OnSubscribeListener {
        /**
         * 点击订阅按钮时的回调
         *
         * @param recommend
         */
        void onSubscribe(SubscriptionBean.DataBean.RecommendBean recommend);
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
            ButterKnife.bind(this, itemView);
        }
    }
}
