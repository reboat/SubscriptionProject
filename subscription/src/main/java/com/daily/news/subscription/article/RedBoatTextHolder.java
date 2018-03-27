package com.daily.news.subscription.article;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.daily.news.ui.holder.SuperNewsHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaoyangzhen on 2018/3/27.
 */

public class RedBoatTextHolder extends SuperNewsHolder {


    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_other)
    TextView tvOther;

    public RedBoatTextHolder(ViewGroup parent) {
        super(parent, R.layout.module_redboat_item_text);
        ButterKnife.bind(this, itemView);

    }

    @Override
    public void bindView() {
        if (mData == null) return;

        setTitleTag(tvTitle, null);
        tvOther.setText(mData.source);

        tvOther.setVisibility(TextUtils.isEmpty(tvOther.getText()) ? View.GONE : View.VISIBLE);
    }
}
