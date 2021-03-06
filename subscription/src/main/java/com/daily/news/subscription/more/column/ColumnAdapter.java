package com.daily.news.subscription.more.column;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ColumnAdapter extends BaseRecyclerAdapter<ColumnResponse.DataBean.ColumnBean> {
    private List<ColumnResponse.DataBean.ColumnBean> mColumnBeen;
    private OnSubscribeListener mOnSubscribeListener;

    public ColumnAdapter(List<ColumnResponse.DataBean.ColumnBean> columnBeen) {
        super(columnBeen);
        mColumnBeen = columnBeen;
    }

    public void updateValues(List<ColumnResponse.DataBean.ColumnBean> columnsBeens) {
        mColumnBeen.clear();
        mColumnBeen.addAll(columnsBeens);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item_column, parent, false);
        return new ColumnViewHolder(itemView,mOnSubscribeListener);
    }


    public void setOnSubscribeListener(OnSubscribeListener onSubscribeListener) {
        mOnSubscribeListener = onSubscribeListener;
    }

    /**
     * 点击和订阅回调
     */
    public interface OnSubscribeListener {
        void onSubscribe(ColumnResponse.DataBean.ColumnBean bean);
    }

    protected static class ColumnViewHolder extends BaseRecyclerViewHolder<ColumnResponse.DataBean.ColumnBean> {
        @BindView(R2.id.column_imageView)
        ImageView mImageView;
        @BindView(R2.id.column_title_view)
        TextView mTitleView;
        @BindView(R2.id.column_info_view)
        TextView mColumnInfoView;
        @BindView(R2.id.column_subscribe_btn)
        TextView mSubscribeBtn;
        @BindView(R2.id.padding_view)
        View mPaddingView;
        private OnSubscribeListener mOnSubscribeListener;

        public ColumnViewHolder(View itemView, OnSubscribeListener onSubscribeListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnSubscribeListener = onSubscribeListener;
        }

        @OnClick(R2.id.column_subscribe_btn)
        public void onSubscribe() {
            if (mOnSubscribeListener != null) {
                mOnSubscribeListener.onSubscribe(getData());
            }
        }

        public void hidePadingView(){
            mPaddingView.setVisibility(View.GONE);
        }

        @Override
        public void bindView() {
            final ColumnResponse.DataBean.ColumnBean column = getData();
            mTitleView.setText(column.name);

            String info = TextUtils.isEmpty(column.subscribe_count_general) ? "" : column.subscribe_count_general + "订阅  ";
            info += TextUtils.isEmpty(column.article_count_general) ? "" : column.article_count_general + "份稿件";

            mColumnInfoView.setText(info);
            String subscriptionText = column.subscribed ? itemView.getContext().getString(R.string.has_been_subscribed) : itemView.getContext().getString(R.string.subscription);
            mSubscribeBtn.setText(subscriptionText);
            mSubscribeBtn.setSelected(column.subscribed);
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.column_placeholder_big);
            Glide.with(itemView).load(column.pic_url).apply(options).into(mImageView);
        }
    }
}
