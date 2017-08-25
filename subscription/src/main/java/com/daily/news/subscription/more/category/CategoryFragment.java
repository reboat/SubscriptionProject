package com.daily.news.subscription.more.category;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.LocalColumnStore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment implements CategoryContract.View {

    private CategoryContract.Presenter mPresenter;

    @BindView(R2.id.more_category_list)
    RecyclerView mRecyclerView;
    CategoryAdapter mCategoryAdapter;
    List<Category> mCategories;

    @BindView(R2.id.column_tip_container)
    View mTipContainer;
    @BindView(R2.id.column_tip_view)
    TextView mTipView;
    @BindView(R2.id.column_progressBar)
    ProgressBar mProgressBar;

    public CategoryFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
    }

    private void setupRecyclerView() {
        mCategories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(mCategories);
        mRecyclerView.setAdapter(mCategoryAdapter);
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateValues(CategoryResponse response) {
        mCategoryAdapter.updateValue(response.elements);
        Bundle arguments = new Bundle();
        response.elements.get(0).is_selected = true;
        arguments.putString(ColumnFragment.ARG_ITEM_ID, String.valueOf(response.elements.get(0).class_id));
        ColumnFragment fragment = new ColumnFragment();
        fragment.setArguments(arguments);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.more_category_detail_container, fragment)
                .commit();
        new ColumnPresenter(fragment, new LocalColumnStore(response.elements.get(0).columns));
    }

    @Override
    public void showError(String message) {
        mTipContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTipView.setText(message);
    }

    @Override
    public void showProgressBar() {
        mTipContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTipView.setText(R.string.loading);
    }

    @Override
    public void hideProgressBar() {
        mTipContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        private final List<Category> mValues;

        public CategoryAdapter(List<Category> items) {
            mValues = items;
        }

        public void updateValue(List<Category> items) {
            mValues.clear();
            mValues.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new CategoryAdapter.ViewHolder(view);
        }

        int mCurPosition = 0;

        @Override
        public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {
            final Category category = mValues.get(position);
            holder.mItem = mValues.get(position);
            int textSize = category.is_selected ? 20 : 17;
            holder.mCategoryView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            holder.mCategoryView.setSelected(category.is_selected);
            holder.mCategoryView.setText(category.class_name);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ColumnFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.class_id));
                    ColumnFragment fragment = new ColumnFragment();
                    fragment.setArguments(arguments);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.more_category_detail_container, fragment)
                            .commit();
                    new ColumnPresenter(fragment, new LocalColumnStore(category.columns));
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
            public View mView;
            public Category mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);
            }
        }
    }
}

