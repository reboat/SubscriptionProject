package com.daily.news.subscription.article;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.daily.news.ui.holder.SuperNewsHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaoyangzhen on 2018/3/27.
 */

public class RedBoatImageHolder extends SuperNewsHolder {


    @BindView(R2.id.iv_picture)
    ImageView ivPicture;
    @BindView(R2.id.iv_tag)
    ImageView ivTag;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_other)
    TextView tvOther;

    public RedBoatImageHolder(ViewGroup parent) {
        super(parent, R.layout.module_redboat_item);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        if (mData == null) return;

        GlideApp.with(ivPicture).load(mData.urlByIndex(0))
                .apply(AppGlideOptions.smallOptions()).into(ivPicture);

        setTitleTag(tvTitle, ivTag);

        tvOther.setText(mData.source);
    }
}
