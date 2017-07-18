package com.daily.news.subscription.home.my;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.google.android.flexbox.FlexboxLayoutManager;

import butterknife.ButterKnife;

/**
 * Created by lixinke on 2017/7/12.
 */

public class SubscriptionAdapter extends RecyclerView.Adapter {
    private static final int ARTICLE_TYPE = 1;
    private static final int VIDEO_TYPE = 2;
    private Context mContext;

    public SubscriptionAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 3 == 0) {
            return ARTICLE_TYPE;
        }
        return VIDEO_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ARTICLE_TYPE:
                View view = View.inflate(mContext, R.layout.item_article, null);
                return new MySubArticleViewHolder(view);
            case VIDEO_TYPE:
                View view1 = View.inflate(mContext, R.layout.item_video, null);
                return new MySubVideoViewHolder(view1);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       ViewGroup.LayoutParams layoutParams= holder.itemView.getLayoutParams();
        if(layoutParams instanceof FlexboxLayoutManager.LayoutParams){
            ((FlexboxLayoutManager.LayoutParams)layoutParams).setFlexGrow(1f);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class MySubArticleViewHolder extends RecyclerView.ViewHolder {
        public MySubArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class MySubVideoViewHolder extends RecyclerView.ViewHolder {
        public MySubVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
