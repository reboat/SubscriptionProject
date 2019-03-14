package com.daily.news.subscription.rank;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.zjrb.daily.db.bean.ChannelBean;
import com.zjrb.daily.news.ui.adapter.TabPagerAdapter;

import java.util.List;

public class RankPagerAdapter extends TabPagerAdapter<ChannelBean> {

    public RankPagerAdapter(FragmentManager fm, @NonNull List<ChannelBean> channels) {
        super(fm, channels);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getName();
    }

    @Override
    protected boolean isUpdateEntity(ChannelBean newVal, ChannelBean oldVal) {
        return !TextUtils.equals(newVal.getName(), oldVal.getName());
    }

    @Override
    protected int getId(ChannelBean data) {
        return data.getId().intValue();
    }

    @Override
    public Fragment newFragment(ChannelBean channel) {
        Fragment f;
        switch (channel.getNav_type() != null ? channel.getNav_type() : "") {
            case "normal":
                f = RankFragment.instance(channel, false);
                break;
            default:
                f = RankFragment.instance(channel, false);
                break;
        }
        return f;
    }

    @Override
    public String toKey(ChannelBean channel) {
        return channel.getName();
    }

}

