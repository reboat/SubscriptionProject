package com.daily.news.subscription.more.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.FooterLoadMoreV2;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * Created by gaoyangzhen on 2018/3/14.
 */

public class SearchBaseAdapter extends BaseRecyclerAdapter<SearchResponse.DataBean.ColumnBean> implements LoadMoreListener<SearchResponse.DataBean> {

    private List<SearchResponse.DataBean.ColumnBean> mColumnBeen;
    private OnSubscribeListener mOnSubscribeListener;
    private FooterLoadMoreV2<SearchResponse.DataBean> mFooterLoadMore;
    private Object mKeyword;

    public SearchBaseAdapter(RecyclerView parent, List<SearchResponse.DataBean.ColumnBean> columnBeen) {
        super(columnBeen);
        mColumnBeen = columnBeen;
        mFooterLoadMore = new FooterLoadMoreV2<>(parent, this);
        addFooterView(mFooterLoadMore.getItemView());
    }

    public void updateValues(List<SearchResponse.DataBean.ColumnBean> columnBeans) {
        mColumnBeen.clear();
        mColumnBeen.addAll(columnBeans);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item_search, parent, false);
        return new ColumnViewHolder(itemView, mOnSubscribeListener);
    }


    public void setOnSubscribeListener(OnSubscribeListener onSubscribeListener) {
        mOnSubscribeListener = onSubscribeListener;
    }

    @Override
    public void onLoadMoreSuccess(SearchResponse.DataBean data, LoadMore loadMore) {

        if (data.elements == null || data.elements.size() == 0 || !data.has_more) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        } else {
            loadMore.setState(LoadMore.TYPE_IDLE);
            getData().addAll(data.elements);
            notifyDataSetChanged();
        }


    }

    @Override
    public void onLoadMore(LoadingCallBack<SearchResponse.DataBean> callback) {
        if (mKeyword == null) {
            if (mFooterLoadMore != null) {
                mFooterLoadMore.setState(LoadMore.TYPE_IDLE);
            }
            return;
        }
        new APIGetTask<SearchResponse.DataBean>(callback) {
            @Override
            public void onSetupParams(Object... params) {
                put("keyword", params[0]);
                put("from", params[1]);
            }

            @Override
            public String getApi() {
                return "/api/subscription/search";
            }
        }.exe(mKeyword, getDataSize());
    }

    public void restMoreState() {
        mKeyword = null;
        if (mFooterLoadMore != null) {
            mFooterLoadMore.setState(LoadMore.TYPE_IDLE);
        }
    }

    public void updateKeyword(Object keyword) {
        mKeyword = keyword;
    }

    /**
     * 点击和订阅回调
     */
    public interface OnSubscribeListener {
        void onSubscribe(SearchResponse.DataBean.ColumnBean bean);
    }

    protected static class ColumnViewHolder extends BaseRecyclerViewHolder<SearchResponse.DataBean.ColumnBean> {

        @BindView(R2.id.search_title)
        TextView searchTitle;
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

        public void hidePadingView() {
            mPaddingView.setVisibility(View.GONE);
        }

        @Override
        public void bindView() {
            final SearchResponse.DataBean.ColumnBean column = getData();
            mTitleView.setText(column.name);
            mColumnInfoView.setText(column.description);
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
