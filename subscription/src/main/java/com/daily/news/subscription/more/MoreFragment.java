package com.daily.news.subscription.more;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnStore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreFragment extends Fragment implements MoreContract.View {

    private MoreContract.Presenter mPresenter;

    @BindView(R2.id.more_category_list)
    RecyclerView mRecyclerView;
    MoreAdapter mMoreAdapter;
    List<Category> mCategories;

    public MoreFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
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
        mMoreAdapter = new MoreAdapter(mCategories);
        mRecyclerView.setAdapter(mMoreAdapter);
    }

    @Override
    public void setPresenter(MoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateValues(List<Category> items) {
        mMoreAdapter.updateValue(items);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
    }

    public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {

        private final List<Category> mValues;

        public MoreAdapter(List<Category> items) {
            mValues = items;
        }

        public void updateValue(List<Category> items) {
            mValues.clear();
            mValues.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public MoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_more, parent, false);
            return new MoreAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MoreAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mCategoryView.setText(mValues.get(position).class_name);
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
                    new ColumnPresenter(fragment, new ColumnStore());
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

