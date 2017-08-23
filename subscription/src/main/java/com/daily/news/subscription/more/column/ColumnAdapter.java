package com.daily.news.subscription.more.column;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.news.subscription.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerAdapter;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ColumnAdapter extends BaseRecyclerAdapter<Column, ColumnAdapter.ColumnViewHolder> {
    private OnSubscribeListener mOnSubscribeListener;


    private OnItemClickListener mOnItemClickListener;


    public ColumnAdapter(List<Column> columnsBeens) {
        super(columnsBeens);
    }

    public void updateValues(List<Column> columnsBeens) {
        getDatas().clear();
        getDatas().addAll(columnsBeens);
        notifyDataSetChanged();
    }

    public void addMoreValues(List<Column> columnsBeens) {
        getDatas().addAll(columnsBeens);
    }


    @Override
    public ColumnViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_column, parent, false);
        return new ColumnViewHolder(itemView, mOnSubscribeListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnSubscribeListener(OnSubscribeListener onSubscribeListener) {
        mOnSubscribeListener = onSubscribeListener;
    }

    /**
     * 点击和订阅回调
     */
    public interface OnSubscribeListener {
        void onSubscribe(Column bean);
    }

    protected static class ColumnViewHolder extends BaseRecyclerViewHolder<Column> {
        private final OnSubscribeListener mOnSubscribleListener;
        @BindView(R2.id.column_imageView)
        ImageView mImageView;
        @BindView(R2.id.column_title_view)
        TextView mTitleView;
        @BindView(R2.id.column_info_view)
        TextView mColumnInfosView;
        @BindView(R2.id.column_subscribe_btn)
        TextView mSubscribeBtn;

        public ColumnViewHolder(View itemView, OnSubscribeListener onSubscribeListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnSubscribleListener = onSubscribeListener;
        }

        @Override
        public void bindView() {
            Column column = getData();
            mTitleView.setText(column.name);
            mColumnInfosView.setText(String.format(Locale.getDefault(), itemView.getContext().getString(R.string.column_info_format), column.subscribe_count, column.article_count));

            String subscriptionText = column.subscribed ? itemView.getContext().getString(R.string.has_been_subscribed) : itemView.getContext().getString(R.string.subscription);
            mSubscribeBtn.setText(subscriptionText);
            mSubscribeBtn.setSelected(column.subscribed);
        }

        @OnClick(R2.id.column_subscribe_btn)
        public void onSubscribe(View view) {
            if (mOnSubscribleListener != null) {
                mOnSubscribleListener.onSubscribe(getData());
            }
        }
    }
}
