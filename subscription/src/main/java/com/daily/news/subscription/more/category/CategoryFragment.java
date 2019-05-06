package com.daily.news.subscription.more.category;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;

public class CategoryFragment extends Fragment implements CategoryContract.View {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.Action.SUBSCRIBE_SUCCESS.equals(intent.getAction())) {
                long id = intent.getLongExtra(Constants.Name.ID, 0);
                boolean subscribe = intent.getBooleanExtra(Constants.Name.SUBSCRIBE, false);

                if(mCategories != null) {
                    for (int i = 0; i < mCategories.size(); i++) {
                        if(mCategories.get(i).columns != null){
                        for (int j = 0; j < mCategories.get(i).columns.size(); j++) {
                            if (mCategories.get(i).columns.get(j).id == id) {
                                mCategories.get(i).columns.get(j).subscribed = subscribe;
                            }
                        }
                        }
                    }
                }


            }
        }
    };

    private CategoryContract.Presenter mPresenter;

    @BindView(R2.id.category_container)
    View mContainerView;
    @BindView(R2.id.more_category_list)
    RecyclerView mRecyclerView;
    CategoryAdapter mCategoryAdapter;
    List<CategoryResponse.DataBean.CategoryBean> mCategories;

    ColumnFragment fragment;
    private String mChannelName;
    private String mChannelId;


    public CategoryFragment() {
        new CategoryPresenter(this, new CategoryStore());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChannelName = getArguments().getString("channel_name");
            mChannelId = getArguments().getString("channel_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.subscription_fragment_more, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe(2);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setupRecyclerView() {
        mCategories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(mCategories);
        mRecyclerView.setAdapter(mCategoryAdapter);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateValues(CategoryResponse.DataBean dataBean) {
        if (dataBean != null && dataBean.elements != null && dataBean.elements.size() > 0) {
            mCategoryAdapter.updateValue(dataBean.elements);
            dataBean.elements.get(0).is_selected = true;
            fragment = new CategoryColumnFragment();
            Bundle args = new Bundle();
            args.putString("channel_name",mChannelName);
            args.putString("channel_id",mChannelId);
            args.putParcelableArrayList("columns", (ArrayList<? extends Parcelable>) dataBean.elements.get(0).columns);
            args.putString("className", dataBean.elements.get(0).class_name);
            fragment.setArguments(args);
            fragment.setFeedbackDataListener(mCategoryAdapter);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.more_category_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public void showError(Throwable message) {
    }

    @Override
    public LoadViewHolder getProgressBar() {
        return new LoadViewHolder(mContainerView, (ViewGroup) mContainerView.getParent());
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements ColumnFragment.FeedbackDataListener {

        private final List<CategoryResponse.DataBean.CategoryBean> mValues;

        public CategoryAdapter(List<CategoryResponse.DataBean.CategoryBean> items) {
            mValues = items;
        }

        public void updateValue(List<CategoryResponse.DataBean.CategoryBean> items) {
            mValues.clear();
            mValues.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subscription_item_category, parent, false);
            return new CategoryAdapter.ViewHolder(view);
        }

        int mCurPosition = 0;

        @Override
        public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {
            final CategoryResponse.DataBean.CategoryBean category = mValues.get(position);
            holder.mItem = mValues.get(position);
            holder.mCategoryView.setSelected(category.is_selected);
            holder.mCategoryView.setText(category.class_name);
            holder.mCategoryIndicator.setSelected(category.is_selected);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (category.is_selected) {
                        return;
                    }

                    new Analytics.AnalyticsBuilder(getContext(), "500005", "ClassNavigationSwitch", false)
                            .name("点击订阅号分类")
                            .pageType("订阅号分类检索页面")
                            .action(category.class_name)
                            .referClassName(mValues.get(mCurPosition).class_name)
                            .className(category.class_name)
                            .build()
                            .send();

                    ColumnFragment fragment = new CategoryColumnFragment();
                    Bundle args = new Bundle();
                    args.putString("className", category.class_name);
                    fragment.setArguments(args);
                    fragment.setFeedbackDataListener(CategoryAdapter.this);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.more_category_detail_container, fragment)
                            .commitAllowingStateLoss();
                    category.is_selected = true;
                    notifyItemChanged(position);
                    mValues.get(mCurPosition).is_selected = false;
                    notifyItemChanged(mCurPosition);
                    mCurPosition = position;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R2.id.more_item_category_name)
            TextView mCategoryView;
            @BindView(R2.id.more_item_category_indicator)
            View mCategoryIndicator;

            public View mView;
            public CategoryResponse.DataBean.CategoryBean mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);
            }
        }

        @Override
        public void feedback(ColumnResponse.DataBean dataBean) {
            List<ColumnResponse.DataBean.ColumnBean> list = mValues.get(mCurPosition).columns;
            mValues.get(mCurPosition).has_more = dataBean.has_more;
            if (list == null || list.size() == 0) {
                mValues.get(mCurPosition).columns = dataBean.elements;
            } else if (list.get(0).id != dataBean.elements.get(0).id) {
                mValues.get(mCurPosition).columns.addAll(dataBean.elements);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

